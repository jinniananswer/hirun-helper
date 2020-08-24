package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 订单支付信息展示
 * @author: jinnian
 * @create: 2020-03-07 16:57
 **/
@Data
public class OrderPayInfoDTO {

    private LocalDate payDate;

    private Double totalMoney;

    private String employeeName;

    private String shopName;

    private String auditStatusName;

    private String auditEmployeeName;

    private List<OrderPayItemInfoDTO> payItems;

    private List<OrderPayMoneyInfoDTO> payMonies;
}
