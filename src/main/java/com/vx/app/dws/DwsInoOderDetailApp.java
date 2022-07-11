package com.vx.app.dws;

import com.alibaba.fastjson.JSON;
import com.vx.app.func.SinkToMySQL;
import com.vx.app.schema.DwdKafkaSerializationSchema;
import com.vx.bean.DwdInbAsnContainer;
import com.vx.bean.DwdInvTransaction;
import com.vx.bean.DwsInoOderDetail;
import com.vx.common.GmallConfig;
import com.vx.utils.DateTimeUtil;
import com.vx.utils.MyKafkaUtil;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class DwsInoOderDetailApp {
    public static void main(String[] args) throws Exception {
        //获取执行参数
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        //并行度
        Integer parallelism = parameterTool.getInt("parallelism",3);
        boolean isSavePoint = parameterTool.getBoolean("isSavePoint",false);
        // 1.获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(parallelism);
        // 设置事件时间
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        //设置周期生成Watermark间隔(10毫秒）
        //env.getConfig().setAutoWatermarkInterval(10L);
        // 设置检查点
        env.enableCheckpointing(15000L);
        //2.2 指定CK的一致性语义
//        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
//        //2.3 设置任务关闭的时候保留最后一次CK数据
//        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
//        //2.4 指定从CK自动重启策略
//        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, 30000L));
        //2.5 设置状态后端
        if (isSavePoint)
            env.setStateBackend(new FsStateBackend(String.format(GmallConfig.FS_STATE_BACKEND,"dws-inb")));
        //dwd_inb_asn_containert
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

        //构建收货表流
        FlinkKafkaConsumer<String> iacConsumer = MyKafkaUtil.getKafkaSource("dwd_inb_asn_container", Thread.currentThread().getStackTrace()[1].getClassName());
        SingleOutputStreamOperator<String> iacDS = env.addSource(iacConsumer).name("dwd_inb_asn_container");
        //打印测试
//        iacDS.print("收货单流>>>>>>");
        //dwd_inv_transaction
        //构建交易表流
        FlinkKafkaConsumer<String> itConsumer = MyKafkaUtil.getKafkaSource("dwd_inv_transaction", Thread.currentThread().getStackTrace()[1].getClassName());
        SingleOutputStreamOperator<String> itDS = env.addSource(itConsumer).name("dwd_inv_transaction");
        //打印测试
//        itDS.print("交易表流>>>>>>");
        //转成实体类 判断类型  inv_adjustment_type  310 = A 其他为 S
        SingleOutputStreamOperator<DwdInbAsnContainer> iacMapDS = iacDS.map(new MapFunction<String, DwdInbAsnContainer>() {
            @Override
            public DwdInbAsnContainer map(String s) throws Exception {
                DwdInbAsnContainer dia = JSON.parseObject(s, DwdInbAsnContainer.class);
                //判断
                if (310 == dia.getStatus()){
                    dia.setInv_adjustment_type("A");
                }else {
                    dia.setInv_adjustment_type("S");
                }
                if (StringUtils.isEmpty(dia.getArrival_temperature()))
                    dia.setArrival_temperature("");
                return dia;
            }
        });
//        iacMapDS.print("类型增加>>>>");

        //加入水位线  Duration.ofDays(1)  延迟一天
//        KeyedStream<DwdInbAsnContainer, Tuple5<String,String, String, String, String>> iacKeyDS = iacMapDS.assignTimestampsAndWatermarks(WatermarkStrategy.<DwdInbAsnContainer>forBoundedOutOfOrderness(Duration.ofDays(1))
//                .withTimestampAssigner(new SerializableTimestampAssigner<DwdInbAsnContainer>() {
//                    @Override
//                    public long extractTimestamp(DwdInbAsnContainer dwdInbAsnContainer, long l) {
//                        return DateTimeUtil.toTs(dwdInbAsnContainer.getCreated_dtm_loc());
//                    }
//                })).keyBy(new KeySelector<DwdInbAsnContainer, Tuple5<String, String, String, String,String>>() {
//            @Override
//            public Tuple5<String, String, String, String,String> getKey(DwdInbAsnContainer dwdInbAsnContainer) throws Exception {
//                return new Tuple5<>(
//                        dwdInbAsnContainer.getEventTimeDay(),
//                        dwdInbAsnContainer.getClient_code(),
//                        dwdInbAsnContainer.getSku_code(),
//                        dwdInbAsnContainer.getPallet_code(),
//                        dwdInbAsnContainer.getInv_adjustment_type()
//                );
//            }
//        });
        //水位线 时间戳
//        SingleOutputStreamOperator<DwdInbAsnContainer> iacMapDs = iacMapDS.assignTimestampsAndWatermarks(WatermarkStrategy.<DwdInbAsnContainer>forBoundedOutOfOrderness(Duration.ofDays(1))
//                .withTimestampAssigner(new SerializableTimestampAssigner<DwdInbAsnContainer>() {
//                    @Override
//                    public long extractTimestamp(DwdInbAsnContainer dwdInbAsnContainer, long l) {
//                        return DateTimeUtil.toTs(dwdInbAsnContainer.getCreated_dtm_loc());
//                    }
//                }));
        //DwdInvTransaction
//        KeyedStream<DwdInvTransaction, Tuple5<String,String,String,String,String>> itKeyDS = itDS.map(s -> JSON.parseObject(s, DwdInvTransaction.class)).assignTimestampsAndWatermarks(WatermarkStrategy.<DwdInvTransaction>forBoundedOutOfOrderness(Duration.ofDays(1))
//                .withTimestampAssigner(new SerializableTimestampAssigner<DwdInvTransaction>() {
//                    @Override
//                    public long extractTimestamp(DwdInvTransaction dwdInvTransaction, long l) {
//                        return DateTimeUtil.toTs(dwdInvTransaction.getCreated_dtm_loc());
//                    }
//                })).keyBy(new KeySelector<DwdInvTransaction, Tuple5<String,String,String,String,String>>() {
//            @Override
//            public Tuple5<String,String,String,String,String> getKey(DwdInvTransaction dwdInvTransaction) throws Exception {
//                return new Tuple5<>(
//                        dwdInvTransaction.getEventTimeDay(),
//                        dwdInvTransaction.getClient_code(),
//                        dwdInvTransaction.getItem_code(),
//                        dwdInvTransaction.getLpn_no(),
//                        dwdInvTransaction.getInv_adjustment_type()
//                );
//            }
//        });
//        SingleOutputStreamOperator<DwdInvTransaction> itMapDS = itDS.map(s -> JSON.parseObject(s, DwdInvTransaction.class)).assignTimestampsAndWatermarks(WatermarkStrategy.<DwdInvTransaction>forBoundedOutOfOrderness(Duration.ofDays(1))
//                .withTimestampAssigner(new SerializableTimestampAssigner<DwdInvTransaction>() {
//                    @Override
//                    public long extractTimestamp(DwdInvTransaction dwdInvTransaction, long l) {
//                        return DateTimeUtil.toTs(dwdInvTransaction.getCreated_dtm_loc());
//                    }
//                }));
        //两条流进行关联  intervalJoin  有问题 出现笛卡尔积 多对多情况  考虑使用join
//        SingleOutputStreamOperator<DwsInoOderDetail> mapDs = iacKeyDS.intervalJoin(itKeyDS)
//                .between(Time.days(-1), Time.days(1))
//                .process(new ProcessJoinFunction<DwdInbAsnContainer, DwdInvTransaction, DwsInoOderDetail>() {
//                    @Override
//                    public void processElement(DwdInbAsnContainer dwdInbAsnContainer, DwdInvTransaction dwdInvTransaction, Context context, Collector<DwsInoOderDetail> collector) throws Exception {
//                        collector.collect(new DwsInoOderDetail(dwdInbAsnContainer, dwdInvTransaction));
//                    }
//                }).map((MapFunction<DwsInoOderDetail, DwsInoOderDetail>) dwsInoOderDetail -> {
//                    dwsInoOderDetail.setEtl_time(df.format(new Date()));
//                    return dwsInoOderDetail;
//                });
        SingleOutputStreamOperator<DwdInvTransaction> itMapDs = itDS.map(s -> JSON.parseObject(s, DwdInvTransaction.class));
        DataStream<DwsInoOderDetail> applyDS = iacMapDS.join(itMapDs)
                .where(new KeySelector<DwdInbAsnContainer, Tuple5<String,String,String,String,String>>() {
                    @Override
                    public Tuple5<String, String, String, String, String> getKey(DwdInbAsnContainer dwdInbAsnContainer) throws Exception {
                        return new Tuple5<>(
                                dwdInbAsnContainer.getEventTimeDay(),
                                dwdInbAsnContainer.getInv_adjustment_type(),
                                dwdInbAsnContainer.getClient_code(),
                                dwdInbAsnContainer.getSku_code(),
                                dwdInbAsnContainer.getPallet_code()
                        );
                    }
                })
                .equalTo(new KeySelector<DwdInvTransaction, Tuple5<String, String, String, String, String>>() {
                    @Override
                    public Tuple5<String, String, String, String, String> getKey(DwdInvTransaction dwdInvTransaction) throws Exception {
                        return new Tuple5<>(
                                dwdInvTransaction.getEventTimeDay(),
                                dwdInvTransaction.getInv_adjustment_type(),
                                dwdInvTransaction.getClient_code(),
                                dwdInvTransaction.getItem_code(),
                                dwdInvTransaction.getLpn_no()
                        );
                    }
                })
                .window(TumblingEventTimeWindows.of(Time.days(1)))
                .apply(new JoinFunction<DwdInbAsnContainer, DwdInvTransaction, DwsInoOderDetail>() {
                    @Override
                    public DwsInoOderDetail join(DwdInbAsnContainer dwdInbAsnContainer, DwdInvTransaction dwdInvTransaction) throws Exception {
                        return new DwsInoOderDetail(dwdInbAsnContainer, dwdInvTransaction);
                    }
                });
        //输出测试
        applyDS.map(JSON::toJSONString).print("合并测试>>>>>>");        //sink到sqlserver

//        String sinkTopic = "ods_default";
//        Properties outprop = new Properties();
//        outprop.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, GmallConfig.KAFKA_SERVER);
//        FlinkKafkaProducer<String> myProducer = new FlinkKafkaProducer<String>(
//                sinkTopic,
//                new DwdKafkaSerializationSchema("dws"),
//                outprop,
//                FlinkKafkaProducer.Semantic.AT_LEAST_ONCE); // 容错

        //sink到数据库
//        applyDS.map(JSON::toJSONString).addSink(myProducer);

        //启动任务
        env.execute(Thread.currentThread().getStackTrace()[1].getClassName());

    }
}
