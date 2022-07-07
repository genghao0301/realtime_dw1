package com.vx.app.func;

import com.vx.bean.BaseBean;
import com.vx.bean.TableName;
import com.vx.bean.TransientSink;
import com.vx.common.GmallConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @version V1.0
 * @ClassName: SinkToMySQL
 * @Description: 往数据库插入数据
 * @Author: xiehp
 * @Date: 2022/5/24 15:41
 */
public class SinkToSqlServer<T> extends RichSinkFunction<T> {

    private static PreparedStatement ps;
    private static PreparedStatement selectps;
    private static PreparedStatement updateps;
    private static Connection connection = null;

    private String url;
    private String username;
    private String password;
    private String env;

    public SinkToSqlServer(String env) {
        this.env = env;
    }

    public SinkToSqlServer(String url2, String username2, String password2) {
        url = url2;
        username = username2;
        password = password2;
    }

    /**
     * open() 方法中建立连接，这样不用每次 invoke 的时候都要建立连接和释放连接
     *
     * @param parameters
     * @throws Exception
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        try {
            if (connection != null) return;
            connection = getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("sinkToMysql open is error " + e.getMessage());
        }
    }

    private Connection getConnection() {
        try {
            //加载数据库驱动
            Class.forName(GmallConfig.SQLSERVER_DRIVER);
            if (StringUtils.isBlank(GmallConfig.SINK_SQLSERVER_URL))
                GmallConfig.getSingleton().init(env);
            //获取连接
            connection = DriverManager.getConnection(
                    GmallConfig.SINK_SQLSERVER_URL,
                    GmallConfig.SINK_SQLSERVER_USERNAME,
                    GmallConfig.SINK_SQLSERVER_PASSWORD
            );
            System.out.println("数据库连接建立成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("-----------mysql get connection has exception , msg = " + e.getMessage());
        }
        return connection;
    }

    @Override
    public void close() throws Exception {
        super.close();
        //关闭连接和释放资源
        try {
            if (ps != null) {
                System.out.println("关闭插入ps");
                ps.close();
            }
            if (selectps != null) {
                System.out.println("关闭查询ps");
                selectps.close();
            }
            if (updateps != null) {
                System.out.println("关闭更新ps");
                updateps.close();
            }
//            if (connection != null && !connection.isClosed()) {
//                System.out.println("关闭数据库连接");
//                connection.close();
//            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("close mysql is error " + e.getMessage());
        }
    }

    /**
     * 每条数据的插入都要调用一次 invoke() 方法
     *
     * @param invTransaction
     * @param context
     * @throws Exception
     */
    @Override
    public void invoke(T obj, Context context) throws SQLException {

        PreparedStatement ps = null;
        BaseBean baseBean = null;
        String op = null;
        try {
            if (obj instanceof BaseBean) {
                baseBean = (BaseBean) obj;
                op = baseBean.getOp();
            }
            if (StringUtils.isBlank(op)) {
                insert(obj);
                return;
            }
            if ("read".equals(op) || "insert".equals(op))
                insert(obj);
            else if ("update".equals(op))
                update(obj);
            else if ("delete".equals(op))
                delete(baseBean);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("invoke is error " + e.getMessage());
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    private void insert(T obj) {
        PreparedStatement ps = null;
        try {
            Class<?> classz = obj.getClass();
            //获取表名
            TableName tableName = classz.getAnnotation(TableName.class);
            //反射的方式获取所有的属性名
            Field[] fields = classz.getDeclaredFields();
            // 生成插入sql语句
            StringBuffer sql = new StringBuffer();
            StringBuffer sql1 = new StringBuffer();
            sql.append("insert into ").append(tableName.value()).append(" (");
            for (int i=0; i<fields.length; i++) {
                //获取字段上的注解
                TransientSink transientSink = fields[i].getAnnotation(TransientSink.class);
                if (transientSink != null) continue;
                fields[i].setAccessible(true);
                sql.append(fields[i].getName());
                sql1.append("?");
                if (i!= fields.length-1) {
                    sql.append(",");
                    sql1.append(",");
                }
            }
            sql.append(") ").append(" values (").append(sql1).append(") ");

            //定义跳过的属性
            int offset = 0;
            ps = connection.prepareStatement(sql.toString());
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
                //给站位符赋值
                ps.setObject(i + 1 - offset, field.get(obj));
            }
            int a = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("invoke is error " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void update(T obj) {
        PreparedStatement ps = null;
        try {
            Class<?> classz = obj.getClass();
            //获取表名
            TableName tableName = classz.getAnnotation(TableName.class);
            //反射的方式获取所有的属性名
            Field[] fields = classz.getDeclaredFields();
            // 生成插入sql语句
            StringBuffer sql = new StringBuffer();
            sql.append("update ").append(tableName.value()).append(" set ");
            int paramNum = 0;
            for (int i=0; i<fields.length; i++) {
                //获取字段上的注解
                TransientSink transientSink = fields[i].getAnnotation(TransientSink.class);
                if (transientSink != null) continue;
                paramNum++;
                sql.append(fields[i].getName()).append(" = ? ");
                if (i != fields.length - 1)
                    sql.append(" , ");
            }
            sql.append(" where ").append(fields[0].getName()) .append(" = ? ");
            //定义跳过的属性
            int offset = 0;
            ps = connection.prepareStatement(sql.toString());
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
                //给站位符赋值
                ps.setObject(i + 1 - offset, field.get(obj));
            }
            // 设置主键
            fields[0].setAccessible(true);
            ps.setObject(paramNum,fields[0].get(obj));
            // 执行SQL
            int a = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("invoke is error " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void delete(BaseBean baseBean) {
        PreparedStatement ps = null;
        try {
            Class<?> classz = baseBean.getClass();
            //获取表名
            TableName tableName = classz.getAnnotation(TableName.class);
            //反射的方式获取所有的属性名
            Field[] fields = classz.getDeclaredFields();
            // 生成插入sql语句
            String sql = "Delete from " + tableName + " Where "+ baseBean.getPrimaryKey() +" = ?";
            // 执行删除操作
            ps = connection.prepareStatement(sql);
            ps.setObject(1, baseBean.getPrimaryKeyValue());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("invoke is error " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}