package com.vx.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @TableName WH_MONTH_INVENTORY
 */
//@TableName(value ="WH_MONTH_INVENTORY")
@Data
public class WhMonthInventory implements Serializable {
    /**
     * 
     */
    //@TableId(value = "WMI_ID", type = IdType.AUTO)
    private Long WMI_ID;

    /**
     * 
     */
    //@TableField(value = "MONTH_CODE")
    private String MONTH_CODE;

    /**
     * 
     */
    //@TableField(value = "WH_CODE")
    private String WH_CODE;

    /**
     * 
     */
    //@TableField(value = "WH_COS_NAME")
    private String WH_COS_NAME;

    /**
     * 
     */
    //@TableField(value = "WH_NAME")
    private String WH_NAME;

    /**
     * 
     */
    //@TableField(value = "WH_TOTAL_PALLET_NUM")
    private BigDecimal WH_TOTAL_PALLET_NUM;

    /**
     * 
     */
    //@TableField(value = "SELF_USED_PALLET_NUM")
    private BigDecimal SELF_USED_PALLET_NUM;

    /**
     * 
     */
    //@TableField(value = "EXT_USED_PALLET_NUM")
    private BigDecimal EXT_USED_PALLET_NUM;

    /**
     * 
     */
    //@TableField(value = "UOM_NAME")
    private String UOM_NAME;

    /**
     * 
     */
    //@TableField(value = "STATUS")
    private Integer STATUS;

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