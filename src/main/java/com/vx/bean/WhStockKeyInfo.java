package com.vx.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 园区库房信息表
 * @TableName WH_STOCK_KEY_INFO
 */
@TableName(value ="WH_STOCK_KEY_INFO")
@Data
public class WhStockKeyInfo implements Serializable {
    /**
     * 自增id
     */
    //@TableId(value = "ID", type = IdType.AUTO)
    @TransientSink
    private Long ID;

    /**
     * 园区编号
     */
    //@TableField(value = "WH_CODE")
    private String WH_CODE;

    /**
     * WMS的对应园区编码
     */
    //@TableField(value = "WMS_WH_CODE")
    private String WMS_WH_CODE;

    /**
     * 园区名称
     */
    //@TableField(value = "WH_NAME")
    private String WH_NAME;

    /**
     * 库房编号
     */
    //@TableField(value = "WH_AREA_CODE")
    private String WH_AREA_CODE;

    /**
     * 库房名称
     */
    //@TableField(value = "WH_AREA_NAME")
    private String WH_AREA_NAME;

    /**
     * 温区：1：冷冻 2：冷藏 3：常温 4：恒温
     */
    //@TableField(value = "TEMPERATURE_ZONE")
    private String TEMPERATURE_ZONE;

    /**
     * 温度信息
     */
    //@TableField(value = "AREA_TEMPERATURE_INFO")
    private String AREA_TEMPERATURE_INFO;

    /**
     * 
     */
    //@TableField(value = "CREATE_TIME")
    private Date CREATE_TIME;

    /**
     * 
     */
    //@TableField(value = "CREATED_BY")
    private String CREATED_BY;

    /**
     * 
     */
    //@TableField(value = "UPDATE_TIME")
    private Date UPDATE_TIME;

    /**
     * 
     */
    //@TableField(value = "UPDATED_BY")
    private String UPDATED_BY;

}