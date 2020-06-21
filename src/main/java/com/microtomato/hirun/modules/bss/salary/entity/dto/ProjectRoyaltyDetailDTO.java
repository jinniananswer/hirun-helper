package com.microtomato.hirun.modules.bss.salary.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 工程提成明细
 * @author: jinnian
 * @create: 2020-06-14 14:25
 **/
@Data
public class ProjectRoyaltyDetailDTO {

    private Long strategyId;

    private String nodeCondition;

    private String orgName;

    private Long employeeId;

    private String employeeName;

    private String jobRoleName;

    private String jobGrade;

    private String employeeStatus;

    private Long contractFee;

    private String value;

    private Long basicFee;

    private Long doorFee;

    private Long furnitureFee;

    private Long basicRoyalty;

    private Long doorRoyalty;

    private Long furnitureRoyalty;

    private Long basicAlreadyFetch;

    private Long doorAlreadyFetch;

    private Long furnitureAlreadyFetch;

    private Long thisMonthFetch;

    private String remark;
}
