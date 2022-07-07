package com.vx.bean;

import lombok.Data;
import org.apache.flink.api.java.tuple.Tuple2;

import java.util.List;

/**
 * @version V1.0
 * @ClassName: BaseBean
 * @Description: TODO
 * @Author: xiehp
 * @Date: 2022/5/28 18:39
 */
@Data
public class BaseBean {

    /**
     * 数据库
     */
    @TransientSink
    private String db;

    /**
     * 表名
     */
    @TransientSink
    private String table;

    /**
     * 主键名
     */
    @TransientSink
    private String primaryKey;

    /**
     * 主键值
     */
    @TransientSink
    private String primaryKeyValue;

    /**
     * 操作
     */
    @TransientSink
    private String op;

    @TransientSink
    List<Tuple2<String, Object>> whereSqls;

}
