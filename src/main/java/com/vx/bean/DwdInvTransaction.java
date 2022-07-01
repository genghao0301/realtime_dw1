package com.vx.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存交易表
 * @TableName inv_transaction
 */
//@TableName(value ="inv_transaction")
@Data
public class DwdInvTransaction extends InvTransaction {

    /**
     * 箱转换系数
     */
    private BigDecimal csNum = new BigDecimal(0);

    private String package_ratio_cs;

    /**
     * 托转换系数
     */
    private BigDecimal palletNum = new BigDecimal("0");

    private String package_ratio_pl;

    /*
     * 事件日期
     */
//    private String eventTimeDay;

    /*
     * 包装规格编码
     */
    private String pack_code;

}