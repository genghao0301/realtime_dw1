package com.vx.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class DwdWideInbAsnContainer extends BaseBean{
    /**
     * 事件日期
     */
     String eventTimeDay;
    /**
     * 收货箱主键
     */
     Long inb_asn_container_id;
    /**
     * 仓库ID
     */
     Long warehouse_id;
    /**
     * 仓库
     */
     String warehouse_code;
    /**
     * 公司代码
     */
     String company_code;
    /**
     * 公司名称
     */
     String company_name;
    /**
     * 货主代码
     */
     String client_code;
    /**
     * 货主名称，买方名称
     */
     String client_name;
    /**
     * 收货箱号
     */
     String from_container_code;
    /**
     * 收货箱类型
     */
     String container_type;
    /**
     * 收货类型
     */
     String receive_way;
    /**
     * 混箱属性
     */
     String carton_break_attr;
    /**
     * 收货箱状态
     */
     Integer status;
    /**
     * 入库单明细表主键ID
     */
     Long inb_asn_detail_id;
    /**
     * 入库单号
     */
     String asn_code;
    /**
     * 入库单明细行号
     */
     Integer asn_line_no;
    /**
     * PO单明细表主键
     */
     Long inb_po_detail_id;
    /**
     * PO单号
     */
     String po_no;
    /**
     * PO明细行号
     */
     Integer po_line_no;
    /**
     * 入库单类型
     */
     String asn_type;
    /**
     * 运单号（客退入库）
     */
     String return_tran_bill_no;
    /**
     * 原销售订单号（客退入库）
     */
     String return_sale_order_no;
    /**
     * SO单号
     */
     String so_order_no;
    /**
     * 供应商箱号
     */
     String purchase_case_no;
    /**
     * 目的库位
     */
     String to_location_code;
    /**
     * 落放托盘号
     */
     String put_pallet_code;
    /**
     * 托盘号
     */
     String pallet_code;
    /**
     * 开箱时间
     */
     String container_open_time;
    /**
     * 关箱时间
     */
     String container_close_time;
    /**
     * 货品编码
     */
     String sku_code;
    /**
     * 商品描述
     */
     String sku_name;
    /**
     * 整包装单包装件数
     */
     BigDecimal pack_qty;
    /**
     * 包装编码
     */
     String package_code;
    /**
     * 包装代码
     */
     String uom_code;
    /**
     * 包装层级单位
     */
     String uom_level;
    /**
     * 计划收货数量
     */
     BigDecimal plan_qty;
    /**
     * 扫描数量（件）
     */
     BigDecimal scan_qty;
    /**
     * 单位  EA：单件收货 CS：整包装收货
     */
     String scan_uom;
    /**
     * 扫描人代码
     */
     String scan_person;
    /**
     * 扫描人ID
     */
     String scan_person_id;
    /**
     * 扫描时间
     */
     String scan_time;
    /**
     * QC类型：收货前质检-100、收货后质检-200
     */
     String qc_type;
    /**
     * 是否需要QC：否-0、是-1
     */
     int qc_flag;
    /**
     * QC自动调整：否-0、是-1
     */
     int qc_auto_adjust_flag;
    /**
     * QC状态：未完成-100、已完成-200
     */
     String qc_status;
    /**
     * QC检验EA量
     */
     BigDecimal qc_check_ea_qty;
    /**
     * QC拒绝EA量
     */
     BigDecimal qc_reject_ea_qty;
    /**
     * QC检验员code
     */
     String qc_check_user_code;
    /**
     * QC检验员名称
     */
     String qc_check_user_name;
    /**
     * QC拒绝原因：破损-100、劣质品-200、不合格-300
     */
     String qc_reject_reason;
    /**
     * QC处理方法：报废-100，按原样使用-200、修理-300
     */
     String qc_handle;
    /**
     * QC隔离LPN
     */
     String qc_lpn;
    /**
     * 超收原因（10 -供应商多货，20-额外收货）
     */
     String overflow_reason_type;
    /**
     * 组车数
     */
     BigDecimal group_qty;
    /**
     * 组车单位
     */
     String group_uom;
    /**
     * 组车人代码
     */
     String group_person;
    /**
     * 组车人ID
     */
     String group_person_id;
    /**
     * 组车时间
     */
     String group_time;
    /**
     * 默认上架策略
     */
     String putaway_rule;
    /**
     * 推荐库位
     */
     String def_location_code;
    /**
     * 上架数
     */
     BigDecimal check_qty;
    /**
     * 单位
     */
     String check_uom;
    /**
     * 上架人代码
     */
     String check_person;
    /**
     * 上架人ID
     */
     String check_person_id;
    /*8
    上架时间
     */
     String check_time;
    /**
     * 撤销量
     */
     BigDecimal cancel_qty;
    /**
     * 撤销收货时间
     */
     String cancel_time;
    /*8
    商品条码
     */
     String bar_code;
    /**
     * 基本单位名称
     */
     String basic_uom_name;
    /**
     * 总皮重
     */
     BigDecimal weight;
    /**
     * 总毛重
     */
     BigDecimal gross_weight;
    /*8
    总净重
     */
     BigDecimal net_weight;
    /**
     * 重量单位
     */
     String weight_uom;
    /**
     * 总体积
     */
     BigDecimal volume;
    /**
     * 体积单位
     */
     String volume_uom;
    /**
     * 库存属性ID
     */
     Long inv_lot_id;
    /**
     * VMI原档期号
     */
     String vmi_sales_no;
    /**
     * 销售品牌ID
     */
     String sale_brand_id;
    /**
     * 客退分类代码
     */
     String return_iqc_classify_code;
    /**
     * 客退分类名称
     */
     String return_iqc_classify_name;
    /**
     * 条码客退分类
     */
     String return_item_category_code;
    /**
     * 送货箱整箱收货，0：不是；1：是
     */
     Integer is_fcl;
    /**
     * 是否当面清点：默认值0  ‘0’表示否，‘1’表示是
     */
     Integer is_spot_check;
    /*8
    是否混放
     */
     Integer is_mix;
    /*8
    是否越库周转箱
     */
     Integer is_crossdock_container;
    /**
     * 供应商编码
     */
     String vendor_code;
    /**
     * 供应商名称
     */
     String vendor_name;
    /*8
    品牌代码
     */
     String brand_code;
    /**
     * 品牌名称
     */
     String brand_name;
    /**
     * 第三方货物拥有者代码
     */
     String owner_code;
    /**
     * 第三方货物拥有者名称
     */
     String owner_name;
    /**
     * 库存类型
     */
     String inventory_type;
    /**
     * 批号
     */
     String batch_no;
    /**
     * 库存状态
     */
     String inventory_quality;
    /**
     * 原产国
     */
     String country_of_origin;
    /**
     * 生产日期
     */
     String mfg_date;
    /**
     * 失效日期
     */
     String exp_date;
    /**
     * 收货时间
     */
     String received_date;
    /**
     * 销售档期号
     */
     String sales_no;
    /**
     * 销售状态。0-不可售 1-可售
     */
     String sales_status;
    /**
     * 批属性字段1
     */
//     String lot_attr1;
//    /**
//     * 批属性字段1
//     */
//     String lot_attr2;
//    /**
//     * 批属性字段1
//     */
//     String lot_attr3;
//    /**
//     * 批属性字段1
//     */
//     String lot_attr4;
//    /**
//     * 批属性字段1
//     */
//     String lot_attr5;
//    /**
//     * 批属性字段1
//     */
//     String lot_attr6;
//    /**
//     * 批属性字段1
//     */
//     String lot_attr7;
//    /**
//     * 批属性字段1
//     */
//     String lot_attr8;
//    /**
//     * 批属性字段1
//     */
//     String lot_attr9;
//    /**
//     * 批属性字段1
//     */
//     String lot_attr10;
//    /**
//     * 批属性字段1
//     */
//     String lot_attr11;
//    /**
//     * 批属性字段1
//     */
//     String lot_attr12;
//    /**
//     * 批属性字段1
//     */
//     String lot_attr13;
//    /**
//     * 批属性字段1
//     */
//     String lot_attr14;
//    /**
//     * 批属性字段1
//     */
//     String lot_attr15;
//    /**
//     * 批属性字段1
//     */
     String lot_attr16;
    /**
     * EDI接口字段1  0:初始值 ，1：已写接口  9:不用处理
     */
     String edi_inf1;
    /**
     * EDI接口字段2   0:初始值 ，1：已写接口
     */
     String edi_inf2;
    /**
     * EDI接口字段3  0:初始值 ，1：已写接口
     */
     String edi_inf3;
    /**
     * 更新时间1
     */
     String edi_upd1;
    /**
     * 更新时间1
     */
     String edi_upd2;
    /**
     * 更新时间1
     */
     String edi_upd3;
    /**
     * 最终目的仓
     */
     String order_warehouse_code;
    /**
     * 1：正常2：超数量3：超条码4：超销售订单
     */
     int return_result_type;
    /**
     * 抽检单头表主键ID
     */
     Long inb_spot_check_header_id;
    /**
     * 文件管理头表主键ID
     */
     Long inv_file_header_id;
    /**
     * 备注
     */
     String remark;
    /**
     * 用户自定义字段1
     */
//     String user_def1;
//    /**
//     * 用户自定义字段1
//     */
//     String user_def2;
//    /**
//     * 用户自定义字段1
//     */
//     String user_def3;
//    /**
//     * 用户自定义字段1
//     */
//     String user_def4;
//    /**
//     * 用户自定义字段1
//     */
//     String user_def5;
//    /**
//     * 用户自定义字段1
//     */
//     String user_def6;
//    /**
//     * 用户自定义字段1
//     */
//     String user_def7;
//    /**
//     * 用户自定义字段1
//     */
//     String user_def8;
//    /**
//     * 用户自定义字段1
//     */
//     String user_def9;
//    /**
//     * 用户自定义字段1
//     */
//     String user_def10;
    /**
     * 是否有效,0-无效,1-有效
     */
     int valid;
    /**
     * 创建用户ID
     */
     Long created_by_user_id;
    /**
     * 创建用户
     */
     String created_by_user;
    /**
     * 创建地点
     */
     String created_office;
    /**
     * 创建时间
     */
     String created_dtm_loc;
    /**
     * 创建时区
     */
     String created_time_zone;
    /**
     * 更新用户ID
     */
     Long updated_by_user_id;
    /**
     * 更新用户
     */
     String updated_by_user;
    /**
     * 更新地点
     */
     String updated_office;
    /**
     * 更新时间
     */
     String updated_dtm_loc;
    /**
     * 更新时区
     */
     String updated_time_zone;
    /**
     * 记录版本
     */
     Long record_version;
    /**
     * 到货温度（单位：°C）
      */
     String arrival_temperature;

    public DwdWideInbAsnContainer(InbAsnHeader inbAsnHeader,InbAsnContainer inbAsnContainer) {
        mergeInbAsnContainer(inbAsnContainer);
        mergeInbAsnHeader(inbAsnHeader);
    }

    private void mergeInbAsnHeader(InbAsnHeader inbAsnHeader) {
        if (inbAsnHeader != null){
            this.arrival_temperature = inbAsnHeader.getArrival_temperature();
//            this.arrival_temperature = inbAsnHeader.getWarehouse_code();
        }
    }

    private void mergeInbAsnContainer(InbAsnContainer inbAsnContainer) {
        if (inbAsnContainer != null){
            this.eventTimeDay = inbAsnContainer.getEventTimeDay();
            this.inb_asn_container_id = inbAsnContainer.getInb_asn_container_id();
            this.warehouse_id = inbAsnContainer.getWarehouse_id();
            this.warehouse_code = inbAsnContainer.getWarehouse_code();
            this.company_code = inbAsnContainer.getCompany_code();
            this.company_name = inbAsnContainer.getCompany_name();
            this.client_code = inbAsnContainer.getClient_code();
            this.client_name = inbAsnContainer.getClient_name();
            this.from_container_code = inbAsnContainer.getFrom_container_code();
            this.container_type = inbAsnContainer.getContainer_type();
            this.receive_way = inbAsnContainer.getReceive_way();
            this.carton_break_attr = inbAsnContainer.getCarton_break_attr();
            this.status = inbAsnContainer.getStatus();
            this.inb_asn_detail_id = inbAsnContainer.getInb_asn_detail_id();
            this.asn_code = inbAsnContainer.getAsn_code();
            this.asn_line_no = inbAsnContainer.getAsn_line_no();
            this.inb_po_detail_id = inbAsnContainer.getInb_po_detail_id();
            this.po_no = inbAsnContainer.getPo_no();
            this.po_line_no = inbAsnContainer.getPo_line_no();
            this.asn_type = inbAsnContainer.getAsn_type();
            this.return_tran_bill_no = inbAsnContainer.getReturn_tran_bill_no();
            this.return_sale_order_no = inbAsnContainer.getReturn_sale_order_no();
            this.so_order_no = inbAsnContainer.getSo_order_no();
            this.purchase_case_no = inbAsnContainer.getPurchase_case_no();
            this.to_location_code = inbAsnContainer.getTo_location_code();
            this.put_pallet_code = inbAsnContainer.getPut_pallet_code();
            this.pallet_code = inbAsnContainer.getPallet_code();
            this.container_open_time = inbAsnContainer.getContainer_open_time();
            this.container_close_time = inbAsnContainer.getContainer_close_time();
            this.sku_code = inbAsnContainer.getSku_code();
            this.sku_name = inbAsnContainer.getSku_name();
            this.pack_qty = inbAsnContainer.getPack_qty();
            this.package_code = inbAsnContainer.getPackage_code();
            this.uom_code = inbAsnContainer.getUom_code();
            this.uom_level = inbAsnContainer.getUom_level();
            this.plan_qty = inbAsnContainer.getPlan_qty();
            this.scan_qty = inbAsnContainer.getScan_qty();
            this.scan_uom = inbAsnContainer.getScan_uom();
            this.scan_person = inbAsnContainer.getScan_person();
            this.scan_person_id = inbAsnContainer.getScan_person_id();
            this.scan_time = inbAsnContainer.getScan_time();
            this.qc_type = inbAsnContainer.getQc_type();
            this.qc_flag = inbAsnContainer.getQc_flag();
            this.qc_auto_adjust_flag = inbAsnContainer.getQc_auto_adjust_flag();
            this.qc_status = inbAsnContainer.getQc_status();
            this.qc_check_ea_qty = inbAsnContainer.getQc_check_ea_qty();
            this.qc_reject_ea_qty = inbAsnContainer.getQc_reject_ea_qty();
            this.qc_check_user_code = inbAsnContainer.getQc_check_user_code();
            this.qc_check_user_name = inbAsnContainer.getQc_check_user_name();
            this.qc_reject_reason = inbAsnContainer.getQc_reject_reason();
            this.qc_handle = inbAsnContainer.getQc_handle();
            this.qc_lpn = inbAsnContainer.getQc_lpn();
            this.overflow_reason_type = inbAsnContainer.getOverflow_reason_type();
            this.group_qty = inbAsnContainer.getGroup_qty();
            this.group_uom = inbAsnContainer.getGroup_uom();
            this.group_person = inbAsnContainer.getGroup_person();
            this.group_person_id = inbAsnContainer.getGroup_person_id();
            this.group_time = inbAsnContainer.getGroup_time();
            this.putaway_rule = inbAsnContainer.getPutaway_rule();
            this.def_location_code = inbAsnContainer.getDef_location_code();
            this.check_qty = inbAsnContainer.getCheck_qty();
            this.check_uom = inbAsnContainer.getCheck_uom();
            this.check_person = inbAsnContainer.getCheck_person();
            this.check_person_id = inbAsnContainer.getCheck_person_id();
            this.check_time = inbAsnContainer.getCheck_time();
            this.cancel_qty = inbAsnContainer.getCancel_qty();
            this.cancel_time = inbAsnContainer.getCancel_time();
            this.bar_code = inbAsnContainer.getBar_code();
            this.basic_uom_name = inbAsnContainer.getBasic_uom_name();
            this.weight = inbAsnContainer.getWeight();
            this.gross_weight = inbAsnContainer.getGross_weight();
            this.net_weight = inbAsnContainer.getNet_weight();
            this.weight_uom = inbAsnContainer.getWeight_uom();
            this.volume = inbAsnContainer.getVolume();
            this.volume_uom = inbAsnContainer.getVolume_uom();
            this.inv_lot_id = inbAsnContainer.getInv_lot_id();
            this.vmi_sales_no = inbAsnContainer.getVmi_sales_no();
            this.sale_brand_id = inbAsnContainer.getSale_brand_id();
            this.return_iqc_classify_code = inbAsnContainer.getReturn_iqc_classify_code();
            this.return_iqc_classify_name = inbAsnContainer.getReturn_iqc_classify_name();
            this.return_item_category_code = inbAsnContainer.getReturn_item_category_code();
            this.is_fcl = inbAsnContainer.getIs_fcl();
            this.is_spot_check = inbAsnContainer.getIs_spot_check();
            this.is_mix = inbAsnContainer.getIs_mix();
            this.is_crossdock_container = inbAsnContainer.getIs_crossdock_container();
            this.vendor_code = inbAsnContainer.getVendor_code();
            this.vendor_name = inbAsnContainer.getVendor_name();
            this.brand_code = inbAsnContainer.getBrand_code();
            this.brand_name = inbAsnContainer.getBrand_name();
            this.owner_code = inbAsnContainer.getOwner_code();
            this.owner_name = inbAsnContainer.getOwner_name();
            this.inventory_type = inbAsnContainer.getInventory_type();
            this.batch_no = inbAsnContainer.getBatch_no();
            this.inventory_quality = inbAsnContainer.getInventory_quality();
            this.country_of_origin = inbAsnContainer.getCountry_of_origin();
            this.mfg_date = inbAsnContainer.getMfg_date();
            this.exp_date = inbAsnContainer.getExp_date();
            this.received_date = inbAsnContainer.getReceived_date();
            this.sales_no = inbAsnContainer.getSales_no();
            this.sales_status = inbAsnContainer.getSales_status();
//            this.lot_attr1 = inbAsnContainer.getLot_attr1();
//            this.lot_attr2 = inbAsnContainer.getLot_attr2();
//            this.lot_attr3 = inbAsnContainer.getLot_attr3();
//            this.lot_attr4 = inbAsnContainer.getLot_attr4();
//            this.lot_attr5 = inbAsnContainer.getLot_attr5();
//            this.lot_attr6 = inbAsnContainer.getLot_attr6();
//            this.lot_attr7 = inbAsnContainer.getLot_attr7();
//            this.lot_attr8 = inbAsnContainer.getLot_attr8();
//            this.lot_attr9 = inbAsnContainer.getLot_attr9();
//            this.lot_attr10 = inbAsnContainer.getLot_attr10();
//            this.lot_attr11 = inbAsnContainer.getLot_attr11();
//            this.lot_attr12 = inbAsnContainer.getLot_attr12();
//            this.lot_attr13 = inbAsnContainer.getLot_attr13();
//            this.lot_attr14 = inbAsnContainer.getLot_attr14();
//            this.lot_attr15 = inbAsnContainer.getLot_attr15();x
            this.lot_attr16 = inbAsnContainer.getLot_attr16();
            this.edi_inf1 = inbAsnContainer.getEdi_inf1();
            this.edi_inf2 = inbAsnContainer.getEdi_inf2();
            this.edi_inf3 = inbAsnContainer.getEdi_inf3();
            this.edi_upd1 = inbAsnContainer.getEdi_upd1();
            this.edi_upd2 = inbAsnContainer.getEdi_upd2();
            this.edi_upd3 = inbAsnContainer.getEdi_upd3();
            this.order_warehouse_code = inbAsnContainer.getOrder_warehouse_code();
            this.return_result_type = inbAsnContainer.getReturn_result_type();
            this.inb_spot_check_header_id = inbAsnContainer.getInb_spot_check_header_id();
            this.inv_file_header_id = inbAsnContainer.getInv_file_header_id();
            this.remark = inbAsnContainer.getRemark();
//            this.user_def1 = inbAsnContainer.getUser_def1();
//            this.user_def2 = inbAsnContainer.getUser_def2();
//            this.user_def3 = inbAsnContainer.getUser_def3();
//            this.user_def4 = inbAsnContainer.getUser_def4();
//            this.user_def5 = inbAsnContainer.getUser_def5();
//            this.user_def6 = inbAsnContainer.getUser_def6();
//            this.user_def7 = inbAsnContainer.getUser_def7();
//            this.user_def8 = inbAsnContainer.getUser_def8();
//            this.user_def9 = inbAsnContainer.getUser_def9();
//            this.user_def10 = inbAsnContainer.getUser_def10();
            this.valid = inbAsnContainer.getValid();
            this.created_by_user_id = inbAsnContainer.getCreated_by_user_id();
            this.created_by_user = inbAsnContainer.getCreated_by_user();
            this.created_office = inbAsnContainer.getCreated_office();
            this.created_dtm_loc = inbAsnContainer.getCreated_dtm_loc();
            this.created_time_zone = inbAsnContainer.getCreated_time_zone();
            this.updated_by_user_id = inbAsnContainer.getUpdated_by_user_id();
            this.updated_by_user = inbAsnContainer.getUpdated_by_user();
            this.updated_office = inbAsnContainer.getUpdated_office();
            this.updated_dtm_loc = inbAsnContainer.getUpdated_dtm_loc();
            this.updated_time_zone = inbAsnContainer.getUpdated_time_zone();
            this.record_version = inbAsnContainer.getRecord_version();
            //
            this.setDb(inbAsnContainer.getDb());
            this.setTable(inbAsnContainer.getTable());
            this.setPrimaryKey(inbAsnContainer.getPrimaryKey());
            this.setOp(inbAsnContainer.getOp());

        }
    }


}
