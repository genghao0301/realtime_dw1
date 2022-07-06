package com.vx.app.func;

import com.alibaba.fastjson.JSONObject;
import com.vx.common.GmallConfig;
import com.vx.utils.DimUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.phoenix.exception.PhoenixIOException;
import org.apache.phoenix.execute.CommitException;
import org.apache.phoenix.schema.ColumnNotFoundException;
import org.apache.phoenix.schema.TableNotFoundException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;

/**
 * @version V1.0
 * @ClassName: DimSink
 * @Description: TODO
 * @Author: xiehp
 * @Date: 2022/4/24 18:28
 */
public class DimHbaseSink extends RichSinkFunction<JSONObject> {

    private Connection connection = null;
    private String env;

    public DimHbaseSink(String env) {
        this.env = env;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        try {
            //初始化Phoenix连接
            Class.forName(GmallConfig.PHOENIX_DRIVER);
            if (StringUtils.isBlank(GmallConfig.PHOENIX_SERVER))
                GmallConfig.getSingleton().init(env);
            System.out.println("=================================================sink hbase地址："+GmallConfig.PHOENIX_SERVER);
            connection = DriverManager.getConnection(GmallConfig.PHOENIX_SERVER);
            //connection.setSchema(GmallConfig.HBASE_SCHEMA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //将数据写入Phoenix：upsert into t(id, name, sex) values(..., ..., ...)
    @Override
    public void invoke(JSONObject jsonObject, Context context) throws Exception {

        PreparedStatement preparedStatement = null;
        try {
            if (!jsonObject.containsKey("invokeNum")) jsonObject.put("invokeNum" , 0);
            int invokeNum = jsonObject.getInteger("invokeNum");
            if (invokeNum > 10) return;
            jsonObject.put("invokeNum" , ++invokeNum);
            //获取数据中的Key以及Value
            JSONObject data = jsonObject.getJSONObject("data");
            if (data == null) data = jsonObject.getJSONObject("after");
            if (data == null) data = jsonObject.getJSONObject("before");
            //获取数据库名
            String dbName = jsonObject.getString("db");
            //获取主键名
            String primaryKey = jsonObject.getString("primaryKey");
            // 生成新的主键
            String primaryKeyVal = dbName + "_" +data.getString(primaryKey);
            data.put(primaryKey, primaryKeyVal);
            //获取表名
            String tableName = "dim_" + jsonObject.getString("table");
            //列名
            Set<String> keys = data.keySet();
            Collection<Object> values = data.values();

//            //如果表不存在就创建
//            checkTable(tableName, keys, primaryKey , null);

            //创建插入数据的SQL
            String upsertSql = genUpsertSql(tableName, keys, values);
            System.out.println(upsertSql);
            //编译SQL
            preparedStatement = connection.prepareStatement(upsertSql);
            //执行
            preparedStatement.executeUpdate();
            //提交
            connection.commit();
            //判断如果是更新操作，则删除Redis中的数据保证数据的一致性
            String op = jsonObject.getString("op");
            if ("update".equals(op) || "delete".equals(op))
                DimUtil.deleteCached(tableName, null);
        }
        catch (TableNotFoundException e) {
            System.out.println("***************************************表不存在，增加表：" + e.getTableName());
            try {
                // 增加表字段
                createTable(jsonObject);
                // 重新插入数据
                //Thread.sleep(100 * new Random(9).nextInt());
                invoke(jsonObject, context);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        catch (ColumnNotFoundException e) {
            System.out.println("***************************************表字段不存在，增加表字段：" + e.getColumnName());
            try {
                // 增加表字段
                addTableColumn(jsonObject,e);
                // 重新插入数据
                //Thread.sleep(100 * new Random(9).nextInt());
                // 重新插入数据
                invoke(jsonObject, context);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        catch (CommitException e) {
            // 重新插入数据
            invoke(jsonObject, context);
        }
        catch (PhoenixIOException e) {
            // 重新插入数据
            invoke(jsonObject, context);
        }
        catch (Exception e) {
            // 重新插入数据
            //invoke(jsonObject, context);
            e.printStackTrace();
            System.out.println("插入Phoenix数据失败2！");
        }
        finally {
            if (preparedStatement != null ) {
                preparedStatement.close();
            }
        }
    }

    //创建插入数据的SQL upsert into t(id, name, sex) values('...', '...', '...')
    private String genUpsertSql(String tableName, Set<String> keys, Collection<Object> values) {
        String values2 = " values( '" + StringUtils.join(values, "','") + "')";
        System.out.println(StringUtils.join(values, "','"));
        System.out.println(values2);
        return "upsert into " + GmallConfig.HBASE_SCHEMA + "." +
                tableName + "(" + StringUtils.join(keys, ",") + ")" +
                " values( '" + StringUtils.join(values, "','") + "')";
    }

    //建表语句 : create table if not exists db.tn(id varchar primary key,tm_name varchar) xxx;
    private void createTable(JSONObject jsonObject) {

        JSONObject data = jsonObject.getJSONObject("data");
        if (data == null) data = jsonObject.getJSONObject("after");
        if (data == null) data = jsonObject.getJSONObject("before");
        //获取数据库名
        String dbName = jsonObject.getString("db");
        //获取主键名
        String sinkPk = jsonObject.getString("primaryKey");
        // 生成新的主键
        String primaryKeyVal = dbName + "_" +data.getString(sinkPk);
        data.put(sinkPk, primaryKeyVal);
        //获取表名
        String sinkTable = "dim_" + jsonObject.getString("table");
        //列名
        Set<String> sinkColumns = data.keySet();
        Collection<Object> values = data.values();

        //
        String sinkExtend = null;

        PreparedStatement preparedStatement = null;

        try {
            if (sinkPk == null) {
                sinkPk = "id";
            }
            if (sinkExtend == null) {
                sinkExtend = " SALT_BUCKETS=16, COMPRESSION='GZ' ";
            }

            StringBuffer createTableSQL = new StringBuffer("create table if not exists ")
                    .append(GmallConfig.HBASE_SCHEMA)
                    .append(".")
                    .append(sinkTable)
                    .append("(");

            int i = 0;
            for (String field : sinkColumns) {
                //判断是否为主键
                if (sinkPk.equals(field)) {
                    createTableSQL.append(field).append(" varchar primary key ");
                } else {
                    createTableSQL.append(field).append(" varchar ");
                }

                //判断是否为最后一个字段,如果不是,则添加","
                if (i < sinkColumns.size() - 1) {
                    createTableSQL.append(",");
                }
                i++;
            }

//            String[] fields = sinkColumns.split(",");
//            for (int i = 0; i < fields.length; i++) {
//                String field = fields[i];
//                //判断是否为主键
//                if (sinkPk.equals(field)) {
//                    createTableSQL.append(field).append(" varchar primary key ");
//                } else {
//                    createTableSQL.append(field).append(" varchar ");
//                }
//                //判断是否为最后一个字段,如果不是,则添加","
//                if (i < fields.length - 1) {
//                    createTableSQL.append(",");
//                }
//            }

            createTableSQL.append(")").append(sinkExtend);

            //打印建表语句
            System.out.println(createTableSQL);

            //预编译SQL
            preparedStatement = connection.prepareStatement(createTableSQL.toString());

            //执行
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Phoenix表" + sinkTable + "建表失败！");
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //建表语句 : create table if not exists db.tn(id varchar primary key,tm_name varchar) xxx;
    private void checkTable(String sinkTable, Set<String> sinkColumns, String sinkPk, String sinkExtend) {

        PreparedStatement preparedStatement = null;

        try {
            if (sinkPk == null) {
                sinkPk = "id";
            }
            if (sinkExtend == null) {
                sinkExtend = "";
            }

            StringBuffer createTableSQL = new StringBuffer("create table if not exists ")
                    .append(GmallConfig.HBASE_SCHEMA)
                    .append(".")
                    .append(sinkTable)
                    .append("(");

            int i = 0;
            for (String field : sinkColumns) {
                //判断是否为主键
                if (sinkPk.equals(field)) {
                    createTableSQL.append(field).append(" varchar primary key ");
                } else {
                    createTableSQL.append(field).append(" varchar ");
                }

                //判断是否为最后一个字段,如果不是,则添加","
                if (i < sinkColumns.size() - 1) {
                    createTableSQL.append(",");
                }
                i++;
            }

//            String[] fields = sinkColumns.split(",");
//            for (int i = 0; i < fields.length; i++) {
//                String field = fields[i];
//                //判断是否为主键
//                if (sinkPk.equals(field)) {
//                    createTableSQL.append(field).append(" varchar primary key ");
//                } else {
//                    createTableSQL.append(field).append(" varchar ");
//                }
//                //判断是否为最后一个字段,如果不是,则添加","
//                if (i < fields.length - 1) {
//                    createTableSQL.append(",");
//                }
//            }

            createTableSQL.append(")").append(sinkExtend);

            //打印建表语句
            System.out.println(createTableSQL);

            //预编译SQL
            preparedStatement = connection.prepareStatement(createTableSQL.toString());

            //执行
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Phoenix表" + sinkTable + "建表失败！");
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //建表语句 : create table if not exists db.tn(id varchar primary key,tm_name varchar) xxx;
    private void addTableColumn(JSONObject jsonObject, ColumnNotFoundException e2) {

        PreparedStatement preparedStatement = null;

        //获取表名
        String sinkTable = null;
        try {
            //获取表名
            sinkTable = "dim_" + jsonObject.getString("table");

            StringBuffer createTableSQL = new StringBuffer(" alter table ")
                    .append(GmallConfig.HBASE_SCHEMA)
                    .append(".")
                    .append(sinkTable)
                    .append(" add if not exists " +e2.getColumnName() +" varchar ");

            //打印建表语句
            System.out.println(createTableSQL);

            //预编译SQL
            preparedStatement = connection.prepareStatement(createTableSQL.toString());

            //执行
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Phoenix表" + sinkTable + "增加字段" + e2.getColumnName() + "失败！");
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
