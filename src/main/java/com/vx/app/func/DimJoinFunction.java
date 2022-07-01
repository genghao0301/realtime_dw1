package com.vx.app.func;

import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.java.tuple.Tuple2;

import java.util.List;

/**
 * @version V1.0
 * @Interface: DimJoinFunction
 * @Description: TODO
 * @Author: xiehp
 * @Date: 2022/4/24 17:52
 */
public interface DimJoinFunction<T> {

    //获取数据中的所要关联维度的主键
    String getKey(T input);

    //获取数据中的所要关联维度的主键
    List<Tuple2<String, String>> getCondition(T input);

    //关联事实数据和维度数据
    void join(T input, JSONObject dimInfo) throws Exception;

    void join(T input, List<JSONObject> dimInfo) throws Exception;

}
