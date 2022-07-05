package cdc.vx.mysql;

import cdc.schema.MyJsonDebeziumDeserializationSchema;
import cdc.schema.MyKafkaSerializationSchema;
import cdc.vx.utils.PropertiesUtil;
import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;
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
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer.Semantic;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

/**
 * @version V1.0
 * @ClassName: MySqlBinlogFlinkCDCStream2
 * @Description: TODO
 * @Author: xiehp
 * @Date: 2022/2/21 16:39
 */
public class CDCMySqlToKafka23 {

    public static void main(String[] args) throws Exception {

        String[] classNames = Thread.currentThread().getStackTrace()[1].getClassName().split(",");
        String className = classNames[classNames.length -1];
        System.setProperty("HADOOP_USER_NAME","root");

        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        // 初始化配置信息
        String config_env = parameterTool.get("env", "dev");
        GmallConfig.getSingleton().init(config_env);
        // 获取需要同步的数据库以及表名
        String[] databaseList = parameterTool.get("databaseList", "wms_szwt").split(",");
        String[] tableList = new String[]{
                "wms_szwt.md_sku"
//                "wms_szwt.md_warehouse",
//                "wms_szwt.md_sku",
//                "wms_szwt.md_client",
//                "wms_szwt.md_package_detail",
//                "wms_szwt.md_location",
//                "wms_szwt.md_code_dict",
//                "wms_szwt.inv_transaction",
//                "wms_szwt.inv_location_inventory",
//                "wms_szwt.inv_inventory_daily_report",
//                "wms_szwt.inb_asn_header",
//                "wms_szwt.inb_asn_detail",
//                "wms_szwt.oub_order_header",
//                "wms_szwt.oub_order_detail"
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
                case "earliest":
                    startupOptions = StartupOptions.earliest();
                    break;
                case "timestamp":
                    startupOptions = StartupOptions.timestamp(1000);
                    break;
            }
        }
        // 1 通过FlinkCDC构建sourceDatabase
        Properties debeziumProperties = PropertiesUtil.getDebeziumProperties();
        MySqlSource<String> sourceDatabase = MySqlSource.<String>builder()
                .hostname(GmallConfig.WMS4_MYSQL_HOSTNAME)
                .port(GmallConfig.WMS4_MYSQL_PORT)
                .serverTimeZone("Asia/Shanghai")
                .username(GmallConfig.WMS4_MYSQL_USERNAME)
                .password(GmallConfig.WMS4_MYSQL_PASSWORD)
                // 需要监控的database
                .databaseList(databaseList)
                .tableList(tableList)
                .serverId("1900-1999")
                // 反序列化
                .deserializer(new MyJsonDebeziumDeserializationSchema())
                //.includeSchemaChanges(true)
                .startupOptions(startupOptions)
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
        env.setStateBackend(new FsStateBackend(String.format(GmallConfig.FS_STATE_BACKEND,"wms4-kafka")));

        DataStreamSource<String> dataStreamSource = env.fromSource(sourceDatabase,
                WatermarkStrategy.noWatermarks(), Thread.currentThread().getStackTrace()[1].getClassName());

        // 3 打印数据
        String isPrint = parameterTool.get("isPrint", "n");
        if ("y".equals(isPrint.toLowerCase())) dataStreamSource.print();

        String sinkTopic = "ods_default";
        Properties outprop= new Properties();
        outprop.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, GmallConfig.KAFKA_SERVER);
        outprop.setProperty("transaction.timeout.ms", 60 * 5 * 1000 + "");

        FlinkKafkaProducer<String> myProducer = new FlinkKafkaProducer<String>(
                sinkTopic,
                new MyKafkaSerializationSchema("ods2"),
                outprop,
                Semantic.AT_LEAST_ONCE); // 容错

        dataStreamSource.addSink(myProducer).name("MySqlToKafka21_Sink");

        // 4 启动任务
        env.execute();

    }

}
