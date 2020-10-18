package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 订单付款明细
 * @author: jinnian
 * @create: 2020-03-07 17:29
 **/
@Data
public class OrderPayMoneyInfoDTO {

    private String paymentTypeName;

    private String paymentName;

    private Double money;
}
