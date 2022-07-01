package cdc.vx.sqlserver;

import cdc.schema.MyJsonDebeziumDeserializationSchema;
import cdc.schema.MyKafkaSerializationSchema;
import cdc.vx.utils.PropertiesUtil;
import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.connectors.sqlserver.table.StartupOptions;
import com.vx.common.GmallConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import com.ververica.cdc.connectors.sqlserver.SqlServerSource;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
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
        String hostname = parameterTool.get("hostname", "prod01.public.b8f689b2a39a.database.chinacloudapi.cn");
        Integer port = parameterTool.getInt("port",3342);
        String username = parameterTool.get("username", "data_admin");
        String password = parameterTool.get("password", "data_admin_#320AB_");
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
                .hostname(hostname)
                .port(port)
                .database(database) // monitor sqlserver database
                .tableList(tableList) // monitor products table
                .username(username)
                .password(password)
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
        //env.setStateBackend(new FsStateBackend("hdfs://shucang001:8020/user/flink/dmp-kf/checkpoints"));

        DataStreamSource<String> dataStreamSource = env.addSource(sourceFunction);
        dataStreamSource.name(Thread.currentThread().getStackTrace()[1].getClassName());

        // 3 打印数据
        String isPrint = parameterTool.get("isPrint", "n");
        if ("y".equals(isPrint.toLowerCase())) dataStreamSource.print();

        String sinkTopic = "ods_default";
        Properties outprop= new Properties();
        outprop.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, GmallConfig.KAFKA_SERVER);
        outprop.setProperty("transaction.timeout.ms", 60 * 5 * 1000 + "");

        FlinkKafkaProducer<String> myProducer = new FlinkKafkaProducer<String>(
                sinkTopic,
                new MyKafkaSerializationSchema("ODS"),
                outprop,
                FlinkKafkaProducer.Semantic.EXACTLY_ONCE); // 容错

        dataStreamSource.addSink(myProducer).name("SqlServerToKafka21_Sink");

        // 4 启动任务
        env.execute(Thread.currentThread().getStackTrace()[1].getClassName());

    }

}
