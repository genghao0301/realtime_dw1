package cdc.schema;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.annotation.Nullable;

/**
 * @version V1.0
 * @ClassName: MyKafkaSerializationSchema
 * @Description: TODO
 * @Author: xiehp
 * @Date: 2022/2/24 11:12
 */
public class MyKafkaSerializationSchema2 implements KafkaSerializationSchema<String> {

    private String primaryKey;

    public MyKafkaSerializationSchema2() {

    }

    public MyKafkaSerializationSchema2(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Override
    public ProducerRecord<byte[], byte[]> serialize(String element, @Nullable Long timestamp) {
        JSONObject jsonObject = JSONObject.parseObject(element);
        String topic = "ods_"+jsonObject.getString("table");
        String primaryKey2 = topic;
        // 主题
        if (StringUtils.isNotBlank(primaryKey)) {
            primaryKey2 = jsonObject.getString("db") + "-" + jsonObject.getString("table") + jsonObject.getString(primaryKey);
        } else {
            primaryKey = primaryKey + "-"+ jsonObject.getJSONObject("after").getString(jsonObject.getString("primaryKey"));
        }
        return new ProducerRecord<>(topic,primaryKey2.getBytes(), element.getBytes());
    }
}