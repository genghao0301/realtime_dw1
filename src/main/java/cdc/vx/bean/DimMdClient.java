package cdc.vx.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 货主资料表
 * //@TableName md_client
 */
//@TableName(value ="md_client")
@Data
public class DimMdClient implements Serializable {
    /**
     * 主键ID
     */
    //@TableId(value = "md_client_id", type = IdType.AUTO)
    private Long md_client_id;

    /**
     * 货主代码
     */
    //@TableField(value = "client_code")
    private String client_code;

    /**
     * 货主简称
     */
    //@TableField(value = "client_short_name")
    private String client_short_name;

    /**
     * 货主名称
     */
    //@TableField(value = "client_name")
    private String client_name;

    /**
     * 全局货主代码
     */
    //@TableField(value = "global_client_code")
    private String global_client_code;

    /**
     * 行业类型
     */
    //@TableField(value = "industry")
    private String industry;

    /**
     * 地址1
     */
    //@TableField(value = "address1")
    private String address1;

    /**
     * 地址2
     */
    //@TableField(value = "address2")
    private String address2;

    /**
     * 地址3
     */
    //@TableField(value = "address3")
    private String address3;

    /**
     * 地区
     */
    //@TableField(value = "region")
    private String region;

    /**
     * 城市
     */
    //@TableField(value = "city")
    private String city;

    /**
     * 省会
     */
    //@TableField(value = "province")
    private String province;

    /**
     * 国家
     */
    //@TableField(value = "country")
    private String country;

    /**
     * 邮编
     */
    //@TableField(value = "post_code")
    private String post_code;

    /**
     * 联系人
     */
    //@TableField(value = "attention_to")
    private String attention_to;

    /**
     * 手机
     */
    //@TableField(value = "mobile")
    private String mobile;

    /**
     * 电话
     */
    //@TableField(value = "tel")
    private String tel;

    /**
     * 分机号
     */
    //@TableField(value = "cell")
    private String cell;

    /**
     * 传真
     */
    //@TableField(value = "fax")
    private String fax;

    /**
     * 邮箱
     */
    //@TableField(value = "email")
    private String email;

    /**
     * 默认上架策略
     */
    //@TableField(value = "putaway_rule")
    private String putaway_rule;

    /**
     * 默认库位
     */
    //@TableField(value = "default_location_code")
    private String default_location_code;

    /**
     * 收货质检库位
     */
    //@TableField(value = "receive_qc_location_code")
    private String receive_qc_location_code;

    /**
     * 退货库位
     */
    //@TableField(value = "return_location_code")
    private String return_location_code;

    /**
     * 退货批属性，多个用逗号分隔
     */
    //@TableField(value = "return_batch")
    private String return_batch;

    /**
     * 动态拣货顺序(库位拣货顺序, 数量（由多到少）, 数量（由少到多）)
     */
    //@TableField(value = "dynamic_pick_order")
    private String dynamic_pick_order;

    /**
     * 按行收货标识
     */
    //@TableField(value = "receive_by_line_flag")
    private Integer receive_by_line_flag;

    /**
     * 供应商信用等级(高、中、低)
     */
    //@TableField(value = "vendor_credit_level")
    private String vendor_credit_level;

    /**
     * 启用禁用[0-禁用,1-启用]
     */
    //@TableField(value = "enabled")
    private Integer enabled;

    /**
     * 是否越库
     */
    //@TableField(value = "is_cross_docking")
    private Integer is_cross_docking;

    /**
     * 越库分配规则
     */
    //@TableField(value = "cross_allot_rule")
    private String cross_allot_rule;

    /**
     * 是否关闭收货单时执行分配
     */
    //@TableField(value = "is_close_allot")
    private Integer is_close_allot;

    /**
     * 是否创建拣货任务
     */
    //@TableField(value = "is_create_task")
    private Integer is_create_task;

    /**
     * 是否打印越库标签
     */
    //@TableField(value = "is_print_cross_tag")
    private Integer is_print_cross_tag;

    /**
     * 是否机会性越库
     */
    //@TableField(value = "is_chance_cross_docking")
    private Integer is_chance_cross_docking;

    /**
     * 收货分配周转库位
     */
    //@TableField(value = "receive_allot_location")
    private String receive_allot_location;

    /**
     * 出库待运库位
     */
    //@TableField(value = "out_ready_location")
    private String out_ready_location;

    /**
     * 是否允许部分分配
     */
    //@TableField(value = "is_part_allot")
    private Integer is_part_allot;

    /**
     * 单次分配的订单数
     */
    //@TableField(value = "single_allot_qty")
    private BigDecimal single_allot_qty;

