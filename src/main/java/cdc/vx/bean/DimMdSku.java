package cdc.vx.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品资料表
 * //@TableName md_sku
 */
//@TableName(value ="md_sku")
@Data
public class DimMdSku implements Serializable {
    /**
     * 主键ID
     */
    //@TableId(value = "md_sku_id", type = IdType.AUTO)
    private Long md_sku_id;

    /**
     * 货主代码
     */
    //@TableField(value = "client_code")
    private String client_code;

    /**
     * 商品编码
     */
    //@TableField(value = "sku_code")
    private String sku_code;

    /**
     * 商品名称
     */
    //@TableField(value = "sku_name")
    private String sku_name;

    /**
     * 商品简称
     */
    //@TableField(value = "sku_short_name")
    private String sku_short_name;

    /**
     * 货品规格
     */
    //@TableField(value = "sku_spec")
    private String sku_spec;

    /**
     * 商品条码
     */
    //@TableField(value = "bar_code")
    private String bar_code;

    /**
     * 商品辨识码
     */
    //@TableField(value = "voco_check_dig")
    private String voco_check_dig;

    /**
     * 描述
     */
    //@TableField(value = "description")
    private String description;

    /**
     * 最小计量单位，默认为‘EA’
     */
    //@TableField(value = "basic_uom")
    private String basic_uom;

    /**
     * 货品类别编码
     */
    //@TableField(value = "item_class_code")
    private String item_class_code;

    /**
     * 品牌名称
     */
    //@TableField(value = "brand_name")
    private String brand_name;

    /**
     * 尺码
     */
    //@TableField(value = "sku_size")
    private String sku_size;

    /**
     * 颜色
     */
    //@TableField(value = "sku_color")
    private String sku_color;

    /**
     * 款
     */
    //@TableField(value = "sku_style")
    private String sku_style;

    /**
     * 季节
     */
    //@TableField(value = "sku_season")
    private String sku_season;

    /**
     * 性别
     */
    //@TableField(value = "sku_gender")
    private String sku_gender;

    /**
     * 均净重
     */
    //@TableField(value = "net_weight")
    private BigDecimal net_weight;

    /**
     * 长
     */
    //@TableField(value = "length")
    private BigDecimal length;

    /**
     * 均毛重
     */
    //@TableField(value = "gross_weight")
    private BigDecimal gross_weight;

    /**
     * 宽
     */
    //@TableField(value = "width")
    private BigDecimal width;

    /**
     * 高
     */
    //@TableField(value = "height")
    private BigDecimal height;

    /**
     * 均皮重
     */
    //@TableField(value = "weight")
    private BigDecimal weight;

    /**
     * 均体积
     */
    //@TableField(value = "volume")
    private BigDecimal volume;

    /**
     * 包装规格
     */
    //@TableField(value = "pack_spec")
    private String pack_spec;

    /**
     * 包装规格编码
     */
    //@TableField(value = "pack_code")
    private String pack_code;

    /**
     * 货品ABC, 参照数据字典SKU_ABC
     */
    //@TableField(value = "sku_abc")
    private String sku_abc;

    /**
     * 温区
     */
    //@TableField(value = "temp_zone")
    private String temp_zone;

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
     * 是否批次管理
     */
    //@TableField(value = "is_using_batch")
    private Integer is_using_batch;

    /**
     * 是否启用保质期
     */
    //@TableField(value = "is_using_shelflife")
    private Integer is_using_shelflife;

    /**
     * 保质期验证字段, 10-生产日期，20-失效日期
     */
    //@TableField(value = "shelflife_validate")
    private Integer shelflife_validate;

    /**
     * 入库允许天数
     */
    //@TableField(value = "inb_days")
    private Integer inb_days;

    /**
     * YUM允收期天数
     */
    //@TableField(value = "yum_receipt_days")
    private Integer yum_receipt_days;

