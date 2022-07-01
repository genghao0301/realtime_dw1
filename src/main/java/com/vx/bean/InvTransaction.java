package com.vx.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 库存交易表
 * @TableName inv_transaction
 */
//@TableName(value ="inv_transaction")
@Data
public class InvTransaction extends BaseBean implements Serializable {

    private String eventTimeDay;

    private BigDecimal eaNum;

    /**
     * 主键
     */
    //@TableId(value = "inv_transaction_id", type = IdType.AUTO)
    private Long inv_transaction_id;

    /**
     * 仓库ID
     */
    //@TableField(value = "warehouse_id")
    private Long warehouse_id;

    /**
     * 仓库编码
     */
    //@TableField(value = "warehouse_code")
    private String warehouse_code;

    /**
     * 交易类型
     */
    //@TableField(value = "action_type")
    private String action_type;

    /**
     * 交易流水号(UUID)
     */
    //@TableField(value = "transaction_no")
    private String transaction_no;

    /**
     * 调整类型，A-增加；S-减少；N-不增不减
     */
    //@TableField(value = "inv_adjustment_type")
    private String inv_adjustment_type;

    /**
     * 调整数量
     */
    //@TableField(value = "inv_adjustment_qty")
    private BigDecimal inv_adjustment_qty;

    /**
     * 单位
     */
    //@TableField(value = "qty_uom")
    private String qty_uom;

    /**
     * LPN号
     */
    //@TableField(value = "lpn_no")
    private String lpn_no;

    /**
     * 处理状态
     */
    //@TableField(value = "process_state")
    private Integer process_state;

    /**
     * 执行次数
     */
    //@TableField(value = "priority")
    private Integer priority;

    /**
     * 库位代码
     */
    //@TableField(value = "location_code")
    private String location_code;

    /**
     * 源库位代码
     */
    //@TableField(value = "from_location_code")
    private String from_location_code;

    /**
     * 货品编码
     */
    //@TableField(value = "item_code")
    private String item_code;

    /**
     * 货品名称
     */
    //@TableField(value = "item_name")
    private String item_name;

    /**
     * 货主代码
     */
    //@TableField(value = "client_code")
    private String client_code;

    /**
     * 货主名称
     */
    //@TableField(value = "client_name")
    private String client_name;

    /**
     * 供应商编码
     */
    //@TableField(value = "vendor_code")
    private String vendor_code;

    /**
     * 供应商名称
     */
    //@TableField(value = "vendor_name")
    private String vendor_name;

    /**
     * 品牌代码
     */
    //@TableField(value = "brand_code")
    private String brand_code;

    /**
     * 品牌名称
     */
    //@TableField(value = "brand_name")
    private String brand_name;

    /**
     * 库存类型
     */
    //@TableField(value = "inventory_type")
    private String inventory_type;

    /**
     * 批号
     */
    //@TableField(value = "batch_no")
    private String batch_no;

    /**
     * 残次标示,100正常
     */
    //@TableField(value = "inventory_quality")
    private String inventory_quality;

    /**
     * 原产国
     */
    //@TableField(value = "country_of_origin")
    private String country_of_origin;

    /**
     * 生产日期
     */
    //@TableField(value = "mfg_date")
    private String mfg_date;

    /**
     * 生产日期
     */
    //@TableField(value = "exp_date")
    private String exp_date;

    /**
     * 收货时间
     */
    //@TableField(value = "received_date")
    private String received_date;

    /**
     * PO单号
     */
    //@TableField(value = "po_no")
    private String po_no;

    /**
     * 销售档期号
     */
    //@TableField(value = "sales_no")
    private String sales_no;

    /**
     * 销售状态。0-不可售 1-可售
     */
    //@TableField(value = "sales_status")
    private String sales_status;

    /**
     * 装载该货品的容器的编码。空则填入*
     */
    //@TableField(value = "container_code")
    private String container_code;

    /**
     * 是否锁定。1-锁定 0-非锁定
     */
    //@TableField(value = "locked")
    private Integer locked;

    /**
     * 库存总数量
     */
    //@TableField(value = "on_hand_qty")
    private BigDecimal on_hand_qty;

    /**
     * 分配数量
     */
    //@TableField(value = "allocated_qty")
    private BigDecimal allocated_qty;

    /**
     * 超配数量
     */
    //@TableField(value = "in_transit_qty")
    private BigDecimal in_transit_qty;

    /**
     * 冻结数量
     */
    //@TableField(value = "frozen_qty")
    private BigDecimal frozen_qty;

    /**
     * 总净重
     */
    //@TableField(value = "total_net_weight")
    private BigDecimal total_net_weight;

    /**
     * 总毛重
     */
    //@TableField(value = "total_gross_weight")
    private BigDecimal total_gross_weight;

    /**
     * 总皮重
     */
    //@TableField(value = "total_weight")
    private BigDecimal total_weight;

    /**
     * 体积
     */
    //@TableField(value = "volume")
    private BigDecimal volume;

    /**
     * 批属性字段1
     */
    //@TableField(value = "lot_attr1")
    private String lot_attr1;

    /**
     * 批属性字段2
     */
    //@TableField(value = "lot_attr2")
    private String lot_attr2;

    /**
     * 批属性字段3
     */
    //@TableField(value = "lot_attr3")
    private String lot_attr3;

    /**
     * 批属性字段4
     */
    //@TableField(value = "lot_attr4")
    private String lot_attr4;

    /**
     * 批属性字段5
     */
    //@TableField(value = "lot_attr5")
    private String lot_attr5;

