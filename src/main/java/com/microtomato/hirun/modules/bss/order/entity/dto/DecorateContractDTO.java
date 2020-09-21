package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * @program: hirun-helper
 * @description: 订单费用数据传输对象
 * @author: sunxin
 * @create: 2020-02-05
 **/
@Data
public class DecorateContractDTO {

    private Long orderId;

    private Long id;

    private LocalDate signDate;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate chargeSecondFeeDate;

    private String busiLevel;

    private String environmentalTestingAgency;

    private Integer contractFee;

    private Integer baseDecorationFee;

    private Integer doorFee;

    private Integer furnitureFee;

    private String activityId;

    private Integer chargedDesignFee;

    private Integer returnDesignFee;

    private Integer taxFee;

    private Integer cashDiscount;

    private String remark;

    private Integer firstContractFee;

    private Long financeEmployeeId;

    private String financeEmployeeName;
}
