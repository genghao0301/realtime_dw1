package com.vx.app.func;

import com.vx.bean.DwdInvTransaction;
import com.vx.common.GmallConfig;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.math.BigDecimal;
import java.sql.*;

/**
 * @version V1.0
 * @ClassName: SinkToMySQL
 * @Description: 往数据库插入数据
 * @Author: xiehp
 * @Date: 2022/5/24 15:41
 */
public class SinkToMySQL extends RichSinkFunction<DwdInvTransaction> implements SinkFunction<DwdInvTransaction> {

    private static PreparedStatement ps;
    private static PreparedStatement selectps;
    private static PreparedStatement updateps;
    private static Connection connection = null;

    private String url;
    private String username;
    private String password;

    public SinkToMySQL() {
    }

    public SinkToMySQL(String url2, String username2, String password2) {
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
            Class.forName(GmallConfig.MYSQL_DRIVER);
            //获取连接
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("数据库连接建立成功");
            String selectSql = "select id, "
                    + " ea_num, "
                    + " pallet_num, "
                    + " cs_num, "
                    + " total_weight, "
                    + " total_gross_weight, "
                    + " total_net_weight "

                    + " from ads_inv_transaction where 1=1 "
                    + " and warehouse_code = ?"
                    + " and client_code = ?"
                    + " and client_name = ?"
                    + " and reference_type = ?"
                    + " and event_time_day = ?" +
                    " ;";
            selectps = connection.prepareStatement(selectSql);
            String sql = "insert into ads_inv_transaction(" +
                    " warehouse_code, " +
                    " client_code, " +
                    " client_name, " +
                    " reference_type, " +
                    " event_time_day, " +
                    " ea_num, "
                    + " pallet_num, "
                    + " cs_num, "
                    + " total_weight, "
                    + " total_gross_weight, "
                    + " total_net_weight "
                    +
                    ") values(?,?,?,?,?,?,?,?,?,?,?);";
            ps = connection.prepareStatement(sql);
            String updateSql = " update ads_inv_transaction set "
                    + " ea_num = ?, "
                    + " pallet_num = ? , "
                    + " cs_num = ? , "
                    + " total_weight = ? , "
                    + " total_gross_weight = ? , "
                    + " total_net_weight = ?  "
                    + " where 1=1 "
                    + " and warehouse_code = ?"
                    + " and client_code = ?"
                    + " and client_name = ?"
                    + " and reference_type = ?"
                    + " and event_time_day = ?" +
                    ";";
            updateps = connection.prepareStatement(updateSql);
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
    public void invoke(DwdInvTransaction invTransaction, Context context) throws SQLException {
        ResultSet resultSet = null;
        try {
            //判断是否连接，尝试重连
            try {
                selectps.setString(1, invTransaction.getWarehouse_code());
                selectps.setString(2, invTransaction.getClient_code());
                selectps.setString(3, invTransaction.getClient_name());
                selectps.setString(4, invTransaction.getReference_type());
                selectps.setString(5, invTransaction.getEventTimeDay());
                resultSet = selectps.executeQuery();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("mysql 连接过期，重新连接2");
                getConnection();
                selectps.setString(1, invTransaction.getWarehouse_code());
                selectps.setString(2, invTransaction.getClient_code());
                selectps.setString(3, invTransaction.getClient_name());
                selectps.setString(4, invTransaction.getReference_type());
                selectps.setString(5, invTransaction.getEventTimeDay());
                resultSet = selectps.executeQuery();
            }
            //遍历数据集合
            if (resultSet != null && resultSet.next()) {
                System.out.println("=====================================已有该时段数据");
                BigDecimal ea_num = resultSet.getBigDecimal("ea_num");
                BigDecimal pallet_num = resultSet.getBigDecimal("pallet_num");
                BigDecimal cs_num = resultSet.getBigDecimal("cs_num");
                BigDecimal total_weight = resultSet.getBigDecimal("total_weight");
                BigDecimal total_gross_weight = resultSet.getBigDecimal("total_gross_weight");
                BigDecimal total_net_weight = resultSet.getBigDecimal("total_net_weight");

                System.out.println("数据库数据为：" + ea_num);
                System.out.println("================================update新聚合结果为：" + invTransaction.getInv_adjustment_qty());
                updateps.setBigDecimal(1, invTransaction.getInv_adjustment_qty().add(ea_num));
                updateps.setBigDecimal(2, invTransaction.getPalletNum().add(pallet_num));
                updateps.setBigDecimal(3, invTransaction.getCsNum().add(cs_num));
                updateps.setBigDecimal(4, invTransaction.getTotal_weight().add(total_weight));
                updateps.setBigDecimal(5, invTransaction.getTotal_gross_weight().add(total_gross_weight));
                updateps.setBigDecimal(6, invTransaction.getTotal_net_weight().add(total_net_weight));
                // 条件设置
                updateps.setString(7, invTransaction.getWarehouse_code());
                updateps.setString(8, invTransaction.getClient_code());
                updateps.setString(9, invTransaction.getClient_name());
                updateps.setString(10, invTransaction.getReference_type());
                updateps.setString(11, invTransaction.getEventTimeDay());
                System.out.println(updateps);
                updateps.execute();
            } else {
                System.out.println("================================insert聚合结果为：" + invTransaction.getEaNum());
                ps.setString(1, invTransaction.getWarehouse_code());
                ps.setString(2, invTransaction.getClient_code());
                ps.setString(3, invTransaction.getClient_name());
                ps.setString(4, invTransaction.getReference_type());
                ps.setString(5, invTransaction.getEventTimeDay());
                ps.setBigDecimal(6, invTransaction.getInv_adjustment_qty());
                ps.setBigDecimal(7, invTransaction.getPalletNum());
                ps.setBigDecimal(8, invTransaction.getCsNum());
                ps.setBigDecimal(9, invTransaction.getTotal_weight());
                ps.setBigDecimal(10, invTransaction.getTotal_gross_weight());
                ps.setBigDecimal(11, invTransaction.getTotal_net_weight());
                System.out.println(ps);
                ps.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("invoke is error " + e.getMessage());
            System.out.println("insert mysql result is " + invTransaction);
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }
}