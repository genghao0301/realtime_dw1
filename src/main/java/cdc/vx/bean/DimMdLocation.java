package cdc.vx.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 库位表
 * //@TableName md_location
 */
//@TableName(value ="md_location")
@Data
public class DimMdLocation implements Serializable {
    /**
     * 主键ID
     */
    //@TableId(value = "md_location_id", type = IdType.AUTO)
    private Long md_location_id;

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
     * 区域代码
     */
    //@TableField(value = "area_code")
    private String area_code;

    /**
     * 库区所属的库房
     */
    //@TableField(value = "room_code")
    private String room_code;

    /**
     * 库位条码
     */
    //@TableField(value = "location_code")
    private String location_code;

    /**
     * 库位名称
     */
    //@TableField(value = "location_name")
    private String location_name;

    /**
     * 货位验证码
     */
    //@TableField(value = "check_dig")
    private String check_dig;

    /**
     * 所属项目
     */
    //@TableField(value = "location_site")
    private String location_site;

    /**
     * 货位属性
     */
    //@TableField(value = "location_class_code")
    private String location_class_code;

    /**
     * 货位类型
     */
    //@TableField(value = "location_type_code")
    private String location_type_code;

    /**
     * 货品存放单位（件/箱/托）
     */
    //@TableField(value = "storage_uom")
    private String storage_uom;

    /**
     * 货架类型
     */
    //@TableField(value = "pack_type")
    private String pack_type;

    /**
     * 周转频率
     */
    //@TableField(value = "turnover_rate")
    private String turnover_rate;

    /**
     * 长
     */
    //@TableField(value = "length")
    private BigDecimal length;

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
     * 通道
     */
    //@TableField(value = "aisle")
    private String aisle;

    /**
     * 货架
     */
    //@TableField(value = "pack")
    private String pack;

    /**
     * 层数
     */
    //@TableField(value = "level")
    private String level;

    /**
     * 深度
     */
    //@TableField(value = "deepth")
    private Integer deepth;

    /**
     * X坐标
     */
    //@TableField(value = "axis_x")
    private Integer axis_x;

    /**
     * y坐标
     */
    //@TableField(value = "axis_y")
    private Integer axis_y;

    /**
     * z坐标
     */
    //@TableField(value = "axis_z")
    private Integer axis_z;

    /**
     * 承重限制
     */
    //@TableField(value = "weight_limit")
    private BigDecimal weight_limit;

    /**
     * 高度限制
     */
    //@TableField(value = "height_limit")
    private BigDecimal height_limit;

    /**
     * 体积限制
     */
    //@TableField(value = "cube_limit")
    private BigDecimal cube_limit;

    /**
     * 拣货顺序号
     */
    //@TableField(value = "picking_seq_num")
    private Integer picking_seq_num;

    /**
     * 上架顺序号
     */
    //@TableField(value = "putaway_seq_num")
    private Integer putaway_seq_num;

    /**
     * 盘点顺序号
     */
    //@TableField(value = "stocktake_seq_num")
    private Integer stocktake_seq_num;

    /**
     * 上架库区
     */
    //@TableField(value = "locating_zone_code")
    private String locating_zone_code;

    /**
     * 分配库区
     */
    //@TableField(value = "allocation_zone_code")
    private String allocation_zone_code;

    /**
     * 工作库区代码
     */
    //@TableField(value = "work_zone_code")
    private String work_zone_code;

    /**
     * 集货库区代码
     */
    //@TableField(value = "gather_zone_code")
    private String gather_zone_code;

    /**
     * 语音验证码
     */
    //@TableField(value = "voco_check_dig")
    private String voco_check_dig;

    /**
     * 周转规则
     */
    //@TableField(value = "turnover_rule")
    private String turnover_rule;

    /**
     * 库位ABC, 参照数据字典LOCATION_ABC
     */
    //@TableField(value = "location_abc")
    private String location_abc;

    /**
     * 货位模板
     */
    //@TableField(value = "location_template")
    private String location_template;

    /**
     * 尺寸单位
     */
    //@TableField(value = "dimension_uom")
    private String dimension_uom;

    /**
     * 最大重量
     */
    //@TableField(value = "maximum_weight")
    private BigDecimal maximum_weight;

    /**
     * 重量单位
     */
    //@TableField(value = "weight_uom")
    private String weight_uom;

    /**
     * 体积
     */
    //@TableField(value = "volume")
    private BigDecimal volume;

    /**
     * 体积单位
     */
    //@TableField(value = "volume_uom")
    private String volume_uom;

    /**
     * 容量大小等级
     */
    //@TableField(value = "capacity_level")
    private Long capacity_level;

