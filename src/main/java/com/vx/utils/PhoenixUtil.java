package com.vx.utils;

import cdc.vx.bean.DimMdClient;
import cdc.vx.bean.DimMdLocation;
import cdc.vx.bean.DimMdSku;
import com.google.gson.JsonObject;
import com.vx.bean.DwdInvTransaction;
import com.vx.common.GmallConfig;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhoenixUtil {

    //声明
    private static Connection connection;

    //初始化连接
    private static Connection init() {
        try {
            Class.forName(GmallConfig.PHOENIX_DRIVER);
            Connection connection = DriverManager.getConnection(GmallConfig.PHOENIX_SERVER);

            //设置连接到的Phoenix的库
            connection.setSchema(GmallConfig.HBASE_SCHEMA);

            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取连接失败！");
        }
    }

    public static <T> List<T> queryList(String sql, Class<T> cls) throws Exception {
        long start = System.currentTimeMillis();
        //初始化连接
        if (connection == null) {
            connection = init();
        }

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            //编译SQL
            preparedStatement = connection.prepareStatement(sql);

            //执行查询
            resultSet = preparedStatement.executeQuery();

            //获取查询结果中的元数据信息
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            ArrayList<T> list = new ArrayList<>();

            while (resultSet.next()) {

                T t = cls.newInstance();

                for (int i = 1; i < columnCount + 1; i++) {
                    BeanUtils.setProperty(t, metaData.getColumnName(i).toLowerCase(), resultSet.getObject(i));
                }

                list.add(t);
            }

            //返回结果
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("=========================================================hbase维度查询时间为：" + (System.currentTimeMillis() - start));
        }
    }

    public static void main(String[] args) throws Exception {

//        System.out.println(queryList("select * from DIM_MD_CLIENT", DimMdClient.class));
//        System.out.println(queryList("select ROOM_CODE from DIM_MD_LOCATION where location_code='EC015-12'", DimMdLocation.class));
//        System.out.println(queryList("SELECT ARRIVAL_TEMPERATURE FROM  DIM_INB_ASN_HEADER  WHERE WAREHOUSE_CODE = 'SHZ_WT' AND ASN_CODE ='ASNWT20210902000002'", JsonObject.class));
//        System.out.println(queryList("select ITEM_CLASS_CODE from DIM_MD_SKU where client_code='SZ00001' and sku_code='4217971'", DimMdSku.class));
//        System.out.println(queryList("select ITEM_CLASS_CODE from DIM_MD_SKU where client_code='SZ00001' and sku_code='4217971'", DimMdSku.class));
//        System.out.println(queryList("select ASN_CODE,ASN_LINE_NO from DIM_INB_ASN_CONTAINER where warehouse_code='SHZ_WT' and client_code='SZ00001' and sku_code='4791546' and status='999' and pallet_code='2021081100041'", DwdInvTransaction.class));
        System.out.println(queryList("select ASN_CODE,ASN_LINE_NO from DIM_INB_ASN_CONTAINER where warehouse_code='SHZ_WT' and client_code='SZ00001' and sku_code='4769215' and status='999' and pallet_code='2021081900078'", DwdInvTransaction.class));
//        System.out.println(queryList("select ASN_CODE,ASN_LINE_NO from DIM_INB_ASN_CONTAINER where warehouse_code='SHZ_WT' and client_code='SZ00001' and sku_code='4791546' and status='310' and pallet_code='2021081100041'", DwdInvTransaction.class));

    }
}
