package com.microtomato.hirun.modules.finance.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @program: hirun-helper
 * @description: 财务付款明细DTO
 * @author: jinnian
 * @create: 2020-11-11 23:15
 **/
@Getter
@Setter
public class VoucherItemDTO {

    private String financeItemId;

    private String financeItemName;

    private String parentFinanceItemId;

    private Long orgId;

    private String orgName;

    private Long employeeId;

    private String projectType;

    private Long projectId;

    private String projectName;

    private String trafficType;

    private LocalDate trafficDate;

    private String trafficBegin;

    private String trafficEnd;

    private Double trafficFee;

    private Double allowance;

    private Double hotelFee;

    private Double fee;

    private String remark;
}
