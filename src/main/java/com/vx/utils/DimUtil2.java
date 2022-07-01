package com.vx.utils;

import cdc.vx.bean.DimMdClient;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.CollectionUtil;
import redis.clients.jedis.Jedis;

import java.util.List;


/**
 * select * from t where id='19' and name='zhangsan';
 * <p>
 * Redis:
 * 1.存什么数据？         维度数据   JsonStr
 * 2.用什么类型？         String  Set  Hash
 * 3.RedisKey的设计？     String：tableName+id  Set:tableName  Hash:tableName
 * t:19:zhangsan
 * <p>
 * 集合方式排除,原因在于我们需要对每条独立的维度数据设置过期时间
 */
public class DimUtil2 {

    public static <T> List<T> getDimInfo(String tableName, Class<T> classz, String columnNames, Tuple2<String, String>... conditions) {

        long start = System.currentTimeMillis();

        try {
            if (conditions.length <= 0) {
                throw new RuntimeException("查询维度数据时,请至少设置一个查询条件！");
            }
            //创建Phoenix Where子句
            StringBuilder whereSql = new StringBuilder(" where ");

            //创建RedisKey
            StringBuilder redisKey = new StringBuilder(tableName).append(":");

            //遍历查询条件并赋值whereSql
            for (int i = 0; i < conditions.length; i++) {
                //获取单个查询条件
                Tuple2<String, String> columnValue = conditions[i];

                String column = columnValue.f0;
                String value = columnValue.f1;
                whereSql.append(column).append("='").append(value).append("'");

                redisKey.append(value);

                //判断如果不是最后一个条件,则添加"and"
                if (i < conditions.length - 1) {
                    whereSql.append(" and ");
                    redisKey.append(":");
                }
            }

            //获取Redis连接
            Jedis jedis = RedisUtil.getJedis();
            String dimJsonStr = jedis.get(redisKey.toString());
            //判断是否从Redis中查询到数据
            if (dimJsonStr != null && dimJsonStr.length() > 0) {
                jedis.close();
                return JSON.parseArray(dimJsonStr, classz);
            }

            StringBuilder querySql = new StringBuilder("select ");
            //遍历查询字段
            if (StringUtils.isNotBlank(columnNames)) {
                querySql.append(columnNames);
            } else {
                querySql.append(" * ");
            }


            //拼接SQL
            querySql = querySql.append(" from ").append(tableName).append(whereSql);
            System.out.println(querySql);

            //查询Phoenix中的维度数据
            List<T> queryList = null;
            // 重试3次
            for (int i=0; i<3; i++) {
                try {
                    queryList = PhoenixUtil.queryList(querySql.toString(), classz);
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //将数据写入Redis
            if (queryList != null && queryList.size() > 0) {
                jedis.set(redisKey.toString(), JSON.toJSONString(queryList));
                jedis.expire(redisKey.toString(), 24 * 60 * 60);
                jedis.close();
            }
            //返回结果
            return queryList;
        } finally {
            System.out.println("========================================================="+tableName+"维度查询时间为：" + (System.currentTimeMillis() - start));
        }
    }

    public static <T> List<T> getDimInfo(String tableName, Class<T> classz, String columnNames, List<Tuple2<String, String>> columnValues) {
        return getDimInfo(tableName, classz, columnNames, columnValues.toArray(new Tuple2[columnValues.size()]));
    }

    public static <T> T getOneDimInfo(String tableName, Class<T> classz, String columnNames, Tuple2<String, String>... columnValues) {
        List<T> list  = getDimInfo(tableName, classz,columnNames, columnValues);
        return list == null ? null : list.get(0);
    }

    public static <T> T getOneDimInfo(String tableName, Class<T> classz, String columnNames, List<Tuple2<String, String>> columnValues) {
        List<T> list = getDimInfo(tableName, classz,columnNames, columnValues);
        return list == null ? null : list.get(0);
    }

    public static <T> List<T> getDimInfo(String tableName, String value, Class<T> classz) {
        return getDimInfo(tableName, classz,null, new Tuple2<>("id", value));
    }

    //根据key让Redis中的缓存失效
    public static void deleteCached(String tableName, String id) {
        String key = tableName.toUpperCase() + ":" + id;
        try {
            Jedis jedis = RedisUtil.getJedis();
            // 通过key清除缓存
            jedis.del(key);
            jedis.close();
        } catch (Exception e) {
            System.out.println("缓存异常！");
            e.printStackTrace();
        }
    }

    public static <T> List<T> getDimInfoFromMySql(String tableName, Class<T> classz, Tuple2<String, String>... columnValues) {

        long start = System.currentTimeMillis();

        if (columnValues.length <= 0) {
            throw new RuntimeException("查询维度数据时,请至少设置一个查询条件！");
        }

        //创建Phoenix Where子句
        StringBuilder whereSql = new StringBuilder(" where ");

        //创建RedisKey
        StringBuilder redisKey = new StringBuilder(tableName).append(":");

        //遍历查询条件并赋值whereSql
        for (int i = 0; i < columnValues.length; i++) {
            //获取单个查询条件
            Tuple2<String, String> columnValue = columnValues[i];

            String column = columnValue.f0;
            String value = columnValue.f1;
            whereSql.append(column).append("='").append(value).append("'");

            redisKey.append(value);

            //判断如果不是最后一个条件,则添加"and"
            if (i < columnValues.length - 1) {
                whereSql.append(" and ");
                redisKey.append(":");
            }
        }

        //获取Redis连接
        Jedis jedis = RedisUtil.getJedis();
        String dimJsonStr = jedis.get(redisKey.toString());
        //判断是否从Redis中查询到数据
        if (dimJsonStr != null && dimJsonStr.length() > 0) {
            jedis.close();
            return JSON.parseArray(dimJsonStr, classz);
        }

        //拼接SQL
        String querySql = "select * from " + tableName + whereSql.toString();
        System.out.println(querySql);

        //查询Phoenix中的维度数据
        List<T> queryList = null;
        // 重试3次
        for (int i=0; i<3; i++) {
            try {
                queryList = MySQLUtil.queryList(querySql, classz, false);
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //将数据写入Redis
        if (queryList != null && queryList.size() > 0) {
            jedis.set(redisKey.toString(), JSON.toJSONString(queryList));
            jedis.expire(redisKey.toString(), 24 * 60 * 60);
            jedis.close();
        }
        System.out.println("========================================================="+tableName+"维度查询时间为：" + (System.currentTimeMillis() - start));
        //返回结果
        return queryList;
    }

    public static <T> List<T> getDimInfoFromMySql(String tableName, Class<T> classz, List<Tuple2<String, String>> columnValues) {
        return getDimInfoFromMySql(tableName, classz, columnValues.toArray(new Tuple2[columnValues.size()]));
    }

    public static <T> T getOneDimInfoFromMySql(String tableName, Class<T> classz, Tuple2<String, String>... columnValues) {
        List<T> list  = getDimInfoFromMySql(tableName, classz, columnValues);
        return list == null ? null : list.get(0);
    }

    public static <T> T getOneDimInfoFromMySql(String tableName, Class<T> classz, List<Tuple2<String, String>> columnValues) {
        List<T> list = getDimInfoFromMySql(tableName, classz, columnValues);
        return list == null ? null : list.get(0);
    }

    public static <T> List<T> getDimInfoFromMySql(String tableName, String value, Class<T> classz) {
        return getDimInfoFromMySql(tableName, classz, new Tuple2<>("MD_CLIENT_id", value));
    }

    public static void main(String[] args) {
        System.out.println(getDimInfoFromMySql("MD_CLIENT", "10", DimMdClient.class));
    }

}
