package com.vx.app.dwd;

import cdc.vx.bean.DimMdPackageDetail;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vx.app.func.DimAsyncFunction2;
import com.vx.app.schema.DwdKafkaSerializationSchema;
import com.vx.bean.DwdInvTransaction;
import com.vx.common.GmallConfig;
import com.vx.utils.DateTimeUtil;
import com.vx.utils.MyKafkaUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @version V1.0
 * @ClassName: DwdInbAsnApp
 * @Description: TODO
 * @Author: xiehp
 * @Date: 2022/5/24 16:18
 */
public class DwdInbAsnApp {

    public static void main(String[] args) throws Exception {

        //获取执行参数
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        // ***************************初始化配置信息***************************
        String config_env = parameterTool.get("env", "dev");
        GmallConfig.getSingleton().init(config_env);
        // ***************************初始化配置信息***************************
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
        env.setStateBackend(new FsStateBackend(String.format(GmallConfig.FS_STATE_BACKEND,"dwd-inb")));

        // kafka消费者
        FlinkKafkaConsumer<String> consumer = MyKafkaUtil.getKafkaSource(GmallConfig.KAFKA_SERVER,"ods_inv_transaction",Thread.currentThread().getStackTrace()[1].getClassName());
        // 输入流
        DataStream<String> edits = env.addSource(consumer).name("ods_inv_transaction");
        //edits.print();
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
                            DwdInvTransaction dwdInvTransaction = JSON.parseObject(data.toJSONString(), DwdInvTransaction.class);
                            dwdInvTransaction.setDb(db);
                            dwdInvTransaction.setTable(table);
                            dwdInvTransaction.setOp(op);
                            dwdInvTransaction.setPrimaryKey(primaryKey);
                            // 事件时间
                            String eventTime = dwdInvTransaction.getCreated_dtm_loc();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            // 事件日期
                            String eventTimeDay = sdf.format(new Date(DateTimeUtil.toTs(eventTime)));
                            dwdInvTransaction.setEventTimeDay(eventTimeDay);
                            return dwdInvTransaction;
                        })
                        .filter(data -> {
                            // 交易类型
                            String action_type = data.getAction_type();
                            String eventTimeDay = data.getEventTimeDay();
                            return StringUtils.isNotBlank(eventTimeCheck) ? eventTimeCheck.equals(eventTimeDay) && ("100".equals(action_type) || "110".equals(action_type) || "120".equals(action_type))
                            : ("100".equals(action_type) || "110".equals(action_type) || "120".equals(action_type)) ;
                        });

        SingleOutputStreamOperator<DwdInvTransaction> calStream2 = AsyncDataStream.unorderedWait(calStream,
                new DimAsyncFunction2<DwdInvTransaction>("DIM_MD_SKU",
                        "WAREHOUSE_CODE,CLIENT_CODE,SKU_CODE,PACK_CODE",config_env) {
                    @Override
                    public List<Tuple2<String, String>> getCondition(DwdInvTransaction input) {
                        List<Tuple2<String, String>> mdSkuTuples = new ArrayList<>();
                        mdSkuTuples.add(new Tuple2<>("warehouse_code",input.getWarehouse_code()));
                        mdSkuTuples.add(new Tuple2<>("client_code",input.getClient_code()));
                        mdSkuTuples.add(new Tuple2<>("sku_code",input.getItem_code()));
                        return mdSkuTuples;
                    }
                    public void join(DwdInvTransaction input, List<JSONObject> dimInfo) throws Exception {
                        if (CollectionUtil.isNullOrEmpty(dimInfo)) return;
                        JSONObject jsonObject = dimInfo.get(0);
                        String pack_code = jsonObject.getString("pack_code");
                        input.setPack_code(pack_code);
                    }
                    @Override
                    public String getKey(DwdInvTransaction input) {return null;}
                    public void join(DwdInvTransaction input, JSONObject dimInfo) throws Exception {}
                }, 60, TimeUnit.SECONDS);

        SingleOutputStreamOperator<String> calStream3 = AsyncDataStream.unorderedWait(calStream2,
                new DimAsyncFunction2<DwdInvTransaction>(
                        "DIM_MD_PACKAGE_DETAIL",
                        "WAREHOUSE_CODE,PACKAGE_CODE,RATIO,UOM"
                        ,config_env) {
                    @Override
                    public List<Tuple2<String, String>> getCondition(DwdInvTransaction input) {
                        if (StringUtils.isBlank(input.getPack_code())) return null;
                        List<Tuple2<String, String>> mdPackageDetailTuples = new ArrayList<>();
                        mdPackageDetailTuples.add(new Tuple2<>("warehouse_code",input.getWarehouse_code()));
                        mdPackageDetailTuples.add(new Tuple2<>("package_code",input.getPack_code()));
                        return mdPackageDetailTuples;
                    }
                    public void join(DwdInvTransaction input, List<JSONObject> dimInfo) throws Exception {
                        // 维度为空时特殊处理
                        if (CollectionUtil.isNullOrEmpty(dimInfo)) return;
                        // 转换系数
                        for (JSONObject jsonObject : dimInfo) {
                            DimMdPackageDetail dimMdPackageDetail = JSON.parseObject(jsonObject.toJSONString(), DimMdPackageDetail.class);
                            String ratio = dimMdPackageDetail.getRatio();
                            if ("PL".equals(dimMdPackageDetail.getUom())){
                                input.setPackage_ratio_pl(ratio);
                            }
                            if ("CS".equals(dimMdPackageDetail.getUom())) {
                                input.setPackage_ratio_cs(ratio);
                            }
                        }
                    }

                    @Override
                    public String getKey(DwdInvTransaction input) {return null;}
                    public void join(DwdInvTransaction input, JSONObject dimInfo) throws Exception {}
                }, 60, TimeUnit.SECONDS)
                .map(x -> {
                    BigDecimal inv_adjustment_qty = x.getInv_adjustment_qty();
                    // 箱
                    String package_ratio_cs = x.getPackage_ratio_cs();
                    if (StringUtils.isBlank(package_ratio_cs) || "0".equals(package_ratio_cs))
                        package_ratio_cs = "1";
                    x.setCsNum(inv_adjustment_qty.divide(new BigDecimal(package_ratio_cs), 5, BigDecimal.ROUND_HALF_DOWN));
                    // 托
                    String package_ratio_pl = x.getPackage_ratio_pl();
                    if (StringUtils.isBlank(package_ratio_pl) || "0".equals(package_ratio_pl))
                        package_ratio_pl = "1";
                    x.setPalletNum(inv_adjustment_qty.divide(new BigDecimal(package_ratio_pl), 5, BigDecimal.ROUND_HALF_DOWN));
                    return  x;
                })
                .map(JSONObject::toJSONString);
        calStream3.print();

        String sinkTopic = "ods_default";
        Properties outprop = new Properties();
        outprop.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, GmallConfig.KAFKA_SERVER);
        outprop.setProperty("transaction.timeout.ms", 60 * 5 * 1000 + "");
        FlinkKafkaProducer<String> myProducer = new FlinkKafkaProducer<String>(
                sinkTopic,
                new DwdKafkaSerializationSchema("dwd"),
                outprop,
                FlinkKafkaProducer.Semantic.AT_LEAST_ONCE); // 容错
        // sink到数据库
        calStream3.addSink(myProducer);

        env.execute(Thread.currentThread().getStackTrace()[1].getClassName());
    }

}
