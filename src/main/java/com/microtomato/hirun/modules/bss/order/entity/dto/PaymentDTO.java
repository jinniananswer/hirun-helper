package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 支付方式数据传输对象
 * @author: jinnian
 * @create: 2020-02-22 00:58
 **/
@Data
public class PaymentDTO {

    /**
     * 支付方式 细项ID
     */
    private Long paymentId;

    /**
     * 支付类型
     */
    private String paymentType;

    /**
     * 支付类型名称
     */
    private String paymentTypeName;

    /**
     * 支付方式名称
     */
    private String paymentName;

    /**
     * 金额
     */
    private Double money = 0.00;
}