    /**
     * 货位状态
     */
    //@TableField(value = "status_code")
    private Integer status_code;

    /**
     * 允许库位内一个货品最多有多少个批号
     */
    //@TableField(value = "max_lot_count")
    private Integer max_lot_count;

    /**
     * 当前批号数
     */
    //@TableField(value = "current_lot_count")
    private Integer current_lot_count;

    /**
     * 允许存放SKU种类数
     */
    //@TableField(value = "max_sku_count")
    private Integer max_sku_count;

    /**
     * 当前SKU种类数
     */
    //@TableField(value = "current_sku_count")
    private Integer current_sku_count;

    /**
     * 允许存放商品件数
     */
    //@TableField(value = "max_sku_qty")
    private Integer max_sku_qty;

    /**
     * 当前存放商品件数
     */
    //@TableField(value = "current_sku_qty")
    private Integer current_sku_qty;

    /**
     * 允许存放的箱数
     */
    //@TableField(value = "max_lpn_count")
    private Integer max_lpn_count;

    /**
     * 当前存放的箱数 
     */
    //@TableField(value = "current_lpn_count")
    private Integer current_lpn_count;

    /**
     * 推荐托盘数
     */
    //@TableField(value = "recommend_lpn_count")
    private Integer recommend_lpn_count;

    /**
     * 是否上传库存信息到其它系统
     */
    //@TableField(value = "is_upload")
    private Integer is_upload;

    /**
     * 是否允许多个LPN（一个库位多箱）
     */
    //@TableField(value = "is_multi_lpn")
    private Integer is_multi_lpn;

    /**
     * 是否允许多个SKU（混放）
     */
    //@TableField(value = "is_multi_item")
    private Integer is_multi_item;

    /**
     * 是否混放Lot
     */
    //@TableField(value = "is_mixed_lot")
    private Integer is_mixed_lot;

    /**
     * 混批号
     */
    //@TableField(value = "batch_no_flag")
    private Integer batch_no_flag;

    /**
     * 混残次标示
     */
    //@TableField(value = "inventory_quality_flag")
    private Integer inventory_quality_flag;

    /**
     * 混原产国
     */
    //@TableField(value = "country_of_origin_flag")
    private Integer country_of_origin_flag;

    /**
     * 混生产日期
     */
    //@TableField(value = "mfg_date_flag")
    private Integer mfg_date_flag;

    /**
     * 混失效日期
     */
    //@TableField(value = "exp_date_flag")
    private Integer exp_date_flag;

    /**
     * 混批属性字段1
     */
    //@TableField(value = "lot_attr1_flag")
    private Integer lot_attr1_flag;

    /**
     * 混批属性字段2
     */
    //@TableField(value = "lot_attr2_flag")
    private Integer lot_attr2_flag;

    /**
     * 混批属性字段3
     */
    //@TableField(value = "lot_attr3_flag")
    private Integer lot_attr3_flag;

    /**
     * 混批属性字段4
     */
    //@TableField(value = "lot_attr4_flag")
    private Integer lot_attr4_flag;

    /**
     * 混批属性字段5
     */
    //@TableField(value = "lot_attr5_flag")
    private Integer lot_attr5_flag;

    /**
     * 混批属性字段6
     */
    //@TableField(value = "lot_attr6_flag")
    private Integer lot_attr6_flag;

    /**
     * 混批属性字段7
     */
    //@TableField(value = "lot_attr7_flag")
    private Integer lot_attr7_flag;

    /**
     * 混批属性字段8
     */
    //@TableField(value = "lot_attr8_flag")
    private Integer lot_attr8_flag;

    /**
     * 混批属性9
     */
    //@TableField(value = "lot_attr9_flag")
    private Integer lot_attr9_flag;

    /**
     * 混批属性10
     */
    //@TableField(value = "lot_attr10_flag")
    private Integer lot_attr10_flag;

    /**
     * 混批属性11
     */
    //@TableField(value = "lot_attr11_flag")
    private Integer lot_attr11_flag;

    /**
     * 混批属性12
     */
    //@TableField(value = "lot_attr12_flag")
    private Integer lot_attr12_flag;

    /**
     * 混批属性字段13
     */
    //@TableField(value = "lot_attr13_flag")
    private Integer lot_attr13_flag;

    /**
     * 混批属性字段14
     */
    //@TableField(value = "lot_attr14_flag")
    private Integer lot_attr14_flag;

    /**
     * 混批属性字段15
     */
    //@TableField(value = "lot_attr15_flag")
    private Integer lot_attr15_flag;

    /**
     * 混批属性字段16
     */
    //@TableField(value = "lot_attr16_flag")
    private Integer lot_attr16_flag;

