package com.vx.common;

public class GmallConfig {

    public static String KAFKA_SERVER = "shucang001:9092,shucang002:9092,shucang003:9092";
    //Phoenix库名
    public static final String HBASE_SCHEMA = "FLINK_REALTIME";

    //Phoenix驱动
    public static final String PHOENIX_DRIVER = "org.apache.phoenix.jdbc.PhoenixDriver";

    //Phoenix连接参数
    public static final String PHOENIX_SERVER = "jdbc:phoenix:shucang001,shucang002,shucang003:2181:/hbase";
    //public static final String PHOENIX_SERVER = "jdbc:phoenix:shucang001:2181";

    //ClickHouse驱动
    public static final String CLICKHOUSE_DRIVER = "ru.yandex.clickhouse.ClickHouseDriver";

    //ClickHouse连接地址
    public static final String CLICKHOUSE_URL = "jdbc:clickhouse://shucang001:8123/default";

    //MySQL驱动
    public static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    //MySQL连接地址
    public static final String MYSQL_URL = "jdbc:mysql://shucang003:16010/wms_szwt?serverTimezone=GMT%2B8&zeroDateTimeBehavior=convertToNull";
    public static final String MYSQL_USERNAME = "repl_finreport@vx-mysql-prod4-slave";
    public static final String MYSQL_PASSWORD = "Vs*xhabt3I";

//    public static final String SINK_MYSQL_URL = "jdbc:mysql://localhost:3306/test3?serverTimezone=GMT%2B8";
//    public static final String SINK_MYSQL_USERNAME = "root";
//    public static final String SINK_MYSQL_PASSWORD = "root";
    public static final String SINK_MYSQL_URL = "jdbc:mysql://shucang003:50010/test?serverTimezone=GMT%2B8";
    public static final String SINK_MYSQL_USERNAME = "root";
    public static final String SINK_MYSQL_PASSWORD = "123456";

}
