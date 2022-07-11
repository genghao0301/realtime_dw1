package com.vx.bean;

import lombok.Data;

@Data
public class DwdInbAsnContainer extends InbAsnContainer{
    /**
     * 到货温度（单位：°C）
     */
    private String arrival_temperature;

    /**
     * 状态类型
     */
    private String inv_adjustment_type;
}