    /**
     * 批属性字段6
     */
    //@TableField(value = "lot_attr6")
    private String lot_attr6;

    /**
     * 批属性字段7
     */
    //@TableField(value = "lot_attr7")
    private String lot_attr7;

    /**
     * 批属性字段8
     */
    //@TableField(value = "lot_attr8")
    private String lot_attr8;

    /**
     * 批属性9
     */
    //@TableField(value = "lot_attr9")
    private String lot_attr9;

    /**
     * 批属性10
     */
    //@TableField(value = "lot_attr10")
    private String lot_attr10;

    /**
     * 批属性11
     */
    //@TableField(value = "lot_attr11")
    private String lot_attr11;

    /**
     * 批属性12
     */
    //@TableField(value = "lot_attr12")
    private String lot_attr12;

    /**
     * 批属性13
     */
    //@TableField(value = "lot_attr13")
    private String lot_attr13;

    /**
     * 批属性14
     */
    //@TableField(value = "lot_attr14")
    private String lot_attr14;

    /**
     * 批属性15
     */
    //@TableField(value = "lot_attr15")
    private String lot_attr15;

    /**
     * 批属性字段16
     */
    //@TableField(value = "lot_attr16")
    private String lot_attr16;

    /**
     * 单据号
     */
    //@TableField(value = "reference_no")
    private String reference_no;

    /**
     * 单据明细行号
     */
    //@TableField(value = "reference_line_no")
    private String reference_line_no;

    /**
     * 单据类型
     */
    //@TableField(value = "reference_type")
    private String reference_type;

    /**
     * 参考代码ID1
     */
    //@TableField(value = "ref_code_id_1")
    private String ref_code_id_1;

    /**
     * 参考值1
     */
    //@TableField(value = "ref_value_1")
    private String ref_value_1;

    /**
     * 参考代码ID2
     */
    //@TableField(value = "ref_code_id_2")
    private String ref_code_id_2;

    /**
     * 参考值2
     */
    //@TableField(value = "ref_value_2")
    private String ref_value_2;

    /**
     * 参考代码ID3
     */
    //@TableField(value = "ref_code_id_3")
    private String ref_code_id_3;

    /**
     * 参考值3
     */
    //@TableField(value = "ref_value_3")
    private String ref_value_3;

    /**
     * 参考代码ID4
     */
    //@TableField(value = "ref_code_id_4")
    private String ref_code_id_4;

    /**
     * 参考值4
     */
    //@TableField(value = "ref_value_4")
    private String ref_value_4;

    /**
     * 参考代码ID5
     */
    //@TableField(value = "ref_code_id_5")
    private String ref_code_id_5;

    /**
     * 参考值5
     */
    //@TableField(value = "ref_value_5")
    private String ref_value_5;

    /**
     * 参考代码ID6
     */
    //@TableField(value = "ref_code_id_6")
    private String ref_code_id_6;

    /**
     * 参考值6
     */
    //@TableField(value = "ref_value_6")
    private String ref_value_6;

    /**
     * 参考代码ID7
     */
    //@TableField(value = "ref_code_id_7")
    private String ref_code_id_7;

    /**
     * 参考值7
     */
    //@TableField(value = "ref_value_7")
    private String ref_value_7;

    /**
     * 参考代码ID8
     */
    //@TableField(value = "ref_code_id_8")
    private String ref_code_id_8;

    /**
     * 参考值8
     */
    //@TableField(value = "ref_value_8")
    private String ref_value_8;

    /**
     * 参考代码ID9
     */
    //@TableField(value = "ref_code_id_9")
    private String ref_code_id_9;

    /**
     * 参考值9
     */
    //@TableField(value = "ref_value_9")
    private String ref_value_9;

    /**
     * 参考代码ID10
     */
    //@TableField(value = "ref_code_id_10")
    private String ref_code_id_10;

    /**
     * 参考值10
     */
    //@TableField(value = "ref_value_10")
    private String ref_value_10;

    /**
     * 拥有者
     */
    //@TableField(value = "owner_code")
    private String owner_code;

    /**
     * 批次
     */
    //@TableField(value = "inv_lot_id")
    private Long inv_lot_id;

    /**
     * 库存表id
     */
    //@TableField(value = "inv_location_inventory_id")
    private Long inv_location_inventory_id;

    /**
     * 创建用户ID
     */
    //@TableField(value = "created_by_user_id")
    private Long created_by_user_id;

    /**
     * 创建用户
     */
    //@TableField(value = "created_by_user")
    private String created_by_user;

    /**
     * 创建地点
     */
    //@TableField(value = "created_office")
    private String created_office;

    /**
     * 创建时间
     */
    //@TableField(value = "created_dtm_loc")
    private String created_dtm_loc;

    /**
     * 创建时区
     */
    //@TableField(value = "created_time_zone")
    private String created_time_zone;

    /**
     * 更新用户ID
     */
    //@TableField(value = "updated_by_user_id")
    private Long updated_by_user_id;

    /**
     * 更新用户
     */
    //@TableField(value = "updated_by_user")
    private String updated_by_user;

    /**
     * 更新地点
     */
    //@TableField(value = "updated_office")
    private String updated_office;

    /**
     * 更新时间
     */
    //@TableField(value = "updated_dtm_loc")
    private String updated_dtm_loc;

    /**
     * 更新时区
     */
    //@TableField(value = "updated_time_zone")
    private String updated_time_zone;

    /**
     * 记录版本
     */
    //@TableField(value = "record_version")
    private Long record_version;

    //@TableField(exist = false)
    private static final long serialVersionUID = 1L;
}