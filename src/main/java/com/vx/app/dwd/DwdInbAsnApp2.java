package com.vx.app.dwd;

import cdc.vx.bean.DimMdPackageDetail;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vx.app.func.DimAsyncFunction2;
import com.vx.app.schema.DwdKafkaSerializationSchema;
import com.vx.bean.DwdInvTransaction;
import com.vx.bean.DwdWideInbAsnContainer;
import com.vx.bean.InbAsnContainer;
import com.vx.bean.InbAsnHeader;
import com.vx.common.GmallConfig;
import com.vx.utils.DateTimeUtil;
import com.vx.utils.MyKafkaUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.util.CollectionUtil;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @version V1.0
 * @ClassName: DwdInbAsnApp2
 * @Description: TODO
 * @Author: cgengh01
 * @Date: 2022/7/5 16:24
 */
public class DwdInbAsnApp2 {

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
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        //设置周期生成Watermark间隔(10毫秒）
        env.getConfig().setAutoWatermarkInterval(10L);
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
        FlinkKafkaConsumer<String> inbAsnContainerConsumer = MyKafkaUtil.getKafkaSource(GmallConfig.KAFKA_SERVER,"ods_inb_asn_container",Thread.currentThread().getStackTrace()[1].getClassName());
        // 收货箱表输入流
        DataStream<String> iacDS = env.addSource(inbAsnContainerConsumer).name("ods_inb_asn_container");
        FlinkKafkaConsumer<String> inbAsnHeaderConsumer = MyKafkaUtil.getKafkaSource(GmallConfig.KAFKA_SERVER,"ods_inb_asn_header",Thread.currentThread().getStackTrace()[1].getClassName());
        //采购单头表
        DataStream<String> iahDS = env.addSource(inbAsnHeaderConsumer).name("ods_inb_asn_container");
        //3.将每行数据转换为JavaBean,提取时间戳生成WaterMark
//        iacDS.print("收货>>>>>>>>>>>>>>");
//        iahDS.print("采购>>>>>>>>>>>>>>");
        //收货箱表
        WatermarkStrategy<InbAsnContainer> inbAsnContainerWatermarkStrategy = WatermarkStrategy.<InbAsnContainer>forMonotonousTimestamps()
                .withTimestampAssigner(new SerializableTimestampAssigner<InbAsnContainer>() {
                    @Override
                    public long extractTimestamp(InbAsnContainer element, long recordTimestamp) {

                        return DateTimeUtil.toTs(element.getUpdated_dtm_loc());
                    }
                });
        //设置方法准备水位线 泛型就是你想要生产水位线的数据
        //采购单头表
        WatermarkStrategy<InbAsnHeader> inbAsnHeaderWatermarkStrategy = WatermarkStrategy.<InbAsnHeader>forMonotonousTimestamps()
                .withTimestampAssigner(new SerializableTimestampAssigner<InbAsnHeader>() {
                    @Override
                    public long extractTimestamp(InbAsnHeader element, long recordTimestamp) {
                        return DateTimeUtil.toTs(element.getUpdated_dtm_loc());
                    }
                });
        //双流join
        //先将流转成json对象 收货箱流
        KeyedStream<InbAsnContainer, String> inbAsnContainerStringKeyedStream = iacDS.map(jsonStr -> {
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
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
            InbAsnContainer inbAsnContainer = JSON.parseObject(data.toJSONString(), InbAsnContainer.class);
            inbAsnContainer.setDb(db);
            inbAsnContainer.setTable(table);
            inbAsnContainer.setOp(op);
            inbAsnContainer.setPrimaryKey(primaryKey);
            // 事件时间
            String eventTime = inbAsnContainer.getCreated_dtm_loc();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            // 事件日期
            String eventTimeDay = sdf.format(new Date(DateTimeUtil.toTs(eventTime)));
            inbAsnContainer.setEventTimeDay(eventTimeDay);
            return inbAsnContainer;
        }).assignTimestampsAndWatermarks(inbAsnContainerWatermarkStrategy)
                .keyBy(InbAsnContainer::getAsn_code);
//                .keyBy(new KeySelector<InbAsnContainer, Tuple2<String, String>>() {
//                    @Override
//                    public Tuple2<String, String> getKey(InbAsnContainer inbAsnContainer) throws Exception {
//                        return new Tuple2<>(
//                                inbAsnContainer.getWarehouse_code(),
//                                inbAsnContainer.getAsn_code()
//                        );
//                    }
//                });
//        inbAsnContainerStringKeyedStream.print("收货>>>>>>>>>>>>>>>>>>");
        //采购单头表
        KeyedStream<InbAsnHeader, String> inbAsnHeaderStringKeyedStream = iahDS.map(jsonStr -> {
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
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
            InbAsnHeader inbAsnHeader = JSON.parseObject(data.toJSONString(), InbAsnHeader.class);
            inbAsnHeader.setDb(db);
            inbAsnHeader.setTable(table);
            inbAsnHeader.setOp(op);
            inbAsnHeader.setPrimaryKey(primaryKey);
            // 事件时间
            String eventTime = inbAsnHeader.getCreated_dtm_loc();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            // 事件日期
            String eventTimeDay = sdf.format(new Date(DateTimeUtil.toTs(eventTime)));
            inbAsnHeader.setEventTimeDay(eventTimeDay);
            return inbAsnHeader;
        }).assignTimestampsAndWatermarks(inbAsnHeaderWatermarkStrategy)
                .keyBy(InbAsnHeader::getAsn_code);
//                .keyBy(new KeySelector<InbAsnHeader, Tuple2<String, String>>() {
//                    @Override
//                    public Tuple2<String, String> getKey(InbAsnHeader inbAsnHeader) throws Exception {
//                        return new Tuple2<>(
//                                inbAsnHeader.getWarehouse_code(),
//                                inbAsnHeader.getAsn_code()
//                        );
//                    }
//                });
//        inbAsnHeaderStringKeyedStream.print("采购单头>>>>>>>>>>>>>>>>");
        //设定时间进行join
        SingleOutputStreamOperator<DwdWideInbAsnContainer> processDS = inbAsnContainerStringKeyedStream.intervalJoin(inbAsnHeaderStringKeyedStream)
                .between(Time.days(-360),Time.days(360))
                .process(new ProcessJoinFunction<InbAsnContainer, InbAsnHeader, DwdWideInbAsnContainer>() {
                    @Override
                    public void processElement(InbAsnContainer inbAsnContainer, InbAsnHeader inbAsnHeader, Context context, Collector<DwdWideInbAsnContainer> collector) throws Exception {
                        collector.collect(new DwdWideInbAsnContainer(inbAsnContainer, inbAsnHeader));
                    }
                });
//        processDS.print("join>>>>>>>");
        String sinkTopic = "ods_default";
        Properties outprop = new Properties();
        outprop.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,GmallConfig.KAFKA_SERVER);
        FlinkKafkaProducer<String> myProducer = new FlinkKafkaProducer<String>(
                sinkTopic,
                new DwdKafkaSerializationSchema("dwd"),
                outprop,
                FlinkKafkaProducer.Semantic.AT_LEAST_ONCE); // 容错
//         sink到dwdkafka
        processDS.map(JSON::toJSONString).addSink(myProducer);
        env.execute(Thread.currentThread().getStackTrace()[1].getClassName());
    }

}
