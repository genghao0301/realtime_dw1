package com.vx.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 收货箱表
 * <p>
 * inb_asn_container
 */
@Data
public class InbAsnContainer extends BaseBean implements Serializable {
    /**
     * 事件日期
     */
    private String eventTimeDay;
    /**
     * 收货箱主键
     */
    private Long inb_asn_container_id;
    /**
     * 仓库ID
     */
    private Long warehouse_id;
    /**
     * 仓库
     */
    private String warehouse_code;
    /**
     * 公司代码
     */
    private String company_code;
    /**
     * 公司名称
     */
    private String company_name;
    /**
     * 货主代码
     */
    private String client_code;
    /**
     * 货主名称，买方名称
     */
    private String client_name;
    /**
     * 收货箱号
     */
    private String from_container_code;
    /**
     * 收货箱类型
     */
    private String container_type;
    /**
     * 收货类型
     */
    private String receive_way;
    /**
     * 混箱属性
     */
    private String carton_break_attr;
    /**
     * 收货箱状态
     */
    private Integer status;
    /**
     * 入库单明细表主键ID
     */
    private Long inb_asn_detail_id;
    /**
     * 入库单号
     */
    private String asn_code;
    /**
     * 入库单明细行号
     */
    private Integer asn_line_no;
    /**
     * PO单明细表主键
     */
    private Long inb_po_detail_id;
    /**
     * PO单号
     */
    private String po_no;
    /**
     * PO明细行号
     */
    private Integer po_line_no;
    /**
     * 入库单类型
     */
    private String asn_type;
    /**
     * 运单号（客退入库）
     */
    private String return_tran_bill_no;
    /**
     * 原销售订单号（客退入库）
     */
    private String return_sale_order_no;
    /**
     * SO单号
     */
    private String so_order_no;
    /**
     * 供应商箱号
     */
    private String purchase_case_no;
    /**
     * 目的库位
     */
    private String to_location_code;
    /**
     * 落放托盘号
     */
    private String put_pallet_code;
    /**
     * 托盘号
     */
    private String pallet_code;
    /**
     * 开箱时间
     */
    private String container_open_time;
    /**
     * 关箱时间
     */
    private String container_close_time;
    /**
     * 货品编码
     */
    private String sku_code;
    /**
     * 商品描述
     */
    private String sku_name;
    /**
     * 整包装单包装件数
     */
    private BigDecimal pack_qty;
    /**
     * 包装编码
     */
    private String package_code;
    /**
     * 包装代码
     */
    private String uom_code;
    /**
     * 包装层级单位
     */
    private String uom_level;
    /**
     * 计划收货数量
     */
    private BigDecimal plan_qty;
    /**
     * 扫描数量（件）
     */
    private BigDecimal scan_qty;
    /**
     * 单位  EA：单件收货 CS：整包装收货
     */
    private String scan_uom;
    /**
     * 扫描人代码
     */
    private String scan_person;
    /**
     * 扫描人ID
     */
    private String scan_person_id;
    /**
     * 扫描时间
     */
    private String scan_time;
    /**
     * QC类型：收货前质检-100、收货后质检-200
     */
    private String qc_type;
    /**
     * 是否需要QC：否-0、是-1
     */
    private int qc_flag;
    /**
     * QC自动调整：否-0、是-1
     */
    private int qc_auto_adjust_flag;
    /**
     * QC状态：未完成-100、已完成-200
     */
    private String qc_status;
    /**
     * QC检验EA量
     */
    private BigDecimal qc_check_ea_qty;
    /**
     * QC拒绝EA量
     */
    private BigDecimal qc_reject_ea_qty;
    /**
     * QC检验员code
     */
    private String qc_check_user_code;
    /**
     * QC检验员名称
     */
    private String qc_check_user_name;
    /**
     * QC拒绝原因：破损-100、劣质品-200、不合格-300
     */
    private String qc_reject_reason;
    /**
     * QC处理方法：报废-100，按原样使用-200、修理-300
     */
    private String qc_handle;
    /**
     * QC隔离LPN
     */
    private String qc_lpn;
    /**
     * 超收原因（10 -供应商多货，20-额外收货）
     */
    private String overflow_reason_type;
    /**
     * 组车数
     */
    private BigDecimal group_qty;
    /**
     * 组车单位
     */
    private String group_uom;
    /**
     * 组车人代码
     */
    private String group_person;
    /**
     * 组车人ID
     */
    private String group_person_id;
    /**
     * 组车时间
     */
    private String group_time;
    /**
     * 默认上架策略
     */
    private String putaway_rule;
    /**
     * 推荐库位
     */
    private String def_location_code;
    /**
     * 上架数
     */
    private BigDecimal check_qty;
    /**
     * 单位
     */
    private String check_uom;
    /**
     * 上架人代码
     */
    private String check_person;
    /**
     * 上架人ID
     */
    private String check_person_id;
    /*8
    上架时间
     */
    private String check_time;
    /**
     * 撤销量
     */
    private BigDecimal cancel_qty;
    /**
     * 撤销收货时间
     */
    private String cancel_time;
    /*8
    商品条码
     */
    private String bar_code;
    /**
     * 基本单位名称
     */
    private String basic_uom_name;
    /**
     * 总皮重
     */
    private BigDecimal weight;
    /**
     * 总毛重
     */
    private BigDecimal gross_weight;
    /*8
    总净重
     */
    private BigDecimal net_weight;
    /**
     * 重量单位
     */
    private String weight_uom;
    /**
     * 总体积
     */
    private BigDecimal volume;
    /**
     * 体积单位
     */
    private String volume_uom;
    /**
     * 库存属性ID
     */
    private Long inv_lot_id;
    /**
     * VMI原档期号
     */
    private String vmi_sales_no;
    /**
     * 销售品牌ID
     */
    private String sale_brand_id;
    /**
     * 客退分类代码
     */
    private String return_iqc_classify_code;
    /**
     * 客退分类名称
     */
    private String return_iqc_classify_name;
    /**
     * 条码客退分类
     */
    private String return_item_category_code;
    /**
     * 送货箱整箱收货，0：不是；1：是
     */
    private Integer is_fcl;
    /**
     * 是否当面清点：默认值0  ‘0’表示否，‘1’表示是
     */
    private Integer is_spot_check;
    /*8
    是否混放
     */
    private Integer is_mix;
    /*8
    是否越库周转箱
     */
    private Integer is_crossdock_container;
    /**
     * 供应商编码
     */
    private String vendor_code;
    /**
     * 供应商名称
     */
    private String vendor_name;
    /*8
    品牌代码
     */
    private String brand_code;
    /**
     * 品牌名称
     */
    private String brand_name;
    /**
     * 第三方货物拥有者代码
     */
    private String owner_code;
    /**
     * 第三方货物拥有者名称
     */
    private String owner_name;
    /**
     * 库存类型
     */
    private String inventory_type;
    /**
     * 批号
     */
    private String batch_no;
    /**
     * 库存状态
     */
    private String inventory_quality;
    /**
     * 原产国
     */
    private String country_of_origin;
    /**
     * 生产日期
     */
    private String mfg_date;
    /**
     * 失效日期
     */
    private String exp_date;
    /**
     * 收货时间
     */
    private String received_date;
    /**
     * 销售档期号
     */
    private String sales_no;
    /**
     * 销售状态。0-不可售 1-可售
     */
    private String sales_status;
    /**
     * 批属性字段1
     */
    private String lot_attr1;
    /**
     * 批属性字段1
     */
    private String lot_attr2;
    /**
     * 批属性字段1
     */
    private String lot_attr3;
    /**
     * 批属性字段1
     */
    private String lot_attr4;
    /**
     * 批属性字段1
     */
    private String lot_attr5;
    /**
     * 批属性字段1
     */
    private String lot_attr6;
    /**
     * 批属性字段1
     */
    private String lot_attr7;
    /**
     * 批属性字段1
     */
    private String lot_attr8;
    /**
     * 批属性字段1
     */
    private String lot_attr9;
    /**
     * 批属性字段1
     */
    private String lot_attr10;
    /**
     * 批属性字段1
     */
    private String lot_attr11;
    /**
     * 批属性字段1
     */
    private String lot_attr12;
    /**
     * 批属性字段1
     */
    private String lot_attr13;
    /**
     * 批属性字段1
     */
    private String lot_attr14;
    /**
     * 批属性字段1
     */
    private String lot_attr15;
    /**
     * 批属性字段1
     */
    private String lot_attr16;
    /**
     * EDI接口字段1  0:初始值 ，1：已写接口  9:不用处理
     */
    private String edi_inf1;
    /**
     * EDI接口字段2   0:初始值 ，1：已写接口
     */
    private String edi_inf2;
    /**
     * EDI接口字段3  0:初始值 ，1：已写接口
     */
    private String edi_inf3;
    /**
     * 更新时间1
     */
    private String edi_upd1;
    /**
     * 更新时间1
     */
    private String edi_upd2;
    /**
     * 更新时间1
     */
    private String edi_upd3;
    /**
     * 最终目的仓
     */
    private String order_warehouse_code;
    /**
     * 1：正常2：超数量3：超条码4：超销售订单
     */
    private int return_result_type;
    /**
     * 抽检单头表主键ID
     */
    private Long inb_spot_check_header_id;
    /**
     * 文件管理头表主键ID
     */
    private Long inv_file_header_id;
    /**
     * 备注
     */
    private String remark;
    /**
     * 用户自定义字段1
     */
    private String user_def1;
    /**
     * 用户自定义字段1
     */
    private String user_def2;
    /**
     * 用户自定义字段1
     */
    private String user_def3;
    /**
     * 用户自定义字段1
     */
    private String user_def4;
    /**
     * 用户自定义字段1
     */
    private String user_def5;
    /**
     * 用户自定义字段1
     */
    private String user_def6;
    /**
     * 用户自定义字段1
     */
    private String user_def7;
    /**
     * 用户自定义字段1
     */
    private String user_def8;
    /**
     * 用户自定义字段1
     */
    private String user_def9;
    /**
     * 用户自定义字段1
     */
    private String user_def10;
    /**
     * 是否有效,0-无效,1-有效
     */
    private int valid;
    /**
     * 创建用户ID
     */
    private Long created_by_user_id;
    /**
     * 创建用户
     */
    private String created_by_user;
    /**
     * 创建地点
     */
    private String created_office;
    /**
     * 创建时间
     */
    private String created_dtm_loc;
    /**
     * 创建时区
     */
    private String created_time_zone;
    /**
     * 更新用户ID
     */
    private Long updated_by_user_id;
    /**
     * 更新用户
     */
    private String updated_by_user;
    /**
     * 更新地点
     */
    private String updated_office;
    /**
     * 更新时间
     */
    private String updated_dtm_loc;
    /**
     * 更新时区
     */
    private String updated_time_zone;
    /**
     * 记录版本
     */
    private Long record_version;
}
