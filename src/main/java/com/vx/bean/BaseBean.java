package com.vx.bean;

import lombok.Data;

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
    private String db;

    /**
     * 表名
     */
    private String table;

    /**
     * 表名
     */
    private String primaryKey;

    /**
     * 操作
     */
    private String op;

}
