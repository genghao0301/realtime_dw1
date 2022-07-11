package com.vx.app.schema;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

/**
 * @version V1.0
 * @ClassName: MyKafkaSerializationSchema
 * @Description: TODO
 * @Author: xiehp
 * @Date: 2022/2/24 11:12
 */
public class DwdKafkaSerializationSchema implements KafkaSerializationSchema<String> {

    private String prefix;

    public DwdKafkaSerializationSchema() {

    }

    public DwdKafkaSerializationSchema(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public ProducerRecord<byte[], byte[]> serialize(String element, @Nullable Long timestamp) {
        JSONObject jsonObject = JSONObject.parseObject(element);
        String topic = "dwd_default";
        // key值设置
        String primaryKey = jsonObject.getString("db") + "-" + jsonObject.getString("table");
        try {
            primaryKey = primaryKey + "-"+ jsonObject.getString(jsonObject.getString("primaryKey"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(jsonObject.toJSONString());
        }
        // 设置主题topic
        if (StringUtils.isNotBlank(prefix)) {
            topic = prefix + "_"+jsonObject.getString("table");
        }
        return new ProducerRecord<>(topic,primaryKey.getBytes(), element.getBytes());
    }

}