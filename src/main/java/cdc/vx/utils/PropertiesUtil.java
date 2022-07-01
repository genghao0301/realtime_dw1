package cdc.vx.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

/**
 * @version V1.0
 * @ClassName: PropertiesUtil
 * @Description: TODO
 * @Author: xiehp
 * @Date: 2022/5/18 16:38
 */
public class PropertiesUtil {

    public static Properties getDebeziumProperties (){
        // 设置debezium参数
        Properties properties = new Properties();
        properties.setProperty("snapshot.locking.mode", "none");
        properties.setProperty("bigint.unsigned.handling.mode","long");
        properties.setProperty("decimal.handling.mode","double");
        // 时区设置
//        properties.put("converters", "datetime");
//        properties.put("datetime.type", "cdc.converter.MySqlDateTimeConverter");
//        properties.put("datetime.format.date", "yyyy-MM-dd");
//        properties.put("datetime.format.time", "HH:mm:ss");
//        properties.put("datetime.format.datetime", "yyyy-MM-dd HH:mm:ss");
//        properties.put("datetime.format.timestamp", "yyyy-MM-dd HH:mm:ss");
//        properties.put("datetime.format.timestamp.zone", "Asia/Shanghai");

//        properties.setProperty("converters", "dateConverters");
//        properties.setProperty("dateConverters.type", "cdc.converter.MySqlDateTimeConverter");
//        properties.setProperty("dateConverters.format.date", "yyyy-MM-dd");
//        properties.setProperty("dateConverters.format.time", "HH:mm:ss");
//        properties.setProperty("dateConverters.format.datetime", "yyyy-MM-dd HH:mm:ss");
//        properties.setProperty("dateConverters.format.timestamp", "yyyy-MM-dd HH:mm:ss");
//        properties.setProperty("dateConverters.format.timestamp.zone", "UTC+8");

//        properties.setProperty("binary.handling.mode","base64");
//        properties.setProperty("producer.max.request.size","16777216");
//        properties.setProperty("database.history.producer.max.request.size","16777216");
//        properties.setProperty("inconsistent.schema.handling.mode","warn");
//        properties.setProperty("database.history.skip.unparseable.ddl","true");
//        properties.setProperty("database.history.store.only.monitored.tables.ddl ","false");
        return properties;
    }

    public static Properties getJdbcProperties (){
        // 设置debezium参数
        Properties properties = new Properties();
        properties.setProperty("useUnicode", "true");
        properties.setProperty("characterEncoding", "utf8");
        properties.setProperty("zeroDateTimeBehavior","convertToNull");
        properties.setProperty("useSSL","false");
        properties.setProperty("serverTimezone","GMT%2B8");
        // 时区设置
//        properties.setProperty("converters", "datetime");
//        properties.setProperty("datetime.type", "cdc.converter.MySqlDateTimeConverter");
//        properties.setProperty("datetime.format.date", "yyyy-MM-dd");
//        properties.setProperty("datetime.format.time", "HH:mm:ss");
//        properties.setProperty("datetime.format.datetime", "yyyy-MM-dd HH:mm:ss");
//        properties.setProperty("datetime.format.timestamp", "yyyy-MM-dd HH:mm:ss");
//        properties.setProperty("datetime.format.timestamp.zone", "Asia/Shanghai");

//        properties.setProperty("converters", "dateConverters");
//        properties.setProperty("dateConverters.type", "cdc.converter.MySqlDateTimeConverter");
//        properties.setProperty("dateConverters.format.date", "yyyy-MM-dd");
//        properties.setProperty("dateConverters.format.time", "HH:mm:ss");
//        properties.setProperty("dateConverters.format.datetime", "yyyy-MM-dd HH:mm:ss");
//        properties.setProperty("dateConverters.format.timestamp", "yyyy-MM-dd HH:mm:ss");
//        properties.setProperty("dateConverters.format.timestamp.zone", "UTC+8");

//        properties.setProperty("binary.handling.mode","base64");
//        properties.setProperty("producer.max.request.size","16777216");
//        properties.setProperty("database.history.producer.max.request.size","16777216");
//        properties.setProperty("inconsistent.schema.handling.mode","warn");
//        properties.setProperty("database.history.skip.unparseable.ddl","true");
//        properties.setProperty("database.history.store.only.monitored.tables.ddl ","false");
        return properties;
    }

    public static String getClassName (String source) {
        return getClassName(source, null);
    }

    public static String getClassName (String source, String replace) {
        if (StringUtils.isBlank(source))
            return "";
        StringBuffer sb = new StringBuffer();
        String regexStr = "_";
        if (StringUtils.isNotBlank(replace))
            source = source.replaceFirst(replace, "");
        String[] sources = source.split("_");
        for (int i=0; i<sources.length; i++) {
            String a=sources[i].substring(0, 1).toUpperCase();
            String b=sources[i].substring(1).toLowerCase();
            sb.append(a+b);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(getClassName("invtransaction",""));
    }
}
