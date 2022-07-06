package com.vx.app.dwd;

import cdc.vx.bean.DimMdPackageDetail;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vx.app.func.DimAsyncFunction2;
import com.vx.app.schema.DwdKafkaSerializationSchema;
import com.vx.bean.DwdInvTransaction;
import com.vx.bean.SkuClass;
import com.vx.common.GmallConfig;
import com.vx.utils.DateTimeUtil;
import com.vx.utils.DbEnum;
import com.vx.utils.MyKafkaUtil;
import com.vx.utils.SqlServerUtil;
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
public class DwdMdCodeDictApp {

    public static void main(String[] args) throws Exception {

        String[] classNames = Thread.currentThread().getStackTrace()[1].getClassName().split(",");
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
                            String wms_wh_code = data.getString("warehouse_code");
                            String class_code = data.getString("code_value");
                            String class_name = data.getString("code_name");
                            String create_time = data.getString("created_dtm_loc");
                            String update_time = data.getString("updated_dtm_loc");

                            skuClass.setID(data.getLong(primaryKey));
                            skuClass.setWH_CODE(DbEnum.getWhCodeEnumByDb(db));
                            skuClass.setWMS_WH_CODE(wms_wh_code);
                            skuClass.setCLASS_CODE(class_code);
                            skuClass.setCLASS_NAME(class_name);
                            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>create_time:"+create_time+":"+data.getLong(primaryKey));
                            skuClass.setCREATE_TIME(DateTimeUtil.toDate(create_time));
                            skuClass.setUPDATE_TIME(DateTimeUtil.toDate(update_time));
                            return skuClass;
                        })
                        ;


        // 3 打印数据
        String isPrint = parameterTool.get("isPrint", "n");
        if ("y".equals(isPrint.toLowerCase())) calStream.print();
        // sink到数据库
        calStream.addSink(SqlServerUtil.getSink(SqlServerUtil.genInsertSql(SkuClass.class))).name("md_code_dict_sqlserver");

        env.execute(sourceName);
    }

}
