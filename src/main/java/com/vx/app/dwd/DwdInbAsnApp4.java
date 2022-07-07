package com.vx.app.dwd;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vx.bean.InbAsnContainer;
import com.vx.common.GmallConfig;
import com.vx.utils.DateTimeUtil;
import com.vx.utils.MyKafkaUtil;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @version V1.0
 * @ClassName: DwdInbAsnApp4
 * @Description: TODO
 * @Author: cgengh01
 * @Date: 2022/7/6 10:24
 */
public class DwdInbAsnApp4 {

    public static void main(String[] args) throws Exception {

        //获取执行参数
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        //并行度
        Integer parallelism = parameterTool.getInt("parallelism",3);
        String eventTimeCheck = parameterTool.get("eventTimeCheck");
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
//        env.setStateBackend(new FsStateBackend(String.format(GmallConfig.FS_STATE_BACKEND,"dwd-inb")));

        // kafka消费者
        FlinkKafkaConsumer<String> consumer = MyKafkaUtil.getKafkaSource(GmallConfig.KAFKA_SERVER,"ods_inb_asn_container",Thread.currentThread().getStackTrace()[1].getClassName());
        // 输入流
        DataStream<String> edits = env.addSource(consumer).name("ods_inb_asn_container");
//        edits.print();
        // 过滤清洗
        SingleOutputStreamOperator<InbAsnContainer> calStream =
                edits
                        .map(x -> {

                            JSONObject jsonObject = JSONObject.parseObject(x);
                            // 数据库名
                            String db = jsonObject.getString("db");
                            // 表名
                            String table = jsonObject.getString("table");
                            // 操作名称
                            String op = jsonObject.getString("op");
                            // 主键字段名称
                            String primaryKey = jsonObject.getString("primaryKey");
                            // 获取数据
                            JSONObject data = jsonObject.getJSONObject("after");
                            if ("delete".equals(op)){
                                data =  jsonObject.getJSONObject("before");
                            }
                            //构建实例
                            InbAsnContainer inbAsnContainer = JSON.parseObject(data.toJSONString(), InbAsnContainer.class);
                            inbAsnContainer.setDb(db);
                            inbAsnContainer.setTable(table);
                            inbAsnContainer.setOp(op);
                            inbAsnContainer.setPrimaryKey(primaryKey);
                            // 事件时间
                            String eventTime = inbAsnContainer.getUpdated_dtm_loc();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            // 事件日期
                            String eventTimeDay = sdf.format(new Date(DateTimeUtil.toTs(eventTime)));
                            inbAsnContainer.setEventTimeDay(eventTimeDay);
                            return inbAsnContainer;
                        });
//        calStream.print();
//        String sinkTopic = "ods_default";
//        Properties outprop = new Properties();
//        outprop.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,GmallConfig.KAFKA_SERVER);
//        FlinkKafkaProducer<String> myProducer = new FlinkKafkaProducer<String>(
//                sinkTopic,
//                new DwdKafkaSerializationSchema("dwd"),
//                outprop,
//                FlinkKafkaProducer.Semantic.AT_LEAST_ONCE); // 容错
        // sink到kafka对应主题
//        calStream4.map(JSON::toJSONString).addSink(myProducer);

        env.execute(Thread.currentThread().getStackTrace()[1].getClassName());
    }

}
