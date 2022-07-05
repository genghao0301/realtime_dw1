package com.vx.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品品类表
 * @TableName SKU_CLASS
 */
//@TableName(value ="SKU_CLASS")
@Data
public class SkuClass implements Serializable {
    /**
     * 自增id
     */
    //@TableId(value = "ID", type = IdType.AUTO)
    private Long ID;

    /**
     * DMP园区编号
     */
    //@TableField(value = "WH_CODE")
    private String WH_CODE;

    /**
     * WMS园区编号
     */
    //@TableField(value = "WMS_WH_CODE")
    private String WMS_WH_CODE;

    /**
     * 类别编号
     */
    //@TableField(value = "CLASS_CODE")
    private String CLASS_CODE;

    /**
     * 类别名称
     */
    //@TableField(value = "CLASS_NAME")
    private String CLASS_NAME;

    /**
     * 
     */
    //@TableField(value = "CREATE_TIME")
    private Date CREATE_TIME;

    /**
     * 
     */
    //@TableField(value = "UPDATE_TIME")
    private Date UPDATE_TIME;

}