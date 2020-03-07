package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 收款业务数据传输对象
 * @author: jinnian
 * @create: 2020-02-26 00:00
 **/
@Data
public class CollectFeeDTO {

    /**
     * 付款日期
     */
    private LocalDate payDate;

    /**
     * 应付总金额
     */
    private Double needPay;

    /**
     * 付款编码，修改时使用
     */
    private Long payNo;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 付款项
     */
    private List<PayItemDTO> payItems;

    /**
     * 付款方式明细
     */
    private List<PaymentDTO> payments;
}
