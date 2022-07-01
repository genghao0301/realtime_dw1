package com.vx.app.func;

import cdc.vx.bean.DimMdPackageDetail;
import cdc.vx.bean.DimMdSku;
import com.vx.bean.DwdInvTransaction;
import com.vx.bean.InvTransaction;
import com.vx.utils.DimUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.CollectionUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @version V1.0
 * @ClassName: InvTransactionCount
 * @Description: TODO
 * @Author: xiehp
 * @Date: 2022/5/24 15:53
 */
public class InvTransactionCount implements AggregateFunction<DwdInvTransaction, DwdInvTransaction, DwdInvTransaction> {

    @Override
    public DwdInvTransaction createAccumulator() {
        return new DwdInvTransaction();
    }

    @Override
    public DwdInvTransaction add(DwdInvTransaction invTransaction, DwdInvTransaction accumulator) {

        // 操作名称
        String op = invTransaction.getOp();
        // 获取数据
        if ("delete".equals(op)){
            return accumulator;
        }
        // adsInvTransaction.getNumbers() == 0.0
        if (StringUtils.isBlank(accumulator.getEventTimeDay())) {
            //仓库编码
            String warehouse_code = invTransaction.getWarehouse_code();
            accumulator.setWarehouse_code(warehouse_code);
            //货主代码
            String client_code = invTransaction.getClient_code();
            accumulator.setClient_code(client_code);
            //货主名称
            String client_name = invTransaction.getClient_name();
            accumulator.setClient_name(client_name);
            //单据类型
            String reference_type = invTransaction.getReference_type();
            accumulator.setReference_type(reference_type);
            //托盘编号
            String lpn_no = invTransaction.getLpn_no();
            accumulator.setLpn_no(lpn_no);
            // 事件日期
            String event_time_day = invTransaction.getEventTimeDay();
            accumulator.setEventTimeDay(event_time_day);
            // 各指标
            accumulator.setEaNum(invTransaction.getInv_adjustment_qty());
            accumulator.setInv_adjustment_qty(invTransaction.getInv_adjustment_qty());
            accumulator.setTotal_weight(invTransaction.getTotal_weight());
            accumulator.setTotal_gross_weight(invTransaction.getTotal_gross_weight());
            accumulator.setTotal_net_weight(invTransaction.getTotal_net_weight());
            accumulator.setPalletNum(invTransaction.getPalletNum());
            accumulator.setCsNum(invTransaction.getCsNum());
        }
        else {
            accumulator.setEaNum(accumulator.getEaNum().add(invTransaction.getInv_adjustment_qty()));
            accumulator.setInv_adjustment_qty(accumulator.getInv_adjustment_qty().add(invTransaction.getInv_adjustment_qty()));
            accumulator.setTotal_weight(accumulator.getTotal_weight().add(invTransaction.getTotal_weight()));
            accumulator.setTotal_gross_weight(accumulator.getTotal_gross_weight().add(invTransaction.getTotal_gross_weight()));
            accumulator.setTotal_net_weight(accumulator.getTotal_net_weight().add(invTransaction.getTotal_net_weight()));
            accumulator.setPalletNum(accumulator.getPalletNum().add(invTransaction.getPalletNum()));
            accumulator.setCsNum(accumulator.getCsNum().add(invTransaction.getCsNum()));
        }
        System.out.println("===============================add: "+ invTransaction.getEventTimeDay() + ":" + invTransaction.getInv_transaction_id() + ":" + invTransaction.getInv_adjustment_qty());
        return accumulator;
    }

    @Override
    public DwdInvTransaction getResult(DwdInvTransaction accumulator) {
        //System.out.println("result is " + adsInvTransaction);
        return accumulator;
    }

    @Override
    public DwdInvTransaction merge(DwdInvTransaction acc1, DwdInvTransaction acc2) {
        acc1.setEaNum(acc1.getEaNum().add(acc2.getEaNum()));
        acc1.setInv_adjustment_qty(acc1.getInv_adjustment_qty().add(acc2.getInv_adjustment_qty()));
        acc1.setTotal_weight(acc1.getTotal_weight().add(acc2.getTotal_weight()));
        acc1.setTotal_gross_weight(acc1.getTotal_gross_weight().add(acc2.getTotal_gross_weight()));
        acc1.setTotal_net_weight(acc1.getTotal_net_weight().add(acc2.getTotal_net_weight()));
        acc1.setPalletNum(acc1.getPalletNum().add(acc2.getPalletNum()));
        acc1.setCsNum(acc1.getCsNum().add(acc2.getCsNum()));
        System.out.println("===============================merge: " + acc1.getEaNum());
        return acc1;
    }

}