    /**
     * 不含机会性越库的订单类型
     */
    //@TableField(value = "cross_order_type")
    private String cross_order_type;

    /**
     * 计划交货时间减去当前天数
     */
    //@TableField(value = "subtract_days")
    private Long subtract_days;

    /**
     * 计划交货时间加上当前天数
     */
    //@TableField(value = "plus_days")
    private Long plus_days;

    /**
     * 订单分配顺序[1订单号(小到大),2订单号(大到小),3优先级(小到大),4优先级(大到小),5订单创建时间(小到大),6订单创建时间(大到小),7计划出库时间(小到大),8计划出库时间(大到小)]
     */
    //@TableField(value = "order_allocated_type")
    private Integer order_allocated_type;

    /**
     * 补货规则
     */
    //@TableField(value = "replenish_rule")
    private String replenish_rule;

    /**
     * 分配后是否播种
     */
    //@TableField(value = "is_allot_seeding")
    private Integer is_allot_seeding;

    /**
     * 是否手动分配
     */
    //@TableField(value = "is_hand_allot")
    private Integer is_hand_allot;

    /**
     * 严格按SO单位分配（0关闭；1开启）
     */
    //@TableField(value = "allot_so_uom_flag")
    private Integer allot_so_uom_flag;

    /**
     * 是否开启单据校验（0关闭；1开启）
     */
    //@TableField(value = "order_check_flag")
    private Integer order_check_flag;

    /**
     * 批属性规则编号
     */
    //@TableField(value = "attr_rule_code")
    private String attr_rule_code;

    /**
     * 批属性验证名称
     */
    //@TableField(value = "attr_rule_name")
    private String attr_rule_name;

    /**
     * 收货验证配置编号
     */
    //@TableField(value = "receive_config_code")
    private String receive_config_code;

    /**
     * 允许托盘重复收货
     */
    //@TableField(value = "allow_pallet_repeat_receive")
    private Integer allow_pallet_repeat_receive;

    /**
     * 允许落放托盘重复收货
     */
    //@TableField(value = "allow_put_pallet_repeat_receive")
    private Integer allow_put_pallet_repeat_receive;

    /**
     * 是否启用索证索票
     */
    //@TableField(value = "is_using_ticket")
    private Integer is_using_ticket;

    /**
     * 是否启用采样拒收
     */
    //@TableField(value = "is_using_qc_sample_batch")
    private Integer is_using_qc_sample_batch;

    /**
     * 备注
     */
    //@TableField(value = "remark")
    private String remark;

    /**
     * 允许重复推荐上架策略
     */
    //@TableField(value = "allow_repeat_recommend")
    private Integer allow_repeat_recommend;

    /**
     * 是否生成上架任务
     */
    //@TableField(value = "allow_create_task")
    private Integer allow_create_task;

    /**
     * 货主库存冻结设置<=托数
     */
    //@TableField(value = "inv_frozen_pallet_size")
    private Integer inv_frozen_pallet_size;

    /**
     * 拣货必选（0否；1是）
     */
    //@TableField(value = "pick_chosen_flag")
    private Integer pick_chosen_flag;

    /**
     * 复核必选（0否；1是）
     */
    //@TableField(value = "check_chosen_flag")
    private Integer check_chosen_flag;

    /**
     * 集货必选（0否；1是）
     */
    //@TableField(value = "store_chosen_flag")
    private Integer store_chosen_flag;

    /**
     * 装车必选（0否；1是）
     */
    //@TableField(value = "truck_chosen_flag")
    private Integer truck_chosen_flag;

    /**
     * 是否允许短复核（0否；1是）
     */
    //@TableField(value = "short_check_flag")
    private Integer short_check_flag;

    /**
     * 是否允许缺货发运（0否；1是）
     */
    //@TableField(value = "short_ship_flag")
    private Integer short_ship_flag;

    /**
     * 发运后是否关单
     */
    //@TableField(value = "close_after_ship_flag")
    private Integer close_after_ship_flag;

    /**
     * 调拨是否同步托盘号
     */
    //@TableField(value = "transfer_synchro_lpn_flag")
    private Integer transfer_synchro_lpn_flag;

    /**
     * 箱码采集校验位数，默认为0，0标识不校验
     */
    //@TableField(value = "boxcode_num_verify")
    private Integer boxcode_num_verify;

    /**
     * 箱码采集允许采集重复箱号（0允许；1不允许；2RF提示）
     */
    //@TableField(value = "boxcode_repeat_collection_type")
    private Integer boxcode_repeat_collection_type;

