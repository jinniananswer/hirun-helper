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

    private Long orderId;

    private String orderStatus;

    private Long strategyId;

    private String nodeCondition;

    private Long orgId;

    private String orgName;

    private Long employeeId;

    private String employeeName;

    private String jobRole;

    private String jobRoleName;

    private String jobGrade;

    private String jobGradeName;

    private String employeeStatus;

    private String employeeStatusName;

    private Long contractFee;

    private Integer periods;

    private String periodName;

    private String value;

    private Long basicId;

    private Long doorId;

    private Long furnitureId;

    private Double basicFee;

    private Double doorFee;

    private Double furnitureFee;

    private Double basicRoyalty;

    private Double doorRoyalty;

    private Double furnitureRoyalty;

    private Double basicAlreadyFetch;

    private Double doorAlreadyFetch;

    private Double furnitureAlreadyFetch;

    private Double thisMonthFetch;

    private Integer salaryMonth;

    private String remark;

    private String auditStatus;

    private String auditStatusName;

    private String auditRemark;

    private String isModified;
}
