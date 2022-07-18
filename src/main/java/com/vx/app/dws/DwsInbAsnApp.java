package com.vx.app.dws;

import com.alibaba.fastjson.JSON;
import com.vx.app.func.InvTransactionCount;
import com.vx.app.func.InvTransactionCount2;
import com.vx.app.func.SinkToMySQL;
import com.vx.app.func.SinkToSqlServer;
import com.vx.bean.DwdInvTransaction;
import com.vx.common.GmallConfig;
import com.vx.utils.MyKafkaUtil;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.api.java.tuple.Tuple6;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

/**
 * @version V1.0
 * @ClassName: DwdInbAsnApp
 * @Description: TODO
 * @Author: xiehp
 * @Date: 2022/5/24 16:18
 */
public class DwsInbAsnApp {

    public static void main(String[] args) throws Exception {

        String[] classNames = Thread.currentThread().getStackTrace()[1].getClassName().split("\\.");
        String sourceName = classNames[classNames.length -1];

        //获取执行参数
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        // ***************************初始化配置信息***************************
        String config_env = parameterTool.get("env", "dev");
        GmallConfig.getSingleton().init(config_env);
        // ***************************初始化配置信息***************************
        //并行度
        Integer parallelism = parameterTool.getInt("parallelism",3);
        // 1.获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(parallelism);
        // 设置事件时间
        //env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        //设置周期生成Watermark间隔(10毫秒）
        //env.getConfig().setAutoWatermarkInterval(10L);
        // 设置检查点
        env.enableCheckpointing(15000L);
        //2.2 指定CK的一致性语义
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        //2.3 设置任务关闭的时候保留最后一次CK数据
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        //2.4 指定从CK自动重启策略
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, 30000L));
        //2.5 设置状态后端
        env.setStateBackend(new FsStateBackend(String.format(GmallConfig.FS_STATE_BACKEND,sourceName+"-"+config_env)));

        String topic = "dwd_inv_transaction";
        // kafka消费者
        FlinkKafkaConsumer<String> consumer = MyKafkaUtil.getKafkaSource(topic,Thread.currentThread().getStackTrace()[1].getClassName());
        // 输入流
        DataStream<String> edits = env.addSource(consumer).name(topic);
        //edits.print();
        // 聚合计算
        DataStream<DwdInvTransaction> calStream =
                edits
                        .map(data -> {
                            return JSON.parseObject(data, DwdInvTransaction.class);
                        })
                        .keyBy(new KeySelector<DwdInvTransaction, Tuple6<String, String, String, String, String, String>>() {
                            @Override
                            public Tuple6<String, String, String, String, String, String> getKey(DwdInvTransaction invTransaction) throws Exception {
                                return new Tuple6<>(
                                        invTransaction.getWarehouse_code(),
                                        invTransaction.getClient_code(),
                                        invTransaction.getClient_name(),
                                        invTransaction.getReference_type(),
                                        invTransaction.getLpn_no(),
                                        invTransaction.getEventTimeDay()
                                );
                            }
                        })
                        .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
                        .aggregate(new InvTransactionCount())
                        .filter(x -> {
                            return x.getInv_adjustment_qty().doubleValue() > 0;
                        })
                        .keyBy(new KeySelector<DwdInvTransaction, Tuple5<String, String, String, String, String>>() {
                            @Override
                            public Tuple5<String, String, String, String, String> getKey(DwdInvTransaction invTransaction) throws Exception {
                                return new Tuple5<>(
                                        invTransaction.getWarehouse_code(),
                                        invTransaction.getClient_code(),
                                        invTransaction.getClient_name(),
                                        invTransaction.getReference_type(),
                                        invTransaction.getEventTimeDay()
                                );
                            }
                        })
                        .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
                        .aggregate(new InvTransactionCount2())
                ;

        //calStream.print();
        //calStream.map(JSON::toJSONString).print();
        // sink到数据库
        calStream.addSink(new SinkToMySQL(GmallConfig.SINK_MYSQL_URL, GmallConfig.SINK_MYSQL_USERNAME, GmallConfig.SINK_MYSQL_PASSWORD)).setParallelism(1);
        calStream.addSink(new SinkToSqlServer(config_env)).name("inv_transaction_sqlserver");

        env.execute(Thread.currentThread().getStackTrace()[1].getClassName());
    }

}
