package com.vx.common;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class GmallConfig {

    private static List<String> envs = Arrays.asList("dev", "uat","prod");
    public static Properties properties = null;

    private GmallConfig() {}
    public static GmallConfig getSingleton() {
        return Inner.instance;
    }
    private static class Inner {
        private static final GmallConfig instance = new GmallConfig();
    }

    // kafka服务器信息
    public static String KAFKA_SERVER = "shucang001:9092,shucang002:9092,shucang003:9092";
    // kafka服务器信息
    public static String FLINK_WM4_STATE_BACKEND = null;

    //Phoenix库名
    public static String HBASE_SCHEMA = "FLINK_REALTIME";
    //Phoenix驱动
    public static String PHOENIX_DRIVER = "org.apache.phoenix.jdbc.PhoenixDriver";
    //Phoenix连接参数
    public static String PHOENIX_SERVER = "jdbc:phoenix:shucang001,shucang002,shucang003:2181:/hbase"   ;

    //ClickHouse驱动
    public static String CLICKHOUSE_DRIVER = "ru.yandex.clickhouse.ClickHouseDriver";
    //ClickHouse连接地址
    public static String CLICKHOUSE_URL = "jdbc:clickhouse://shucang001:8123/default";

    // 状态后端
    public static String FS_STATE_BACKEND = null;
    //MySQL驱动
    public static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    //wms 需要同步的表
    public static String WMS_CDC_TABLES = null;
    //WMS2 MySQL 源数据库连接信息
    public static String WMS2_MYSQL_HOSTNAME = null;
    public static String WMS2_MYSQL_USERNAME = null;
    public static String WMS2_MYSQL_PASSWORD = null;
    public static Integer WMS2_MYSQL_PORT = null;
    public static String WM2_STATE_BACKEND = null;
    public static String WM2_HBASE_STATE_BACKEND = null;
    //WMS3 MySQL 源数据库连接信息
    public static String WMS3_MYSQL_HOSTNAME = null;
    public static String WMS3_MYSQL_USERNAME = null;
    public static String WMS3_MYSQL_PASSWORD = null;
    public static Integer WMS3_MYSQL_PORT = null;
    public static String WM3_STATE_BACKEND = null;
    public static String WM3_HBASE_STATE_BACKEND = null;
    //WMS4 MySQL 源数据库连接信息
    public static String WMS4_MYSQL_HOSTNAME = null;
    public static String WMS4_MYSQL_USERNAME = null;
    public static String WMS4_MYSQL_PASSWORD = null;
    public static Integer WMS4_MYSQL_PORT = null;
    public static String WM4_STATE_BACKEND = null;
    public static String WM4_HBASE_STATE_BACKEND = null;
    //WMS5 MySQL 源数据库连接信息
    public static String WMS5_MYSQL_HOSTNAME = null;
    public static String WMS5_MYSQL_USERNAME = null;
    public static String WMS5_MYSQL_PASSWORD = null;
    public static Integer WMS5_MYSQL_PORT = null;
    public static String WM5_STATE_BACKEND = null;
    public static String WM5_HBASE_STATE_BACKEND = null;
    // dmp SqlServer 源数据库信息
    public static String DMP_SQLSERVER_HOSTNAME = null;
    public static String DMP_SQLSERVER_USERNAME = null;
    public static String DMP_SQLSERVER_PASSWORD = null;
    public static Integer DMP_SQLSERVER_PORT = null;
    public static String DMP_STATE_BACKEND = null;
    public static String DMP_HBASE_STATE_BACKEND = null;
    // MySQL目标数据库
    public static String SINK_MYSQL_URL = "jdbc:mysql://shucang003:50010/test?serverTimezone=GMT%2B8";
    public static String SINK_MYSQL_USERNAME = "root";
    public static String SINK_MYSQL_PASSWORD = "123456";

    //MySQL驱动
    public static final String SQLSERVER_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    // SqlServer目标数据库
    public static String SINK_SQLSERVER_URL = null;
    public static String SINK_SQLSERVER_USERNAME = null;
    public static String SINK_SQLSERVER_PASSWORD = null;

    // 初始化配置信息
    public void init(String env) throws IOException {
        System.out.println("=========================================初始化环境入参：" + env);
        if (StringUtils.isBlank(env) || !envs.contains(env)) env = "dev";
        System.out.println("=========================================初始化环境为2：" + env);
        try {
            System.out.println("=========================================属性初始化开始。。。。。。。。。。。。。。");
            InputStream in = this.getClass().getResourceAsStream("/application-" + env + ".properties");
            properties = new Properties();
            InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");
            properties.load(inputStreamReader);
            System.out.println("=========================================属性初始化结束。。。。。。。。。。。。。。");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("=========================================属性初始化异常。。。。。。。。。。。。。。");
        }

        // kafka连接信息
        KAFKA_SERVER = properties.getProperty("kafka.servers");
        // hbase连接信息
        PHOENIX_SERVER = properties.getProperty("phoenix.server");

        // 状态后端
        FS_STATE_BACKEND = properties.getProperty("fs.state.backend");
        //WMS2 MySQL 源数据库连接信息
        WMS_CDC_TABLES = properties.getProperty("wms.cdc.tables");
        //WMS2 MySQL 源数据库连接信息
        WMS2_MYSQL_HOSTNAME = properties.getProperty("wms2.mysql.hostname");
        WMS2_MYSQL_USERNAME = properties.getProperty("wms2.mysql.username");
        WMS2_MYSQL_PASSWORD = properties.getProperty("wms2.mysql.password");
        WMS2_MYSQL_PORT = Integer.valueOf(properties.getProperty("wms2.mysql.port"));
        WM2_STATE_BACKEND = properties.getProperty("wms2.state.backend");
        WM2_HBASE_STATE_BACKEND = properties.getProperty("wms2.hbase.state.backend");
        //WMS3 MySQL 源数据库连接信息
        WMS3_MYSQL_HOSTNAME = properties.getProperty("wms3.mysql.hostname");
        WMS3_MYSQL_USERNAME = properties.getProperty("wms3.mysql.username");
        WMS3_MYSQL_PASSWORD = properties.getProperty("wms3.mysql.password");
        WMS3_MYSQL_PORT = Integer.valueOf(properties.getProperty("wms3.mysql.port"));
        WM3_STATE_BACKEND = properties.getProperty("wms3.state.backend");
        WM3_HBASE_STATE_BACKEND = properties.getProperty("wms3.hbase.state.backend");
        //WMS4 MySQL 源数据库连接信息
        WMS4_MYSQL_HOSTNAME = properties.getProperty("wms4.mysql.hostname");
        WMS4_MYSQL_USERNAME = properties.getProperty("wms4.mysql.username");
        WMS4_MYSQL_PASSWORD = properties.getProperty("wms4.mysql.password");
        WMS4_MYSQL_PORT = Integer.valueOf(properties.getProperty("wms4.mysql.port"));
        WM4_STATE_BACKEND = properties.getProperty("wms4.state.backend");
        WM4_HBASE_STATE_BACKEND = properties.getProperty("wms4.hbase.state.backend");
        //WMS5 MySQL 源数据库连接信息
        WMS5_MYSQL_HOSTNAME = properties.getProperty("wms5.mysql.hostname");
        WMS5_MYSQL_USERNAME = properties.getProperty("wms5.mysql.username");
        WMS5_MYSQL_PASSWORD = properties.getProperty("wms5.mysql.password");
        WMS5_MYSQL_PORT = Integer.valueOf(properties.getProperty("wms5.mysql.port"));
        WM5_STATE_BACKEND = properties.getProperty("wms5.state.backend");
        WM5_HBASE_STATE_BACKEND = properties.getProperty("wms5.hbase.state.backend");
        // dmp SqlServer 源数据库信息
        DMP_SQLSERVER_HOSTNAME = properties.getProperty("dmp.sqlserver.hostname");
        DMP_SQLSERVER_USERNAME = properties.getProperty("dmp.sqlserver.username");
        DMP_SQLSERVER_PASSWORD = properties.getProperty("dmp.sqlserver.password");
        DMP_SQLSERVER_PORT = Integer.valueOf(properties.getProperty("dmp.sqlserver.port"));
        DMP_STATE_BACKEND = properties.getProperty("dmp.state.backend");
        DMP_HBASE_STATE_BACKEND = properties.getProperty("dmp.hbase.state.backend");

        // SqlServer目标数据库
        SINK_SQLSERVER_URL = properties.getProperty("sink.sqlserver.url");;
        SINK_SQLSERVER_USERNAME = properties.getProperty("sink.sqlserver.username");;
        SINK_SQLSERVER_PASSWORD = properties.getProperty("sink.sqlserver.password");;
    }

}
