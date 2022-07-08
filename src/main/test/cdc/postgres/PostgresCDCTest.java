package cdc.postgres;

import cdc.schema.MyKafkaSerializationSchema;
import com.ververica.cdc.connectors.postgres.PostgreSQLSource;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

/**
 * @version V1.0
 * @ClassName: PostgresCDC
 * @Description: TODO
 * @Author: xiehp
 * @Date: 2022/5/11 12:50
 */
public class PostgresCDC {

    public static void main(String[] args) throws Exception {

        String[] classNames = Thread.currentThread().getStackTrace()[1].getClassName().split("\\.");
        String sourceName = classNames[classNames.length -1];

        //获取执行参数
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        // ***************************初始化配置信息***************************
        String config_env = parameterTool.get("env", "dev");
        GmallConfig.getSingleton().init(config_env);
        // ***************************初始化配置信息***************************

        Properties debeziumProperties = new Properties();
//        debeziumProperties.setProperty("snapshot.locking.mode", "none");// do not use lock
//        debeziumProperties.setProperty("scan.incremental.snapshot.enabled", "false");// do not use incremental snapshot
        // always、never、initial_only、exported、custom
        debeziumProperties.setProperty("snapshot.mode", "always");// do not use incremental snapshot

        SourceFunction<String> sourceFunction = PostgreSQLSource.<String>builder()
                .hostname("192.168.0.102")
                .port(5432)
                .database("postgres")
                .schemaList("public")
                .tableList("public.test")
                .username("postgres")
                .password("123456")
                .decodingPluginName("pgoutput")
                .deserializer(new JsonDebeziumDeserializationSchema())
                .build();

        sourceFunction = PostgreSQLSource.<String>builder()
                .hostname("192.168.0.102")
                .port(5432)
                .database("testdb")
                .schemaList("public")
                //.tableList("public.person")
                .username("postgres")
                .password("123456")
                .decodingPluginName("pgoutput")
                .slotName("flink2")
                .debeziumProperties(debeziumProperties)
                .deserializer(new JsonDebeziumDeserializationSchema())
                .build();

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
        //2.Flink-CDC将读取binlog的位置信息以状态的方式保存在CK,如果想要做到断点续传,需要从Checkpoint或者Savepoint启动程序
        //2.1 开启Checkpoint,每隔5秒钟做一次CK
        env.enableCheckpointing(5000L);
        //2.2 指定CK的一致性语义
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        //2.3 设置任务关闭的时候保留最后一次CK数据
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        //2.4 指定从CK自动重启策略
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, 20000L));
        //2.5 设置状态后端
        env.setStateBackend(new FsStateBackend(String.format(GmallConfig.FS_STATE_BACKEND,sourceName + "-" +config_env)));

        DataStreamSource<String> dataStreamSource = env.addSource(sourceFunction,
                        "postgres-source")
//                .setParallelism(4)
                ;
        dataStreamSource.print();

        String sinkTopic = "ods_default";
        Properties outprop= new Properties();
        outprop.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");

        FlinkKafkaProducer<String> myProducer = new FlinkKafkaProducer<String>(
                sinkTopic,
                new MyKafkaSerializationSchema("table"),
                outprop,
                FlinkKafkaProducer.Semantic.AT_LEAST_ONCE); // 容错

        dataStreamSource.addSink(myProducer).name("cdc_postgres_sink");

        env.execute();
    }

}
