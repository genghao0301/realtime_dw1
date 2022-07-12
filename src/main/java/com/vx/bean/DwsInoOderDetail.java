package com.vx.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class DwsInoOderDetail extends BaseBean {
    String wh_code ;
    String wms_wh_code ;
    String client_code ;
    String sku_code ;
    String pallet_code ;
    String class_code ;
    String location_code ;
    String room_code ;
    String asn_code ;
    String asn_line_no ;
    String asn_status ;
    String temperature ;
    BigDecimal asn_ea ;
    BigDecimal weight ;
    BigDecimal gross_weight ;
    BigDecimal net_weight ;
    String create_time ;
    String update_time ;
    String etl_time ;
    //以下测试
    //收货创建时间
//   String iacct;
//   //收货更新时间
//   String iacut;


    public DwsInoOderDetail(DwdInbAsnContainer dwdInbAsnContainer,DwdInvTransaction dwdInvTransaction) {
        mergeDwdInbAsnContainer(dwdInbAsnContainer);
        mergeDwdInvTransaction(dwdInvTransaction);
    }

    public DwsInoOderDetail(DwdInvTransaction dwdInvTransaction){
        mergeDwdInvTransaction(dwdInvTransaction);
    }

    private void mergeDwdInbAsnContainer(DwdInbAsnContainer dwdInbAsnContainer) {
        if (dwdInbAsnContainer != null){
            this.wms_wh_code = dwdInbAsnContainer.getWarehouse_code();
            this.client_code = dwdInbAsnContainer.getClient_code();
            this.sku_code = dwdInbAsnContainer.getSku_code();
            this.pallet_code = dwdInbAsnContainer.getPallet_code();
            this.asn_code = dwdInbAsnContainer.getAsn_code();
//            this.asn_line_no = dwdInbAsnContainer.getAsn_line_no();
            this.asn_status = dwdInbAsnContainer.getStatus();
            this.temperature = dwdInbAsnContainer.getArrival_temperature();
            //以下测试
//            this.iacct = dwdInbAsnContainer.getCreated_dtm_loc();
//            this.iacut = dwdInbAsnContainer.getUpdated_dtm_loc();
            this.setTable(dwdInbAsnContainer.getTable());
        }
    }

    private void mergeDwdInvTransaction(DwdInvTransaction dwdInvTransaction) {
        if (dwdInvTransaction != null){
            this.wh_code = dwdInvTransaction.getWh_code();
            this.wms_wh_code = dwdInvTransaction.getWarehouse_code();
            this.client_code = dwdInvTransaction.getClient_code();
            this.sku_code = dwdInvTransaction.getItem_code();
            this.pallet_code = dwdInvTransaction.getLpn_no();
            this.class_code = dwdInvTransaction.getItem_class_code();
            this.location_code = dwdInvTransaction.getLocation_code();
            this.room_code = dwdInvTransaction.getRoom_code();
            this.asn_code = dwdInvTransaction.getAsn_code();
            this.asn_line_no = dwdInvTransaction.getAsn_line_no();
            this.asn_status = dwdInvTransaction.getStatus();
            this.temperature = dwdInvTransaction.getArrival_temperature();
            this.asn_ea = dwdInvTransaction.getInv_adjustment_qty();
            this.weight = dwdInvTransaction.getTotal_weight();
            this.gross_weight = dwdInvTransaction.getTotal_gross_weight();
            this.net_weight = dwdInvTransaction.getTotal_net_weight();
            this.create_time = dwdInvTransaction.getCreated_dtm_loc();
            this.update_time = dwdInvTransaction.getUpdated_dtm_loc();
            //添加 db op table  primaryKey
            this.setDb(dwdInvTransaction.getDb());
            this.setTable(dwdInvTransaction.getTable());
            this.setOp(dwdInvTransaction.getOp());
            this.setPrimaryKey(dwdInvTransaction.getPrimaryKey());

        }
    }

}
