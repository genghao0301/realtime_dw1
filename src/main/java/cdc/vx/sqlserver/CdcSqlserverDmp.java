package cdc.vx.sqlserver;

import cdc.schema.MyJsonDebeziumDeserializationSchema;
import cdc.schema.MyKafkaSerializationSchema;
import cdc.vx.utils.CdcConstant;
import cdc.vx.utils.PropertiesUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.connectors.sqlserver.table.StartupOptions;
import com.vx.app.func.DimSink2;
import com.vx.common.GmallConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import com.ververica.cdc.connectors.sqlserver.SqlServerSource;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

/**
 * @version V1.0
 * @ClassName: CdcSqlserverDmp
 * @Description: SqlServer同步数据
 * @Author: xiehp
 * @Date: 2022/6/29 16:31
 */
public class CdcSqlserverDmp {

    public static void main(String[] args) throws Exception {

        System.setProperty("HADOOP_USER_NAME","root");

        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        // 初始化配置信息
        String config_env = parameterTool.get("env", "dev");
        GmallConfig.getSingleton().init(config_env);
        // 获取需要同步的数据库以及表名
        String database = parameterTool.get("database", "DMP_DB");
        String[] tableList = new String[]{
                "dbo.BIZ_HOUSE_INFO"
                ,"dbo.WAREHOUSE_CODE_MAPPING"
        };
        if (StringUtils.isNotBlank(parameterTool.get("tableList")))
            tableList = parameterTool.get("tableList").split(",");
        //并行度
        Integer parallelism = parameterTool.getInt("parallelism",3);
        StartupOptions startupOptions = StartupOptions.initial();
        if (StringUtils.isNotBlank(parameterTool.get("startupOptions"))) {
            String startupOptionsStr = parameterTool.get("startupOptions");
            switch (startupOptionsStr) {
                case "initial":
                    startupOptions = StartupOptions.initial();
                    break;
                case "latest":
                    startupOptions = StartupOptions.latest();
                    break;
                case "initialOnly":
                    startupOptions = StartupOptions.initialOnly();
                    break;
            }
        }
        // 1 通过FlinkCDC构建sourceDatabase
        Properties debeziumProperties = PropertiesUtil.getDebeziumProperties();
        SourceFunction<String> sourceFunction = SqlServerSource.<String>builder()
                .hostname(GmallConfig.DMP_SQLSERVER_HOSTNAME)
                .port(GmallConfig.DMP_SQLSERVER_PORT)
                .database(database) // monitor sqlserver database
                .tableList(tableList) // monitor products table
                .username(GmallConfig.DMP_SQLSERVER_USERNAME)
                .password(GmallConfig.DMP_SQLSERVER_PASSWORD)
                .startupOptions(startupOptions)
                .deserializer(new MyJsonDebeziumDeserializationSchema()) // converts SourceRecord to JSON String
                .debeziumProperties(debeziumProperties)
                .build();
        // 2 创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(parallelism);

        //env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
        //2.Flink-CDC将读取binlog的位置信息以状态的方式保存在CK,如果想要做到断点续传,需要从Checkpoint或者Savepoint启动程序
        //2.1 开启Checkpoint,每隔5秒钟做一次CK
        env.enableCheckpointing(15000L);
        //2.2 指定CK的一致性语义
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        //2.3 设置任务关闭的时候保留最后一次CK数据
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        //2.4 指定从CK自动重启策略
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, 30000L));
        //2.5 设置状态后端
        env.setStateBackend(new FsStateBackend(String.format(GmallConfig.FS_STATE_BACKEND,"dmp-kafka")));

        DataStreamSource<String> dataStreamSource = env.addSource(sourceFunction);
        dataStreamSource.name(Thread.currentThread().getStackTrace()[1].getClassName());

        // 3 打印数据
        String isPrint = parameterTool.get("isPrint", "n");
        if ("y".equals(isPrint.toLowerCase())) dataStreamSource.print();
        // 数据分流
        // 创建分流标记
        OutputTag<String> kafkaTag = new OutputTag<String>("kafka-tag"){};
        OutputTag<String> hbaseTag = new OutputTag<String>("hbase-tag"){};
        // 分流处理
        SingleOutputStreamOperator<String> processStream = dataStreamSource.process(new ProcessFunction<String, String>() {
            @Override
            public void processElement(String value, ProcessFunction<String, String>.Context ctx, Collector<String> out) throws Exception {
                JSONObject jsonObject = JSON.parseObject(value);
                String table = jsonObject.getString("table");
                if (CdcConstant.DMP_DIM_TABLES.contains(jsonObject.getString("table"))) {
                    ctx.output(hbaseTag, value);
                } else {
                    ctx.output(kafkaTag, value);
                }
            }
        });
        // 输出
        DataStream<String> kafkaStream = processStream.getSideOutput(kafkaTag);
        DataStream<String> hbaseStream = processStream.getSideOutput(hbaseTag);

        // 输出到kafka
        String sinkTopic = "ods_default";
        Properties outprop= new Properties();
        outprop.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, GmallConfig.KAFKA_SERVER);
        outprop.setProperty("transaction.timeout.ms", 60 * 5 * 1000 + "");
        FlinkKafkaProducer<String> myProducer = new FlinkKafkaProducer<String>(
                sinkTopic,
                new MyKafkaSerializationSchema("ods"),
                outprop,
                FlinkKafkaProducer.Semantic.EXACTLY_ONCE); // 容错
        dataStreamSource.addSink(myProducer).name("Dmp_Sink_Kafka");

        // 输出到hbase
        hbaseStream.map(JSONObject::parseObject).addSink(new DimSink2()).name("Dmp_Sink_Hbase");
        // 4 启动任务
        env.execute(Thread.currentThread().getStackTrace()[1].getClassName());

    }

}
