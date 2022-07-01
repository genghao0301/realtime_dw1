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
public class MyKafkaSerializationSchema implements KafkaSerializationSchema<String> {

    private String prefix;

    public MyKafkaSerializationSchema() {

    }

    public MyKafkaSerializationSchema(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public ProducerRecord<byte[], byte[]> serialize(String element, @Nullable Long timestamp) {
        JSONObject jsonObject = JSONObject.parseObject(element);
        String topic = "ods_default";
        if (StringUtils.isNotBlank(prefix)) topic = topic.replace("ods",prefix);
        // key值设置
        String primaryKey = jsonObject.getString("db") + "-" + jsonObject.getString("table");
        if (jsonObject.containsKey("after")) {
            try {
                primaryKey = primaryKey + "-"+ jsonObject.getJSONObject("after").getString(jsonObject.getString("primaryKey"));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(jsonObject.toJSONString());
            }
        }
        // 设置主题topic
        if (StringUtils.isNotBlank(prefix)) {
            topic = prefix + "_"+jsonObject.getString("table");
        }
        return new ProducerRecord<>(topic,primaryKey.getBytes(), element.getBytes());
    }

}