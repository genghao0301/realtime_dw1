package com.vx.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 入库详情表
 * @TableName IN_ORDER_DETAIL
 */
@TableName(value ="IN_ORDER_DETAIL1")
//@TableName(value ="IN_ORDER_DETAIL")
@Data
public class InOrderDetail extends BaseBean implements Serializable {
    /**
     * 
     */
    //@TableId(value = "ID", type = IdType.AUTO)
    @TransientSink
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
     * 客户编号
     */
    //@TableField(value = "CLIENT_CODE")
    private String CLIENT_CODE;

    /**
     * 商品编号
     */
    //@TableField(value = "SKU_CODE")
    private String SKU_CODE;

    /**
     * 托盘号
     */
    //@TableField(value = "PALLET_CODE")
    private String PALLET_CODE;

    /**
     * 商品分类编号
     */
    //@TableField(value = "CLASS_CODE")
    private String CLASS_CODE;

    /**
     * 库位编号
     */
    //@TableField(value = "LOCATION_CODE")
    private String LOCATION_CODE;

    /**
     * 库房编号
     */
    //@TableField(value = "ROOM_CODE")
    private String ROOM_CODE;

    /**
     * 入库单号
     */
    //@TableField(value = "ASN_CODE")
    private String ASN_CODE;

    /**
     * 入库单明细行号
     */
    //@TableField(value = "ASN_LINE_NO")
    private Integer ASN_LINE_NO;

    /**
     * 入库状态（310上架，999取消，只需取310状态数据）
     */
    //@TableField(value = "ASN_STATUS")
    private String ASN_STATUS;

    /**
     * 温度
     */
    //@TableField(value = "TEMPERATURE")
    private String TEMPERATURE;

    /**
     * EA量
     */
    //@TableField(value = "ASN_EA")
    private BigDecimal ASN_EA;

    /**
     * 皮重
     */
    //@TableField(value = "WEIGHT")
    private BigDecimal WEIGHT;

    /**
     * 毛重
     */
    //@TableField(value = "GROSS_WEIGHT")
    private BigDecimal GROSS_WEIGHT;

    /**
     * 净重
     */
    //@TableField(value = "NET_WEIGHT")
    private BigDecimal NET_WEIGHT;

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

    /**
     * 程序执行时间
     */
    //@TableField(value = "ETL_TIME")
    private Date ETL_TIME;

}