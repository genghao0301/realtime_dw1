package com.vx.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.java.utils.ParameterTool;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class GmallConfig {

    private static List<String> envs = Arrays.asList("dev", "uat","prod");
    static Properties parameterTool = null;

    private GmallConfig() {}
    public static GmallConfig getSingleton() {
        return Inner.instance;
    }
    private static class Inner {
        private static final GmallConfig instance = new GmallConfig();
    }

    // kafka服务器信息
    public static String KAFKA_SERVER = null;
    // kafka服务器信息
    public static String FLINK_WM4_STATE_BACKEND = null;

    //Phoenix库名
    public static String HBASE_SCHEMA = "FLINK_REALTIME";
    //Phoenix驱动
    public static String PHOENIX_DRIVER = "org.apache.phoenix.jdbc.PhoenixDriver";
    //Phoenix连接参数
    public static String PHOENIX_SERVER = null;

    //ClickHouse驱动
    public static String CLICKHOUSE_DRIVER = "ru.yandex.clickhouse.ClickHouseDriver";
    //ClickHouse连接地址
    public static String CLICKHOUSE_URL = "jdbc:clickhouse://shucang001:8123/default";

    //MySQL驱动
    public static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
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
    // SqlServer目标数据库
    public static String SINK_SQLSERVER_URL = "jdbc:mysql://shucang003:50010/test?serverTimezone=GMT%2B8";
    public static String SINK_SQLSERVER_USERNAME = "root";
    public static String SINK_SQLSERVER_PASSWORD = "123456";

    // 初始化配置信息
    public void init(String env) throws IOException {
        if (StringUtils.isBlank(env) || !envs.contains(env)) env = "dev";

        InputStream in = this.getClass().getResourceAsStream("/application-" + env + ".properties");
        parameterTool = new Properties();
        InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");
        parameterTool.load(inputStreamReader);

        // kafka连接信息
        KAFKA_SERVER = parameterTool.getProperty("kafka.servers");
        // hbase连接信息
        PHOENIX_SERVER = parameterTool.getProperty("phoenix.server");
        //WMS2 MySQL 源数据库连接信息
        WMS2_MYSQL_HOSTNAME = parameterTool.getProperty("wms2.mysql.hostname");
        WMS2_MYSQL_USERNAME = parameterTool.getProperty("wms2.mysql.username");
        WMS2_MYSQL_PASSWORD = parameterTool.getProperty("wms2.mysql.password");
        WMS2_MYSQL_PORT = Integer.valueOf(parameterTool.getProperty("wms2.mysql.port"));
        WM2_STATE_BACKEND = parameterTool.getProperty("wms2.state.backend");
        WM2_HBASE_STATE_BACKEND = parameterTool.getProperty("wms2.hbase.state.backend");
        //WMS3 MySQL 源数据库连接信息
        WMS3_MYSQL_HOSTNAME = parameterTool.getProperty("wms3.mysql.hostname");
        WMS3_MYSQL_USERNAME = parameterTool.getProperty("wms3.mysql.username");
        WMS3_MYSQL_PASSWORD = parameterTool.getProperty("wms3.mysql.password");
        WMS3_MYSQL_PORT = Integer.valueOf(parameterTool.getProperty("wms3.mysql.port"));
        WM3_STATE_BACKEND = parameterTool.getProperty("wms3.state.backend");
        WM3_HBASE_STATE_BACKEND = parameterTool.getProperty("wms3.hbase.state.backend");
        //WMS4 MySQL 源数据库连接信息
        WMS4_MYSQL_HOSTNAME = parameterTool.getProperty("wms4.mysql.hostname");
        WMS4_MYSQL_USERNAME = parameterTool.getProperty("wms4.mysql.username");
        WMS4_MYSQL_PASSWORD = parameterTool.getProperty("wms4.mysql.password");
        WMS4_MYSQL_PORT = Integer.valueOf(parameterTool.getProperty("wms4.mysql.port"));
        WM4_STATE_BACKEND = parameterTool.getProperty("wms4.state.backend");
        WM4_HBASE_STATE_BACKEND = parameterTool.getProperty("wms4.hbase.state.backend");
        //WMS5 MySQL 源数据库连接信息
        WMS5_MYSQL_HOSTNAME = parameterTool.getProperty("wms5.mysql.hostname");
        WMS5_MYSQL_USERNAME = parameterTool.getProperty("wms5.mysql.username");
        WMS5_MYSQL_PASSWORD = parameterTool.getProperty("wms5.mysql.password");
        WMS5_MYSQL_PORT = Integer.valueOf(parameterTool.getProperty("wms5.mysql.port"));
        WM5_STATE_BACKEND = parameterTool.getProperty("wms5.state.backend");
        WM5_HBASE_STATE_BACKEND = parameterTool.getProperty("wms5.hbase.state.backend");
        // dmp SqlServer 源数据库信息
        DMP_SQLSERVER_HOSTNAME = parameterTool.getProperty("dmp.sqlserver.hostname");
        DMP_SQLSERVER_USERNAME = parameterTool.getProperty("dmp.sqlserver.username");
        DMP_SQLSERVER_PASSWORD = parameterTool.getProperty("dmp.sqlserver.password");
        DMP_SQLSERVER_PORT = Integer.valueOf(parameterTool.getProperty("dmp.sqlserver.port"));
        DMP_STATE_BACKEND = parameterTool.getProperty("dmp.state.backend");
        DMP_HBASE_STATE_BACKEND = parameterTool.getProperty("dmp.hbase.state.backend");

        // SqlServer目标数据库
        SINK_SQLSERVER_URL = parameterTool.getProperty("sink.sqlserver.url");;
        SINK_SQLSERVER_USERNAME = parameterTool.getProperty("sink.sqlserver.username");;
        SINK_SQLSERVER_PASSWORD = parameterTool.getProperty("sink.sqlserver.password");;
    }

}