    /**
     * 箱码采集是否采集重量
     */
    //@TableField(value = "boxcode_collect_weight_flag")
    private Integer boxcode_collect_weight_flag;

    /**
     * 出库单超配比例
     */
    //@TableField(value = "over_allocation_ratio")
    private BigDecimal over_allocation_ratio;

    /**
     * 货品允许混放LPN
     */
    //@TableField(value = "sku_allow_mixed_lpn_flag")
    private Integer sku_allow_mixed_lpn_flag;

    /**
     * 拣货完成后打印托盘标签
     */
    //@TableField(value = "print_after_picking_flag")
    private Integer print_after_picking_flag;

    /**
     * 货值低于AR时锁定库存：1是；0否（默认）
     */
    //@TableField(value = "ar_stock_lock_flag")
    private Integer ar_stock_lock_flag;

    /**
     * ASN生成上架前数据分析(0关闭；1开启)
     */
    //@TableField(value = "putaway_before_analysis_flag")
    private Integer putaway_before_analysis_flag;

    /**
     * 混合托盘上架策略
     */
    //@TableField(value = "mixed_pallet_putaway_rule")
    private String mixed_pallet_putaway_rule;

    /**
     * 使用混合托盘最大数量的货品的上架策略
     */
    //@TableField(value = "mixed_pallet_max_sku_flag")
    private Integer mixed_pallet_max_sku_flag;

    /**
     * 装车后是否提示发运配置(0关闭；1开启)
     */
    //@TableField(value = "tips_ship_after_load")
    private Integer tips_ship_after_load;

    /**
     * 小程序模板编号
     */
    //@TableField(value = "wx_template_code")
    private String wx_template_code;

    /**
     * RF收货时的上架任务
     */
    //@TableField(value = "receive_putaway_flag")
    private String receive_putaway_flag;

    /**
     * 计算上架库位
     */
    //@TableField(value = "calc_location_flag")
    private String calc_location_flag;

    /**
     * 发运校验出库箱是否采集
     */
    //@TableField(value = "check_out_box")
    private String check_out_box;

    /**
     * 装车是否确认数量
     */
    //@TableField(value = "confirm_qty_of_load")
    private Integer confirm_qty_of_load;

    /**
     * 取消收货是否保留上架任务
     */
    //@TableField(value = "cancel_receive_retain_task")
    private Integer cancel_receive_retain_task;

    /**
     * 外部单号是否允许重复，0不允许，1允许
     */
    //@TableField(value = "externa_no_repeat_flag")
    private Integer externa_no_repeat_flag;

    /**
     * RF控制策略编号
     */
    //@TableField(value = "rf_strategy_code")
    private String rf_strategy_code;

    /**
     * 当日出库是否计算仓储费，0不计算，1计算
     */
    //@TableField(value = "calc_outbound_fee_flag")
    private Integer calc_outbound_fee_flag;

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
     * 开票名称
     */
    //@TableField(value = "billing_name")
    private String billing_name;

    /**
     * 税号
     */
    //@TableField(value = "tax_number")
    private String tax_number;

    /**
     * 单位地址
     */
    //@TableField(value = "unit_address")
    private String unit_address;

    /**
     * 电话
     */
    //@TableField(value = "phone_number")
    private String phone_number;

    /**
     * 开户银行
     */
    //@TableField(value = "bank_of_deposit")
    private String bank_of_deposit;

    /**
     * 银行账号
     */
    //@TableField(value = "bank_account")
    private String bank_account;

    /**
     * 用户自定义字段1
     */
    //@TableField(value = "user_def1")
    private String user_def1;

    /**
     * 用户自定义字段2
     */
    //@TableField(value = "user_def2")
    private String user_def2;

    /**
     * 用户自定义字段3
     */
    //@TableField(value = "user_def3")
    private String user_def3;

    /**
     * 用户自定义字段4
     */
    //@TableField(value = "user_def4")
    private String user_def4;

    /**
     * 用户自定义字段5
     */
    //@TableField(value = "user_def5")
    private String user_def5;

    /**
     * 用户自定义字段6
     */
    //@TableField(value = "user_def6")
    private String user_def6;

    /**
     * 用户自定义字段7
     */
    //@TableField(value = "user_def7")
    private String user_def7;

    /**
     * 用户自定义字段8
     */
    //@TableField(value = "user_def8")
    private String user_def8;

    /**
     * 用户自定义字段9
     */
    //@TableField(value = "user_def9")
    private String user_def9;

    /**
     * 用户自定义字段10
     */
    //@TableField(value = "user_def10")
    private String user_def10;

    /**
     * 是否有效
     */
    //@TableField(value = "valid")
    private Integer valid;

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