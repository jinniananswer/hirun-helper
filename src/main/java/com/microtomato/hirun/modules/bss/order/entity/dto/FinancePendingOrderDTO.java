package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 财务待办订单数据传输对象
 * @author: jinnian
 * @create: 2020-03-01 14:46
 **/
@Data
public class FinancePendingOrderDTO {

    private Long custId;

    private String custName;

    private String mobileNo;

    private Long orderId;

    private String decorateAddress;

    private Long payNo;

    private Double totalMoney;

    private String status;

    private String auditStatus;
}
