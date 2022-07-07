package cdc.schema;

/**
 * @version V1.0
 * @ClassName: PgCustomerDeserialization
 * @Description: TODO
 * @Author: xiehp
 * @Date: 2022/5/25 9:15
 */
import com.alibaba.fastjson.JSONObject;
import com.ververica.cdc.debezium.DebeziumDeserializationSchema;
import io.debezium.data.Envelope;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.util.Collector;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class PgCustomerDeserialization implements DebeziumDeserializationSchema<String> {

    ZoneId serverTimeZone;

    @Override
    public void deserialize(SourceRecord sourceRecord, Collector<String> collector) throws Exception {

        //1.创建JSON对象用于存储最终数据
        JSONObject result = new JSONObject();
        Struct value = (Struct) sourceRecord.value();
        //2.获取库名&表名
        Struct sourceStruct = value.getStruct("source");
        String database = sourceStruct.getString("db");
        //String schema = sourceStruct.getString("schema");
        String tableName = sourceStruct.getString("table");
        //3.获取"before"数据
        Struct before = value.getStruct("before");
        JSONObject beforeJson = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        if (before != null) {
            Schema beforeSchema = before.schema();
            List<Field> beforeFields = beforeSchema.fields();
            for (Field field : beforeFields) {
                Object beforeValue = before.get(field);
                if ("int64".equals(field.schema().type().getName()) && "io.debezium.time.MicroTimestamp".equals(field.schema().name())) {
                    if (beforeValue != null) {
                        long times = (long) beforeValue / 1000;
                        String dateTime = sdf.format(new Date((times - 8 * 60 * 60 * 1000)));
                        beforeJson.put(field.name(), dateTime);
                    }
                }
                else if ("int64".equals(field.schema().type().getName()) && "io.debezium.time.NanoTimestamp".equals(field.schema().name())) {
                    if (beforeValue != null) {
                        long times = (long) beforeValue;
                        String dateTime = sdf.format(new Date((times - 8 * 60 * 60 * 1000)));
                        beforeJson.put(field.name(), dateTime);
                    }
                }  else if ("int64".equals(field.schema().type().getName()) && "io.debezium.time.Timestamp".equals(field.schema().name())) {
                    if (beforeValue != null) {
                        long times = (long) beforeValue;
                        String dateTime = sdf.format(new Date((times - 8 * 60 * 60 )));
                        beforeJson.put(field.name(), dateTime);
                    }
                } else if("int32".equals(field.schema().type().getName()) && "io.debezium.time.Date".equals(field.schema().name())){
                    if(beforeValue != null) {
                        int times = (int) beforeValue;
                        String dateTime = sdf1.format(new Date(times * 24 * 60 * 60L * 1000));
                        beforeJson.put(field.name(), dateTime);
                    }
                }
                else {
                    beforeJson.put(field.name(), beforeValue);
                }
            }
        }

        //4.获取"after"数据
        Struct after = value.getStruct("after");
        JSONObject afterJson = new JSONObject();
        if (after != null) {
            Schema afterSchema = after.schema();
            List<Field> afterFields = afterSchema.fields();
            for (Field field : afterFields) {
                Object afterValue = after.get(field);
                if ("int64".equals(field.schema().type().getName()) && "io.debezium.time.MicroTimestamp".equals(field.schema().name())) {
                    if (afterValue != null) {
                        long times = (long) afterValue / 1000;
                        String dateTime = sdf.format(new Date((times - 8 * 60 * 60 * 1000)));
                        afterJson.put(field.name(), dateTime);
                    }
                }
                else if ("int64".equals(field.schema().type().getName()) && "io.debezium.time.NanoTimestamp".equals(field.schema().name())) {
                    if (afterValue != null) {
                        long times = (long) afterValue;
                        String dateTime = sdf.format(new Date((times - 8 * 60 * 60 * 1000)));
                        afterJson.put(field.name(), dateTime);
                    }
                }  else if ("int64".equals(field.schema().type().getName()) && "io.debezium.time.Timestamp".equals(field.schema().name())) {
                    if (afterValue != null) {
                        long times = (long) afterValue;
                        String dateTime = sdf.format(new Date((times - 8 * 60 * 60)));
                        afterJson.put(field.name(), dateTime);
                    }
                }
                else if("int32".equals(field.schema().type().getName()) && "io.debezium.time.Date".equals(field.schema().name())){
                    if(afterValue != null) {
                        int times = (int) afterValue;
                        String dateTime = sdf1.format(new Date(times * 24 * 60 * 60L * 1000));
                        afterJson.put(field.name(), dateTime);
                    }
                }
                else {
                    afterJson.put(field.name(), afterValue);
                }
            }
        }

        //5.获取操作类型  CREATE UPDATE DELETE
        Envelope.Operation operation = Envelope.operationFor(sourceRecord);
        String type = operation.toString().toLowerCase();
        //6.将字段写入JSON对象
        result.put("database", database);
        //result.put("schema", schema);
        result.put("tableName", tableName);
        result.put("before", beforeJson);
        result.put("after", afterJson);
        result.put("type", type);

        //7.输出数据
        collector.collect(result.toJSONString());
    }

    @Override
    public TypeInformation<String> getProducedType() {
        return BasicTypeInfo.STRING_TYPE_INFO;
    }

}