    /**
     * 生产日期正序校验
     */
    //@TableField(value = "is_using_mfg_valid")
    private Integer is_using_mfg_valid;

    /**
     * 收货效期控制(0-无,100-1/2效期提醒,200-1/2效期硬错误,300-1/3效期提醒,400-1/3效期硬错误)
     */
    //@TableField(value = "receive_mfg_control")
    private String receive_mfg_control;

    /**
     * 是否复核称重
     */
    //@TableField(value = "need_oqc_weight")
    private Integer need_oqc_weight;

    /**
     * 尺寸单位
     */
    //@TableField(value = "dimension_uom")
    private String dimension_uom;

    /**
     * 重量单位
     */
    //@TableField(value = "weight_uom")
    private String weight_uom;

    /**
     * 体积单位
     */
    //@TableField(value = "volume_uom")
    private String volume_uom;

    /**
     * 货品计费类别[NORMAL_SKU-普通, HEAVY_SKU-重货, BULK_SKU-泡货, NORMAL_TEMPERATURE-常温, LC_SKU-冷藏, LD_SKU-冷冻]
     */
    //@TableField(value = "sku_billing_category")
    private String sku_billing_category;

    /**
     * 临近效期
     */
    //@TableField(value = "near_validity")
    private String near_validity;

    /**
     * 保质期
     */
    //@TableField(value = "shelflife")
    private Long shelflife;

    /**
     * 仓库临保天数
     */
    //@TableField(value = "wh_rej_days")
    private Long wh_rej_days;

    /**
     * 门店允收天数
     */
    //@TableField(value = "store_rej_days")
    private Long store_rej_days;

    /**
     * 上游系统最小单位
     */
    //@TableField(value = "erp_min_uom")
    private String erp_min_uom;

    /**
     * 上游系统采购单位
     */
    //@TableField(value = "erp_po_uom")
    private String erp_po_uom;

    /**
     * 上游系统出货单位
     */
    //@TableField(value = "erp_sales_uom")
    private String erp_sales_uom;

    /**
     * 拆零单位
     */
    //@TableField(value = "tiny_unit")
    private String tiny_unit;

    /**
     * 上游系统品牌编码
     */
    //@TableField(value = "erp_brand_code")
    private String erp_brand_code;

    /**
     * 类别码
     */
    //@TableField(value = "kind_code")
    private String kind_code;

    /**
     * 商品分类
     */
    //@TableField(value = "sku_category")
    private String sku_category;

    /**
     * 栈板堆码
     */
    //@TableField(value = "pallet_stacking")
    private String pallet_stacking;

    /**
     * 采集日期方式
     */
    //@TableField(value = "shelflife_type")
    private String shelflife_type;

    /**
     * 补货规则
     */
    //@TableField(value = "replenish_rule")
    private String replenish_rule;

    /**
     * 是否放置拣货库位
     */
    //@TableField(value = "is_put_pickloc")
    private Integer is_put_pickloc;

    /**
     * 原产地
     */
    //@TableField(value = "country_of_origin")
    private String country_of_origin;

    /**
     * 策略代码
     */
    //@TableField(value = "strategy_code")
    private String strategy_code;

    /**
     * 批次周转
     */
    //@TableField(value = "lot_turnover_rule")
    private String lot_turnover_rule;

    /**
     * 库存周转
     */
    //@TableField(value = "inventory_turnover_rule")
    private String inventory_turnover_rule;

    /**
     * 是否启用机会性越库
     */
    //@TableField(value = "is_using_cross_dock")
    private Integer is_using_cross_dock;

    /**
     * 越库收货库位
     */
    //@TableField(value = "cross_location_code")
    private String cross_location_code;

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
     * 批属性，多个用逗号分隔
     */
    //@TableField(value = "return_batch")
    private String return_batch;

