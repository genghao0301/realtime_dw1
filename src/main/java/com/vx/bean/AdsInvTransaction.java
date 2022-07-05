package com.vx.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName ads_inv_transaction
 */
@TableName(value ="ads_inv_transaction")
@Data
public class AdsInvTransaction extends BaseBean implements Serializable {
    /**
     * 
     */
    //@TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 仓库编码
     */
    //@TableField(value = "warehouse_code")
    private String warehouseCode;

    /**
     * 货主代码
     */
    //@TableField(value = "client_code")
    private String clientCode;

    /**
     * 货主名称
     */
    //@TableField(value = "client_name")
    private String clientName;

    /**
     * 单据类型
     */
    //@TableField(value = "reference_type")
    private String referenceType;

    /**
     * 统计日期
     */
    //@TableField(value = "event_time_day")
    private String eventTimeDay;

    /**
     * 交易类型
     */
    //@TableField(value = "event_time_day")
    private String actionType;

    /**
     * 调整数量
     */
    //@TableField(value = "ea_num")
    private double eaNum;

    /**
     * 调整托数
     */
    //@TableField(value = "pallet_num")
    private double palletNum;

    /**
     * 调整箱数
     */
    //@TableField(value = "cs_num")
    private double csNum;

    /**
     * 总皮重
     */
    //@TableField(value = "total_weight")
    private double totalWeight;

    /**
     * 总毛重
     */
    //@TableField(value = "total_gross_weight")
    private double totalGrossWeight;

    /**
     * 总净重
     */
    //@TableField(value = "total_net_weight")
    private double totalNetWeight;

    /**
     * 
     */
    //@TableField(value = "create_time")
    private Date createTime;

    /**
     * 
     */
    //@TableField(value = "update_time")
    private Date updateTime;

    //@TableField(exist = false)
    //private static final long serialVersionUID = 1L;
}