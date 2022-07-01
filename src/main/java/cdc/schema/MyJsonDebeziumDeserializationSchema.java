package cdc.schema;

import com.alibaba.fastjson.JSONObject;
import com.ververica.cdc.debezium.DebeziumDeserializationSchema;
import com.vx.utils.DateTimeUtil;
import io.debezium.data.Envelope;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.util.Collector;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/*
 * @Description: TODO
 * @Author: xiehp
 * @Date: 2022/5/25 9:43
 * @Param
 * @return
 */
public class MyJsonDebeziumDeserializationSchema implements DebeziumDeserializationSchema<String> {

    // 自定义数据解析器
    @Override
    public void deserialize(SourceRecord sourceRecord, Collector<String> collector) throws Exception {

        // 创建JSON对象用于封装最终返回值数据信息
        JSONObject result = new JSONObject();
        //获取主键名称
        Struct key = (Struct)sourceRecord.key();
        String primaryKey = key.schema().fields().get(0).name();
        Envelope.Operation operation = Envelope.operationFor(sourceRecord);//before空的是因为插入 after是空的是因为删除 修改才是都不为空
        // 获取值信息并转换为Struct类型
        Struct value = (Struct) sourceRecord.value();
        // 时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        //获取source数据
        Struct source = value.getStruct("source");//
        if(source !=null) {
            Schema beforeSchema = source.schema();//字段
            List<Field> beforeFields = beforeSchema.fields();//存储在列表
            for (Field field : beforeFields) {  //field 就是 id  name    beforeValue就是id的值，name的值
                Object beforeValue = source.get(field);
                result.put(field.name(), beforeValue);
            }
        }
        // 获取“before”数据
        Struct before = value.getStruct("before");//
        JSONObject beforeJson = new JSONObject();
        if(before !=null) {
            Schema beforeSchema = before.schema();//字段
            List<Field> beforeFields = beforeSchema.fields();//存储在列表
            for (Field field : beforeFields) {  //field 就是 id  name    beforeValue就是id的值，name的值
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
                        String dateTime = sdf.format(new Date((times - 8 * 60 * 60 * 1000)));
                        beforeJson.put(field.name(), dateTime);
                    }
                } else if ("int64".equals(field.schema().type().getName()) && "io.debezium.time.ZonedTimestamp".equals(field.schema().name())) {
                    if (beforeValue != null) {
                        long times = (long) beforeValue;
                        String dateTime = sdf.format(new Date((times - 8 * 60 * 60 * 1000)));
                        beforeJson.put(field.name(), dateTime);
                    }
                }
                else if ("string".equals(field.schema().type().getName()) && "io.debezium.time.ZonedTimestamp".equals(field.schema().name())) {
                    if (beforeValue != null) {
                        long times = DateTimeUtil.utcToTs(String.valueOf(beforeValue));
                        String dateTime = sdf.format(new Date((times + 8 * 60 * 60 * 1000)));
                        beforeJson.put(field.name(), dateTime);
                    }
                }
                else if("int32".equals(field.schema().type().getName()) && "io.debezium.time.Date".equals(field.schema().name())){
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
        // 获取after数据
        Struct after = value.getStruct("after");
        JSONObject afterJson = new JSONObject();
        if(after !=null) {
            Schema afterSchema = after.schema();//字段
            List<Field> afterFields = afterSchema.fields();//存储在列表
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
                        String dateTime = sdf.format(new Date((times - 8 * 60 * 60 * 1000)));
                        afterJson.put(field.name(), dateTime);
                    }
                }
                else if ("int64".equals(field.schema().type().getName()) && "io.debezium.time.ZonedTimestamp".equals(field.schema().name())) {
                    if (afterValue != null) {
                        long times = (long) afterValue;
                        String dateTime = sdf.format(new Date((times - 8 * 60 * 60 * 1000)));
                        afterJson.put(field.name(), dateTime);
                    }
                }
                else if ("string".equals(field.schema().type().getName()) && "io.debezium.time.ZonedTimestamp".equals(field.schema().name())) {
                    if (afterValue != null) {
                        long times = DateTimeUtil.utcToTs(String.valueOf(afterValue));
                        String dateTime = sdf.format(new Date((times + 8 * 60 * 60 * 1000)));
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


        result.put("op", operation.toString().toLowerCase());
        result.put("before", beforeJson);
        result.put("after", afterJson);
        result.put("primaryKey", primaryKey);
        // 发送数据至下游
        collector.collect(result.toJSONString());
    }

    @Override
    public TypeInformation<String> getProducedType() {
        return TypeInformation.of(String.class);
    }

}