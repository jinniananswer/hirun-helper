package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * @program: hirun-helper
 * @description: 订单合同信息数据对象
 * @author: jinnian
 * @create: 2020-07-28 18:56
 **/
@Data
public class OrderContractDTO {

    private Long orderId;

    private String contractType;

    private String contractTypeName;

    private LocalDate signDate;

    private String busiLevel;

    private String busiLevelName;

    private LocalDate chargeSecondFeeDate;

    private String environmentalTestingAgency;

    private String environmentalTestingAgencyName;

    private LocalDate startDate;

    private LocalDate endDate;
}
