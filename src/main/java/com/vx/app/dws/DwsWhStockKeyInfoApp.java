package com.vx.app.dws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vx.app.func.DimAsyncFunction2;
import com.vx.bean.SkuClass;
import com.vx.bean.WhStockKeyInfo;
import com.vx.common.GmallConfig;
import com.vx.utils.DateTimeUtil;
import com.vx.utils.DbEnum;
import com.vx.utils.MyKafkaUtil;
import com.vx.utils.SqlServerUtil;
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
import org.apache.flink.util.CollectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @version V1.0
 * @ClassName: DwdInbAsnApp
 * @Description: TODO
 * @Author: xiehp
 * @Date: 2022/5/24 16:18
 */
public class DwsWhStockKeyInfoApp {

    public static void main(String[] args) throws Exception {

        String[] classNames = Thread.currentThread().getStackTrace()[1].getClassName().split(",");
        String sourceName = classNames[classNames.length -1];

        //获取执行参数
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        // ***************************初始化配置信息***************************
        String config_env = parameterTool.get("env", "dev");
        GmallConfig.getSingleton().init(config_env);
        System.out.println("==============================PHOENIX_SERVER:" + GmallConfig.PHOENIX_SERVER);
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
        env.setStateBackend(new FsStateBackend(String.format(GmallConfig.FS_STATE_BACKEND,sourceName)));

        //消费主题
        String topic = "ods_biz_house_info";
        // kafka消费者
        FlinkKafkaConsumer<String> consumer = MyKafkaUtil.getKafkaSource(GmallConfig.KAFKA_SERVER,topic,sourceName+env);
        // 输入流
        DataStream<String> edits = env.addSource(consumer).name(topic);
        // 3 打印数据
        String isPrint = parameterTool.get("isPrint", "n");
        if ("y".equals(isPrint.toLowerCase())) edits.print();
        // 过滤清洗
        SingleOutputStreamOperator<WhStockKeyInfo> houseInfoStream =
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
                            WhStockKeyInfo whStockKeyInfo = JSON.parseObject(data.toJSONString(), WhStockKeyInfo.class);
                            String siteCode = data.getString("SITE_CODE");
                            String houseCode = data.getString("HOUSE_CODE");
                            String houseName = data.getString("HOUSE_NAME");
                            String temperateZone = data.getString("TEMPERATE_ZONE");
                            String temperature = data.getString("TEMPERATURE");
                            String createTime = data.getString("CREATE_TIME");
                            String updateTime = data.getString("UPDATE_TIME");
                            whStockKeyInfo.setID(data.getLong(primaryKey));
                            whStockKeyInfo.setWH_CODE(siteCode);
                            whStockKeyInfo.setWH_AREA_CODE(houseCode);
                            whStockKeyInfo.setWH_AREA_NAME(houseName);
                            whStockKeyInfo.setTEMPERATURE_ZONE(temperateZone);
                            whStockKeyInfo.setAREA_TEMPERATURE_INFO(temperature);
                            whStockKeyInfo.setCREATE_TIME(DateTimeUtil.toDate(createTime));
                            whStockKeyInfo.setUPDATE_TIME(DateTimeUtil.toDate(updateTime));
                            return whStockKeyInfo;
                        })
                        ;
        SingleOutputStreamOperator<WhStockKeyInfo> houseInfoStream2 = AsyncDataStream.unorderedWait(houseInfoStream,
                new DimAsyncFunction2<WhStockKeyInfo>("DIM_WAREHOUSE_CODE_MAPPING",
                        "SITE_CODE,WAREHOUSE_CODE,SITE_COS_NAME",config_env) {
                    public String getKey(WhStockKeyInfo input) {return null;}
                    public void join(WhStockKeyInfo input, JSONObject dimInfo) throws Exception {}

                    @Override
                    public List<Tuple2<String, String>> getCondition(WhStockKeyInfo input) {
                        List<Tuple2<String, String>> mdSkuTuples = new ArrayList<>();
                        mdSkuTuples.add(new Tuple2<>("SITE_CODE",input.getWH_CODE()));
                        return mdSkuTuples;
                    }

                    @Override
                    public void join(WhStockKeyInfo input, List<JSONObject> dimInfo) throws Exception {
                        if (CollectionUtil.isNullOrEmpty(dimInfo)) return;
                        JSONObject jsonObject = dimInfo.get(0);
                        String warehouse_code = jsonObject.getString("warehouse_code");
                        String site_cos_name = jsonObject.getString("site_cos_name");
                        input.setWMS_WH_CODE(warehouse_code);
                        input.setWH_NAME(site_cos_name);
                    }
                }, 60, TimeUnit.SECONDS);

        // sink到数据库
        houseInfoStream2.addSink(SqlServerUtil.getSink(SqlServerUtil.genInsertSql(WhStockKeyInfo.class))).name("biz_house_info_sqlserver");

        env.execute(sourceName);
    }

}