    /**
     * 动态拣货顺序(库位拣货顺序, 数量（由多到少）, 数量（由少到多）)
     */
    //@TableField(value = "dynamic_pick_order")
    private String dynamic_pick_order;

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
     * 品牌代码
     */
    //@TableField(value = "brand_code")
    private String brand_code;

    /**
     * 产地
     */
    //@TableField(value = "division")
    private String division;

    /**
     * 生产厂家
     */
    //@TableField(value = "department")
    private String department;

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
     * 成本价
     */
    //@TableField(value = "cost")
    private BigDecimal cost;

    /**
     * 零售价
     */
    //@TableField(value = "list_price")
    private BigDecimal list_price;

    /**
     * 成交价
     */
    //@TableField(value = "net_price")
    private BigDecimal net_price;

    /**
     * 上架类型
     */
    //@TableField(value = "putaway_type")
    private String putaway_type;

    /**
     * 默认分配组策略
     */
    //@TableField(value = "allocation_group")
    private String allocation_group;

    /**
     * 库存分配类型
     */
    //@TableField(value = "allocation_type")
    private String allocation_type;

    /**
     * 默认波次处理类型
     */
    //@TableField(value = "dflt_wave_proc_type")
    private String dflt_wave_proc_type;

    /**
     * 周转规则
     */
    //@TableField(value = "rotation_rule")
    private String rotation_rule;

    /**
     * 上架区
     */
    //@TableField(value = "putaway_zone")
    private String putaway_zone;

    /**
     * 上架库位
     */
    //@TableField(value = "putaway_loc")
    private String putaway_loc;

    /**
     * 默认上架策略
     */
    //@TableField(value = "putaway_rule")
    private String putaway_rule;

    /**
     * 温度要求
     */
    //@TableField(value = "temperature")
    private String temperature;

    /**
     * 可用率
     */
    //@TableField(value = "available_rate")
    private BigDecimal available_rate;

    /**
     * 可用容积
     */
    //@TableField(value = "usable_volume")
    private BigDecimal usable_volume;

    /**
     * 货号
     */
    //@TableField(value = "goods_code")
    private String goods_code;

    /**
     * 是否开启序列号管理
     */
    //@TableField(value = "is_sn_mgmt")
    private Integer is_sn_mgmt;

    /**
     * 是否有效
     */
    //@TableField(value = "is_active")
    private Integer is_active;

    /**
     * 备注
     */
    //@TableField(value = "remark")
    private String remark;

    /**
     * 用户自定义1
     */
    //@TableField(value = "user_def1")
    private String user_def1;

    /**
     * 用户自定义2
     */
    //@TableField(value = "user_def2")
    private String user_def2;

    /**
     * 用户自定义3
     */
    //@TableField(value = "user_def3")
    private String user_def3;

    /**
     * 用户自定义4
     */
    //@TableField(value = "user_def4")
    private String user_def4;

    /**
     * 用户自定义5
     */
    //@TableField(value = "user_def5")
    private String user_def5;

    /**
     * 用户自定义6
     */
    //@TableField(value = "user_def6")
    private String user_def6;

    /**
     * 用户自定义7
     */
    //@TableField(value = "user_def7")
    private String user_def7;

    /**
     * 用户自定义8
     */
    //@TableField(value = "user_def8")
    private String user_def8;

    /**
     * 用户自定义9
     */
    //@TableField(value = "user_def9")
    private String user_def9;

    /**
     * 用户自定义10
     */
    //@TableField(value = "user_def10")
    private String user_def10;

    /**
     * 删除标识(0-删除 1-未删除)
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
     * 创建时间
     */
    //@TableField(value = "created_dtm_loc")
    private String created_dtm_loc;

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
     * 更新时间
     */
    //@TableField(value = "updated_dtm_loc")
    private String updated_dtm_loc;

    /**
     * 记录版本
     */
    //@TableField(value = "record_version")
    private Long record_version;

    //@TableField(exist = false)
    private static final long serialVersionUID = 1L;
}