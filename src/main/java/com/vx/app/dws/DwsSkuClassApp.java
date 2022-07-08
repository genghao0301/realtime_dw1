package com.vx.app.dws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vx.app.func.SinkToSqlServer;
import com.vx.bean.SkuClass;
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
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.ArrayList;
import java.util.List;

/**
 * @version V1.0
 * @ClassName: DwdInbAsnApp
 * @Description: TODO
 * @Author: xiehp
 * @Date: 2022/5/24 16:18
 */
public class DwsSkuClassApp {

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
        env.setStateBackend(new FsStateBackend(String.format(GmallConfig.FS_STATE_BACKEND,sourceName)));

        // kafka消费者
        FlinkKafkaConsumer<String> consumer = MyKafkaUtil.getKafkaSource(GmallConfig.KAFKA_SERVER,"ods_md_code_dict",sourceName+env);
        // 输入流
        DataStream<String> edits = env.addSource(consumer).name("ods_md_code_dict");
        // 过滤清洗
        SingleOutputStreamOperator<SkuClass> calStream =
                edits
                        .filter(x -> {
                            JSONObject jsonObject = JSONObject.parseObject(x);
                            // 操作名称
                            String op = jsonObject.getString("op");
                            // 获取数据
                            JSONObject data = jsonObject.getJSONObject("after");
                            if ("delete".equals(op)){
                                data =  jsonObject.getJSONObject("before");
                            }
                            String type_code = data.getString("type_code");
                            String enabled = data.getString("enabled");
                            return "1".equals(enabled) && "ITEM_CLASS_CODE".equals(type_code);
                        })
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
                            SkuClass skuClass = JSON.parseObject(data.toJSONString(), SkuClass.class);
                            // 设置共同属性
                            skuClass.setDb(db);
                            skuClass.setTable(table);
                            skuClass.setOp(op);
                            skuClass.setPrimaryKey(primaryKey);
                            skuClass.setPrimaryKeyValue(String.valueOf(data.getLong(primaryKey)));
                            // 当删除或者更新时，设置where条件字段，用于删除或者更新
                            if ("delete".equals(op) || "update".equals(op)) {
                                List<Tuple2<String, Object>> whereSqls = new ArrayList<>();
                                whereSqls.add(new Tuple2<>("WH_CODE", DbEnum.getWhCodeEnumByDb(db)));
                                whereSqls.add(new Tuple2<>("CLASS_CODE", data.getString("code_value")));
                                skuClass.setWhereSqls(whereSqls);
                            }
                            // 设置实体信息
                            skuClass.setID(data.getLong(primaryKey));
                            skuClass.setWH_CODE(DbEnum.getWhCodeEnumByDb(db));
                            skuClass.setWMS_WH_CODE(data.getString("warehouse_code"));
                            skuClass.setCLASS_CODE(data.getString("code_value"));
                            skuClass.setCLASS_NAME(data.getString("code_name"));
                            skuClass.setCREATE_TIME(DateTimeUtil.toDate(data.getString("created_dtm_loc")));
                            skuClass.setUPDATE_TIME(DateTimeUtil.toDate(data.getString("updated_dtm_loc")));
                            return skuClass;
                        })
                        ;

        // 3 打印数据
        String isPrint = parameterTool.get("isPrint", "n");
        if ("y".equals(isPrint.toLowerCase())) calStream.print();
        // sink到数据库
        //calStream.addSink(SqlServerUtil.getSink(SqlServerUtil.genInsertSql(SkuClass.class))).name("md_code_dict_sqlserver");
        calStream.addSink(new SinkToSqlServer(config_env)).name("md_code_dict_sqlserver");

        env.execute(sourceName);
    }

}
