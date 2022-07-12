package com.vx.app.dws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vx.app.func.SinkToSqlServer;
import com.vx.bean.InOrderDetail;
import com.vx.common.GmallConfig;
import com.vx.utils.DateTimeUtil;
import com.vx.utils.MyKafkaUtil;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

public class DwsInoOderDetailApp2 {
    public static void main(String[] args) throws Exception {
        //获取执行参数
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        // ***************************初始化配置信息***************************
        String config_env = parameterTool.get("env", "dev");
        GmallConfig.getSingleton().init(config_env);
        // ***************************初始化配置信息***************************
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
        DecimalFormat format = new DecimalFormat();
        format.setParseBigDecimal(true);

        //构建收货表流
        FlinkKafkaConsumer<String> invtConsumer = MyKafkaUtil.getKafkaSource("dwd_inv_transaction", Thread.currentThread().getStackTrace()[1].getClassName());
        SingleOutputStreamOperator<String> invtDS = env.addSource(invtConsumer).name("dwd_inv_transaction");

        //转换成实体
        SingleOutputStreamOperator<InOrderDetail> map = invtDS.map(x -> {
            JSONObject jsonObject = JSONObject.parseObject(x);
            String wms_wh_code = jsonObject.getString("warehouse_code");
            String sku_code = jsonObject.getString("item_code");
            String pallet_code = jsonObject.getString("lpn_no");
            String class_code = jsonObject.getString("item_class_code");
            String asn_status = jsonObject.getString("status");
            String temperature = jsonObject.getString("arrival_temperature");
            String asn_ea1 = jsonObject.getString("inv_adjustment_qty");
            BigDecimal asn_ea = new BigDecimal(asn_ea1);
            BigDecimal weight = (BigDecimal) format.parse(jsonObject.getString("total_weight"));
            BigDecimal gross_weight = (BigDecimal) format.parse(jsonObject.getString("total_gross_weight"));
            BigDecimal net_weight = (BigDecimal) format.parse(jsonObject.getString("total_net_weight"));
            String create_time = jsonObject.getString("created_dtm_loc");
            String update_time = jsonObject.getString("updated_dtm_loc");
            InOrderDetail inOrderDetail = JSON.parseObject(jsonObject.toJSONString(), InOrderDetail.class);
            // 设置共同属性
//            skuClass.setDb(db);
//            skuClass.setTable(table);
//            skuClass.setOp(op);
//            skuClass.setPrimaryKey(primaryKey);
//            skuClass.setPrimaryKeyValue(String.valueOf(data.getLong(primaryKey)));

            inOrderDetail.setWMS_WH_CODE(wms_wh_code);
            inOrderDetail.setSKU_CODE(sku_code);
            inOrderDetail.setPALLET_CODE(pallet_code);
            inOrderDetail.setCLASS_CODE(class_code);
            inOrderDetail.setASN_STATUS(asn_status);
            inOrderDetail.setTEMPERATURE(temperature);
            inOrderDetail.setASN_EA(asn_ea);
            inOrderDetail.setWEIGHT(weight);
            inOrderDetail.setGROSS_WEIGHT(gross_weight);
            inOrderDetail.setNET_WEIGHT(net_weight);
            inOrderDetail.setCREATE_TIME(DateTimeUtil.toDate(create_time));
            inOrderDetail.setUPDATE_TIME(DateTimeUtil.toDate(update_time));
            inOrderDetail.setETL_TIME(new Date());
            return inOrderDetail;
        });
        map.print("测试>>>>");

        //sink到数据库
        map.addSink(new SinkToSqlServer(config_env)).name("in_ooder_detail_app");

        //启动任务
        env.execute(Thread.currentThread().getStackTrace()[1].getClassName());

    }
}
