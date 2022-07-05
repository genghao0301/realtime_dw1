package com.vx.utils;

import com.google.common.base.CaseFormat;
import com.vx.bean.InOrderDetail;
import com.vx.bean.SkuClass;
import com.vx.bean.TableName;
import com.vx.bean.TransientSink;
import com.vx.common.GmallConfig;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import scala.annotation.meta.field;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ORM Object Relation Mapping
 */
public class SqlServerUtil {

    //声明
    private static Connection connection;

    //初始化连接
    private static Connection init() {
        try {
            //GmallConfig.getSingleton().init();
            //1.注册驱动
            Class.forName(GmallConfig.SQLSERVER_DRIVER);

            //2.获取连接
            connection = DriverManager.getConnection(GmallConfig.SINK_SQLSERVER_URL,
                    GmallConfig.SINK_SQLSERVER_USERNAME,
                    GmallConfig.SINK_SQLSERVER_PASSWORD
            );

            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取连接失败！");
        }
    }

    public static <T> List<T> queryList(String sql, Class<T> cls, boolean underScoreToCamel) {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            //初始化连接
            if (connection == null) {
                connection = init();
            }

            //3.编译SQL,并给占位符赋值
            preparedStatement = connection.prepareStatement(sql);

            //4.执行查询
            resultSet = preparedStatement.executeQuery();

            //6.解析查询结果
            ArrayList<T> list = new ArrayList<>();

            //取出列的元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {

                //封装JavaBean并加入集合
                T t = cls.newInstance();

                for (int i = 1; i <= columnCount; i++) {

                    //获取列名
                    String columnName = metaData.getColumnName(i);
                    if (underScoreToCamel) {
                        columnName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName);
                    }
                    Object object = resultSet.getObject(i);

                    //给JavaBean对象赋值
                    BeanUtils.setProperty(t, columnName, object);

                }

                list.add(t);
            }

            //返回结果
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("查询配置信息失败！！！");
        } finally {
            //7.释放资源
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            /*if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }*/
        }
    }

    /**
     * @Description: 根据class生成insert SQL
     * @Author: xiehp
     * @Date: 2022/7/5 10:55
     * @Param [classz]
     * @return java.lang.String
     */
    public static <T> String genInsertSql(Class<T> classz){
        StringBuffer sb = new StringBuffer();
        StringBuffer sql1 = new StringBuffer();
        //获取表名
        TableName tableName = classz.getAnnotation(TableName.class);
        //反射的方式获取所有的属性名
        Field[] fields = classz.getDeclaredFields();
        sb.append("insert into ").append(tableName.value()).append(" (");
        for (int i=0; i<fields.length; i++) {
            //获取字段上的注解
            TransientSink transientSink = fields[i].getAnnotation(TransientSink.class);
            if (transientSink != null) continue;
            fields[i].setAccessible(true);
            sb.append(fields[i].getName());
            sql1.append("?");
            if (i!= fields.length-1) {
                sb.append(",");
                sql1.append(",");
            }
        }
        sb.append(") ").append(" values (").append(sql1).append(") ");
        return sb.toString();
    }

    /**
     * @Description: 生成sink
     * @Author: xiehp
     * @Date: 2022/7/5 10:56
     * @Param [sql]
     * @return org.apache.flink.streaming.api.functions.sink.SinkFunction
     */
    public static <T> SinkFunction getSink(String sql) {
        System.out.println(sql);
        return JdbcSink.sink(sql,
                new JdbcStatementBuilder<T>() {
                    @Override
                    public void accept(PreparedStatement preparedStatement, T obj) throws SQLException {

                        //反射的方式获取所有的属性名
                        Field[] fields = obj.getClass().getDeclaredFields();

                        //定义跳过的属性
                        int offset = 0;

                        for (int i = 0; i < fields.length; i++) {

                            //获取字段名
                            Field field = fields[i];

                            //获取字段上的注解
                            TransientSink transientSink = field.getAnnotation(TransientSink.class);
                            if (transientSink != null) {
                                offset++;
                                continue;
                            }

                            //设置可访问私有属性的值
                            field.setAccessible(true);

                            try {
                                Object o = field.get(obj);

                                //给站位符赋值
                                preparedStatement.setObject(i + 1 - offset, o);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                JdbcExecutionOptions.builder()
                        .withBatchSize(5)
                        .build(),
                new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                        .withDriverName(GmallConfig.SQLSERVER_DRIVER)
                        .withUrl(GmallConfig.SINK_SQLSERVER_URL)
                        .withUsername(GmallConfig.SINK_SQLSERVER_USERNAME)
                        .withPassword(GmallConfig.SINK_SQLSERVER_PASSWORD)
                        .build());
    }

}
