package cdc.vx.mysql;

import cdc.schema.MyJsonDebeziumDeserializationSchema;
import cdc.schema.MyKafkaSerializationSchema;
import cdc.schema.MySqlCustomerDeserialization;
import cdc.vx.utils.PropertiesUtil;
import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.vx.common.GmallConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.utils.ParameterTool;
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
public class CDCMySqlToKafka22 {

    public static void main(String[] args) throws Exception {

        String[] classNames = Thread.currentThread().getStackTrace()[1].getClassName().split(",");
        String className = classNames[classNames.length -1];

        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String hostname = parameterTool.get("hostname", "vx-mysql-prod4-slave.mysql.database.chinacloudapi.cn");
        Integer port = parameterTool.getInt("port",3306);
        String username = parameterTool.get("username", "repl_finreport@vx-mysql-prod4-slave");
        String password = parameterTool.get("password", "Vs*xhabt3I");
        String[] databaseList = parameterTool.get("databaseList", "wms_szwt").split(",");
        String[] tableList = new String[]{
                "wms_szwt.md_warehouse",
                "wms_szwt.md_sku",
                "wms_szwt.md_client",
                "wms_szwt.md_package_detail",
                "wms_szwt.md_location",
                "wms_szwt.md_code_dict",
                "wms_szwt.inv_transaction",
                "wms_szwt.inv_location_inventory",
                "wms_szwt.inv_inventory_daily_report",
                "wms_szwt.inb_asn_header",
                "wms_szwt.inb_asn_detail",
                "wms_szwt.oub_order_header",
                "wms_szwt.oub_order_detail"
        };
        String[] databaseList2 = parameterTool.get("databaseList", "wms_shhg").split(",");
        String[] tableList2 = new String[]{
                "wms_shhg.md_warehouse",
                "wms_shhg.md_sku",
                "wms_shhg.md_client",
                "wms_shhg.md_package_detail",
                "wms_shhg.md_location",
                "wms_shhg.md_code_dict",
                "wms_shhg.inv_transaction",
                "wms_shhg.inv_location_inventory",
                "wms_shhg.inv_inventory_daily_report",
                "wms_shhg.inb_asn_header",
                "wms_shhg.inb_asn_detail",
                "wms_shhg.oub_order_header",
                "wms_shhg.oub_order_detail"
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
                .hostname(hostname)
                .port(port)
                .serverTimeZone("Asia/Shanghai")
                .username(username)
                .password(password)
                // 需要监控的database
                .databaseList(databaseList)
                .tableList(tableList)
                .serverId("1700-1799")
                // 反序列化
                .deserializer(new MySqlCustomerDeserialization())
                //.includeSchemaChanges(true)
                .startupOptions(startupOptions)
                .debeziumProperties(debeziumProperties)
                .build();
        // 1 通过FlinkCDC构建sourceDatabase
        MySqlSource<String> sourceDatabase2 = MySqlSource.<String>builder()
                .hostname(hostname)
                .port(port)
                .serverTimeZone("Asia/Shanghai")
                .username(username)
                .password(password)
                // 需要监控的database
                .databaseList(databaseList2)
                .tableList(tableList2)
                .serverId("1800-1899")
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
        //env.setStateBackend(new FsStateBackend("hdfs://shucang001:8020/user/flink/wms4-kf/checkpoints"));

        DataStreamSource<String> dataStreamSource = env.fromSource(sourceDatabase,
                WatermarkStrategy.noWatermarks(), Thread.currentThread().getStackTrace()[1].getClassName());

        DataStreamSource<String> dataStreamSource2 = env.fromSource(sourceDatabase2,
                WatermarkStrategy.noWatermarks(), Thread.currentThread().getStackTrace()[1].getClassName());
        // 3 打印数据
        String isPrint = parameterTool.get("isPrint", "n");
        if ("y".equals(isPrint.toLowerCase())) dataStreamSource2.print();

        String sinkTopic = "ods_default";
        Properties outprop= new Properties();
        outprop.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, GmallConfig.KAFKA_SERVER);
        outprop.setProperty("transaction.timeout.ms", 60 * 5 * 1000 + "");


        FlinkKafkaProducer<String> myProducer = new FlinkKafkaProducer<String>(
                sinkTopic,
                new MyKafkaSerializationSchema("ods2"),
                outprop,
                Semantic.AT_LEAST_ONCE); // 容错
        FlinkKafkaProducer<String> myProducer2 = new FlinkKafkaProducer<String>(
                sinkTopic,
                new MyKafkaSerializationSchema("ods3"),
                outprop,
                Semantic.AT_LEAST_ONCE); // 容错

        dataStreamSource.addSink(myProducer).name("MySqlToKafka21_Sink");
        dataStreamSource2.addSink(myProducer2).name("MySqlToKafka22_Sink");

        // 4 启动任务
        env.execute();

    }

}
