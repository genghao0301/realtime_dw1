package com.vx.app.dwd;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vx.app.func.DimAsyncFunction2;
import com.vx.app.schema.DwdKafkaSerializationSchema;
import com.vx.bean.DwdInvTransaction;
import com.vx.common.GmallConfig;
import com.vx.utils.DateTimeUtil;
import com.vx.utils.MyKafkaUtil;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.util.CollectionUtil;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @version V1.0
 * @ClassName: DwdInbAsnApp3
 * @Description: TODO
 * @Author: cgengh01
 * @Date: 2022/7/6 9:24
 */
public class DwdInbAsnApp3 {

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
        FlinkKafkaConsumer<String> consumer = MyKafkaUtil.getKafkaSource(GmallConfig.KAFKA_SERVER,"ods_inv_transaction",Thread.currentThread().getStackTrace()[1].getClassName());
        // 输入流
        DataStream<String> edits = env.addSource(consumer).name("ods_inv_transaction");
//        edits.print();
        // 过滤清洗
        SingleOutputStreamOperator<DwdInvTransaction> calStream =
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
                            DwdInvTransaction dwdInvTransaction = JSON.parseObject(data.toJSONString(), DwdInvTransaction.class);
                            dwdInvTransaction.setDb(db);
                            dwdInvTransaction.setTable(table);
                            dwdInvTransaction.setOp(op);
                            dwdInvTransaction.setPrimaryKey(primaryKey);
                            // 事件时间
                            String eventTime = dwdInvTransaction.getUpdated_dtm_loc();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            // 事件日期
                            String eventTimeDay = sdf.format(new Date(DateTimeUtil.toTs(eventTime)));
                            dwdInvTransaction.setEventTimeDay(eventTimeDay);
                            return dwdInvTransaction;
                        })
                        .filter(data ->{
                            String inv_adjustment_type = data.getInv_adjustment_type();
                            String action_type = data.getAction_type();
                            return ("A".equals(inv_adjustment_type) && "200".equals(action_type))|| ("S".equals(inv_adjustment_type)&& "200".equals(action_type));
                        })
                ;
//        calStream.print();
        //查询库位所在库区名称
        SingleOutputStreamOperator<DwdInvTransaction> calStream2 = AsyncDataStream.unorderedWait(calStream,
                new DimAsyncFunction2<DwdInvTransaction>("DIM_MD_LOCATION",
                        "ROOM_CODE") {
                    @Override
                    public List<Tuple2<String, String>> getCondition(DwdInvTransaction input) {
                        List<Tuple2<String, String>> mdSkuTuples = new ArrayList<>();
                        mdSkuTuples.add(new Tuple2<>("location_name",input.getLocation_code()));
                        return mdSkuTuples;
                    }
                    public void join(DwdInvTransaction input, List<JSONObject> dimInfo) throws Exception {
                        if (CollectionUtil.isNullOrEmpty(dimInfo)) return;
                        JSONObject jsonObject = dimInfo.get(0);
                        String room_code = jsonObject.getString("room_code");
                        input.setRoom_code(room_code);
                    }
                    //?
                    @Override
                    public String getKey(DwdInvTransaction input) {return null;}
                    public void join(DwdInvTransaction input, JSONObject dimInfo) throws Exception {}
                }, 60, TimeUnit.SECONDS);
//        calStream2.print();
        //添加sku维度字段
        SingleOutputStreamOperator<DwdInvTransaction> calStream3 = AsyncDataStream.unorderedWait(calStream2,
                new DimAsyncFunction2<DwdInvTransaction>("DIM_MD_SKU",
                        "ITEM_CLASS_CODE") {
                    @Override
                    public List<Tuple2<String, String>> getCondition(DwdInvTransaction input) {
                        List<Tuple2<String, String>> mdSkuTuples = new ArrayList<>();
                        mdSkuTuples.add(new Tuple2<>("client_code",input.getClient_code()));
                        mdSkuTuples.add(new Tuple2<>("sku_code",input.getItem_code()));
                        return mdSkuTuples;
                    }
                    public void join(DwdInvTransaction input, List<JSONObject> dimInfo) throws Exception {
                        if (CollectionUtil.isNullOrEmpty(dimInfo)) return;
                        JSONObject jsonObject = dimInfo.get(0);
                        String item_class_code = jsonObject.getString("item_class_code");
                        input.setItem_class_code(item_class_code);
                    }
                    //?
                    @Override
                    public String getKey(DwdInvTransaction input) {return null;}
                    public void join(DwdInvTransaction input, JSONObject dimInfo) throws Exception {}
                }, 60, TimeUnit.SECONDS);
//        calStream3.print();
        //添加DMP园区名称添加进来
        SingleOutputStreamOperator<DwdInvTransaction> calStream4 = AsyncDataStream.unorderedWait(calStream3,
                new DimAsyncFunction2<DwdInvTransaction>("DIM_WAREHOUSE_CODE_MAPPING",
                        "SITE_CODE") {
                    @Override
                    public List<Tuple2<String, String>> getCondition(DwdInvTransaction input) {
                        List<Tuple2<String, String>> mdSkuTuples = new ArrayList<>();
                        mdSkuTuples.add(new Tuple2<>("warehouse_code",input.getWarehouse_code()));
                        return mdSkuTuples;
                    }
                    public void join(DwdInvTransaction input, List<JSONObject> dimInfo) throws Exception {
                        if (CollectionUtil.isNullOrEmpty(dimInfo)) return;
                        JSONObject jsonObject = dimInfo.get(0);
                        String wh_code = jsonObject.getString("site_code");
                        input.setWh_code(wh_code);
                    }
                    @Override
                    public String getKey(DwdInvTransaction input) {return null;}
                    public void join(DwdInvTransaction input, JSONObject dimInfo) throws Exception {}
                }, 60, TimeUnit.SECONDS);
//        calStream4.print();
        String sinkTopic = "ods_default";
        Properties outprop = new Properties();
        outprop.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,GmallConfig.KAFKA_SERVER);
        FlinkKafkaProducer<String> myProducer = new FlinkKafkaProducer<String>(
                sinkTopic,
                new DwdKafkaSerializationSchema("dwd"),
                outprop,
                FlinkKafkaProducer.Semantic.AT_LEAST_ONCE); // 容错
         //sink到数据库
        calStream4.map(JSON::toJSONString).addSink(myProducer);

        env.execute(Thread.currentThread().getStackTrace()[1].getClassName());
    }

}
