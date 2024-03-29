package com.vx.app.dws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vx.bean.OrderWide;
import com.vx.bean.PaymentWide;
import com.vx.bean.ProductStats;
import com.vx.common.GmallConfig;
import com.vx.common.GmallConstant;
import com.vx.utils.DateTimeUtil;
import com.vx.utils.MyKafkaUtil;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.shaded.netty4.io.netty.handler.codec.json.JsonObjectDecoder;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.CollectionUtil;
import org.apache.flink.util.Collector;

import java.lang.annotation.ElementType;
import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;

/**
 * @version V1.0
 * @ClassName: ProductStatsApp
 * @Description: TODO
 * @Author: xiehp
 * @Date: 2022/5/7 19:38
 */
public class ProductStatsApp {

    public static void main(String[] args) {
        //1.获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //1.1 设置状态后端
        env.setStateBackend(new FsStateBackend(String.format(GmallConfig.FS_STATE_BACKEND,"dwd-log")));
        //1.2 开启CK
        env.enableCheckpointing(10000L, CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setCheckpointTimeout(60000L);

        //2.读取kafka数据 7个主题
        String groupId = "product_stats_app1";
        String pageViewSourceTopic = "dwd_page_log";
        String favorInfoSourceTopic = "dwd_favor_info";
        String cartInfoSourceTopic = "dwd_cart_info";
        String orderWideSourceTopic = "dwm_order_wide";
        String paymentWideSourceTopic = "dwm_payment_wide";
        String refundInfoSourceTopic = "dwd_order_refund_info";
        String commentInfoSourceTopic = "dwd_comment_info";

        FlinkKafkaConsumer<String> pageViewSource = MyKafkaUtil.getKafkaSource(pageViewSourceTopic, groupId);
        DataStreamSource<String> pageViewDStream = env.addSource(pageViewSource);

        FlinkKafkaConsumer<String> favorInfoSourceSource = MyKafkaUtil.getKafkaSource(favorInfoSourceTopic, groupId);
        DataStreamSource<String> favorInfoDStream = env.addSource(favorInfoSourceSource);

        FlinkKafkaConsumer<String> cartInfoSource = MyKafkaUtil.getKafkaSource(cartInfoSourceTopic, groupId);
        DataStreamSource<String> cartInfoDStream = env.addSource(cartInfoSource);

        FlinkKafkaConsumer<String> orderWideSource = MyKafkaUtil.getKafkaSource(orderWideSourceTopic, groupId);
        DataStreamSource<String> orderWideDStream = env.addSource(orderWideSource);

        FlinkKafkaConsumer<String> paymentWideSource = MyKafkaUtil.getKafkaSource(paymentWideSourceTopic, groupId);
        DataStreamSource<String> paymentWideDStream = env.addSource(paymentWideSource);

        FlinkKafkaConsumer<String> refundInfoSource = MyKafkaUtil.getKafkaSource(refundInfoSourceTopic, groupId);
        DataStreamSource<String> refundInfoDStream = env.addSource(refundInfoSource);

        FlinkKafkaConsumer<String> commentInfoSource = MyKafkaUtil.getKafkaSource(commentInfoSourceTopic, groupId);
        DataStreamSource<String> commentInfoDStream = env.addSource(commentInfoSource);

        //3.将7个流转换为统一数据格式
        //3.1 处理点击和曝光数据
        SingleOutputStreamOperator<ProductStats> clickAndDisplayDS = pageViewDStream.process(new ProcessFunction<String, ProductStats>() {
            @Override
            public void processElement(String pageLog, Context ctx, Collector<ProductStats> out) throws Exception {
                //转换为JSON对象
                JSONObject jsonObject = JSON.parseObject(pageLog);
                //获取PageId
                JSONObject pageObj = jsonObject.getJSONObject("page");
                String page_id = pageObj.getString("page_id");
                //获取时间字段
                Long ts = jsonObject.getLong("ts");
                // 判断是good_detail页面，则为点击数据
                if ("good_detail".equals(page_id)) {
                    ProductStats productStats = ProductStats
                            .builder()
                            .sku_id(pageObj.getLong("item"))
                            .click_ct(1L)
                            .ts(ts)
                            .build();
                    out.collect(productStats);
                }

                //获取曝光数据
                JSONArray displays = jsonObject.getJSONArray("displays");
                if (CollectionUtil.isNullOrEmpty(displays)) {
                    for (int i=0; i<displays.size(); i++) {
                        JSONObject displayObj = displays.getJSONObject(i);
                        if ("sku_id".equals(displayObj.getString("item_type"))){
                            ProductStats productStats = ProductStats.builder()
                                    .sku_id(displayObj.getLong("item"))
                                    .display_ct(1l)
                                    .ts(ts)
                                    .build();
                            out.collect(productStats);
                        }
                    }
                }
            }
        });

        //3.2 处理收藏数据
        SingleOutputStreamOperator<ProductStats> favorStatsDS = favorInfoDStream.map(
                json -> {
                    JSONObject favorInfo = JSON.parseObject(json);
                    Long ts = DateTimeUtil.toTs(favorInfo.getString("create_time"));
                    return ProductStats.builder()
                            .sku_id(favorInfo.getLong("sku_id"))
                            .favor_ct(1L)
                            .ts(ts)
                            .build();
                });

        //3.3 处理收藏数据
        SingleOutputStreamOperator<ProductStats> cartStatsDS = cartInfoDStream.map(
                json -> {
                    JSONObject cartInfo = JSON.parseObject(json);
                    Long ts = DateTimeUtil.toTs(cartInfo.getString("crate_time"));
                    return  ProductStats.builder()
                            .sku_id(cartInfo.getLong("sku_id"))
                            .cart_ct(1L)
                            .ts(ts)
                            .build();
                });

        //3.4 处理下单数据
        SingleOutputStreamOperator<ProductStats> orderDS = orderWideDStream.map(
                json -> {
                    //转换为OrderWide对象
                    OrderWide orderWide = JSON.parseObject(json, OrderWide.class);
                    //获取时间戳字段
                    Long ts = DateTimeUtil.toTs(orderWide.getCreate_time());
                    return ProductStats.builder()
                            .sku_id(orderWide.getSku_id())
                            .order_amount(orderWide.getTotal_amount())
                            .order_sku_num(orderWide.getSku_num())
                            .orderIdSet(new HashSet<>(Collections.singleton(orderWide.getOrder_id())))
                            .ts(ts)
                            .build();
                });

        //3.5 处理支付数据
        SingleOutputStreamOperator<ProductStats> paymentStatsDS = paymentWideDStream.map(
                json -> {
                    PaymentWide paymentWide = JSON.parseObject(json, PaymentWide.class);
                    Long ts = DateTimeUtil.toTs(paymentWide.getPayment_create_time());
                    return ProductStats.builder()
                            .sku_id(paymentWide.getSku_id())
                            .payment_amount(paymentWide.getSplit_total_amount())
                            .paidOrderIdSet(new HashSet<>(Collections.singleton(paymentWide.getOrder_id())))
                            .ts(ts)
                            .build();

                });

        //3.6 处理退单数据
        SingleOutputStreamOperator<ProductStats> refundStateDS = refundInfoDStream.map(
            json -> {
                //将数据转换为JSON对象
                JSONObject refundJsonObj = JSON.parseObject(json);
                Long ts = DateTimeUtil.toTs(refundJsonObj.getString("create_time"));
                return ProductStats.builder()
                        .sku_id(refundJsonObj.getLong("sku_id"))
                        .refund_amount(refundJsonObj.getBigDecimal("refund_amount"))
                        .ts(ts)
                        .build();
            });

        //3.7 处理评价数据
        SingleOutputStreamOperator<ProductStats> appraiseDS = commentInfoDStream.map(new MapFunction<String, ProductStats>() {
            @Override
            public ProductStats map(String value) throws Exception {
                //将数据转换为JSON对象
                JSONObject jsonObject = JSON.parseObject(value);
                Long ts = DateTimeUtil.toTs(jsonObject.getString("create_time"));
                //处理好评数
                Long goodCommentCt = 0L;
                if (GmallConstant.APPRAISE_GOOD.equals(jsonObject.getString("appraise")))
                    goodCommentCt = 1l;
                return ProductStats.builder()
                        .sku_id(jsonObject.getLong("sku_id"))
                        .comment_ct(goodCommentCt)
                        .ts(ts)
                        .build();
            }
        });

        //4.Union
        DataStream unionDS = clickAndDisplayDS.union(favorStatsDS,
                cartStatsDS,
                orderDS,
                paymentStatsDS,
                refundStateDS,
                appraiseDS);

        //5.设置Watermark
        SingleOutputStreamOperator<ProductStats> productStatsWithWaterMarkDS = unionDS.assignTimestampsAndWatermarks(WatermarkStrategy.<ProductStats>forBoundedOutOfOrderness(Duration.ofSeconds(10L)).withTimestampAssigner(new SerializableTimestampAssigner<ProductStats>() {
            @Override
            public long extractTimestamp(ProductStats productStats, long l) {
                return productStats.getTs();
            }
        }));

        //6.分组、开窗、聚合
        SingleOutputStreamOperator<ProductStats> reduceDS = productStatsWithWaterMarkDS.keyBy(ProductStats::getSku_id)
                .window(TumblingEventTimeWindows.of(Time.seconds(2)))
                .reduce(new ReduceFunction<ProductStats>() {
                    @Override
                    public ProductStats reduce(ProductStats stats1, ProductStats stats2) throws Exception {

                        return null;
                    }
                }, new WindowFunction<ProductStats, ProductStats, Long, TimeWindow>() {
                    @Override
                    public void apply(Long aLong, TimeWindow window, Iterable<ProductStats> input, Collector<ProductStats> out) throws Exception {

                    }
                });

    }

}