    /**
     * 是否跟踪集装箱（是否记录LPN号）
     */
    //@TableField(value = "is_track_container")
    private Integer is_track_container;

    /**
     * 补货时是否考虑此货位
     */
    //@TableField(value = "is_rpln_evaluation")
    private Integer is_rpln_evaluation;

    /**
     * 是否考虑实时补货
     */
    //@TableField(value = "is_real_time_rpln")
    private Integer is_real_time_rpln;

    /**
     * 库位是否有效。0-无效  1-有效。
     */
    //@TableField(value = "is_active")
    private Integer is_active;

    /**
     * 货位是否满了
     */
    //@TableField(value = "is_full")
    private Integer is_full;

    /**
     * 是否可以上输送线
     */
    //@TableField(value = "is_sortable")
    private Integer is_sortable;

    /**
     * 库位是否冻结
     */
    //@TableField(value = "is_freeze")
    private Integer is_freeze;

    /**
     * 是否追踪托盘
     */
    //@TableField(value = "is_track_pallet")
    private Integer is_track_pallet;

    /**
     * 是否允许整托拣货
     */
    //@TableField(value = "is_pallet_pick_allowed")
    private Integer is_pallet_pick_allowed;

    /**
     * 是否允许整箱拣货
     */
    //@TableField(value = "is_case_pick_allowed")
    private Integer is_case_pick_allowed;

    /**
     * 是否允许逐件拣货
     */
    //@TableField(value = "is_each_pick_allowed")
    private Integer is_each_pick_allowed;

    /**
     * 是否正在上架
     */
    //@TableField(value = "is_putaway")
    private Integer is_putaway;

    /**
     * 冻结日期
     */
    //@TableField(value = "freeze_date")
    private String freeze_date;

    /**
     * 库位冻结操作用户
     */
    //@TableField(value = "freeze_by_user")
    private String freeze_by_user;

    /**
     * 最后盘点日期
     */
    //@TableField(value = "last_stocktake_date")
    private String last_stocktake_date;

    /**
     * 最后盘点用户
     */
    //@TableField(value = "last_stocktake_by_user")
    private String last_stocktake_by_user;

    /**
     * 集货批次号
     */
    //@TableField(value = "dock_no")
    private String dock_no;

    /**
     * 收货暂存位占用情况(0不占用；1占用)
     */
    //@TableField(value = "occupancy_state")
    private Integer occupancy_state;

    /**
     * 周期盘点级别
     */
    //@TableField(value = "cycle_check_level")
    private String cycle_check_level;

    /**
     * 上次库位盘点发布日期
     */
    //@TableField(value = "last_check_release_date")
    private String last_check_release_date;

    /**
     * 上次库位盘点日期
     */
    //@TableField(value = "last_check_date")
    private String last_check_date;

    /**
     * 自库位共享宽度
     */
    //@TableField(value = "location_shared_width")
    private String location_shared_width;

    /**
     * 自库位共享重量
     */
    //@TableField(value = "location_shared_weight")
    private String location_shared_weight;

    /**
     * 插入其它任务的顺序
     */
    //@TableField(value = "other_seq_num")
    private Integer other_seq_num;

    /**
     * 合并提货的生成待运库位
     */
    //@TableField(value = "merge_pick_ship_loc")
    private Integer merge_pick_ship_loc;

    /**
     * 自动将已拣货品发货
     */
    //@TableField(value = "auto_pick_to_deliver")
    private Integer auto_pick_to_deliver;

    /**
     * 自快速拣货的反冲库位
     */
    //@TableField(value = "pick_to_recoil_loc")
    private Integer pick_to_recoil_loc;

    /**
     * 定方位
     */
    //@TableField(value = "set_position")
    private String set_position;

    /**
     * 地面托盘数
     */
    //@TableField(value = "ground_pallet_num")
    private Integer ground_pallet_num;

    /**
     * 备注
     */
    //@TableField(value = "remark")
    private String remark;

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
    private BigDecimal user_def9;

    /**
     * 用户自定义字段10
     */
    //@TableField(value = "user_def10")
    private BigDecimal user_def10;

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

    /**
     * 是否有效
     */
    //@TableField(value = "valid")
    private Integer valid;

    /**
     * 库位处置, 参照数据字典LOCATION_HANDLE
     */
    //@TableField(value = "location_handle")
    private String location_handle;

    /**
     * 库位标识, 参照数据字典LOCATION_FLAG
     */
    //@TableField(value = "location_flag")
    private String location_flag;

    /**
     * 丢失编号(0-不丢失,1-丢失)
     */
    //@TableField(value = "discard_pallet")
    private Integer discard_pallet;

    //@TableField(exist = false)
    private static final long serialVersionUID = 1L;
}