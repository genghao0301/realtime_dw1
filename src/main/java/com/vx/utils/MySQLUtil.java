package com.vx.utils;

import cdc.vx.bean.DimMdClient;
import com.vx.bean.TableProcess;
import com.google.common.base.CaseFormat;
import com.vx.bean.TransientSink;
import com.vx.common.GmallConfig;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ORM Object Relation Mapping
 */
public class MySQLUtil {

    //声明
    private static Connection connection;

    //初始化连接
    private static Connection init() {
        try {
            //1.注册驱动
            Class.forName(GmallConfig.MYSQL_DRIVER);

            //2.获取连接
            connection = DriverManager.getConnection(GmallConfig.MYSQL_URL,
                    GmallConfig.MYSQL_USERNAME,
                    GmallConfig.MYSQL_PASSWORD
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

    public static <T> SinkFunction getSink(String sql) {
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
                        .withDriverName(GmallConfig.MYSQL_DRIVER)
                        .withUrl(GmallConfig.MYSQL_URL)
                        .withUsername(GmallConfig.MYSQL_USERNAME)
                        .withPassword(GmallConfig.MYSQL_PASSWORD)
                        .build());
    }

}
