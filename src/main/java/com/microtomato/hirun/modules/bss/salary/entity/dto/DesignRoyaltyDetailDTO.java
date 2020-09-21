package com.microtomato.hirun.modules.bss.salary.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 通用提成明细
 * @author: jinnian
 * @create: 2020-06-14 14:25
 **/
@Data
public class DesignRoyaltyDetailDTO {

    private Long id;

    private Long custId;

    private String custName;

    private String custNo;

    private Long orderId;

    private Long strategyId;

    private String orderStatus;

    private String nodeCondition;

    private String orgName;

    private Long employeeId;

    private Long orgId;

    private String employeeName;

    private String jobRole;

    private String jobRoleName;

    private String jobGrade;

    private String jobGradeName;

    private String employeeStatusName;

    private String type;

    private String typeName;

    private String item;

    private String itemName;

    private Integer designFeeStandard;

    private Double designFee;

    private String value;

    private Double totalRoyalty;

    private Double alreadyFetch;

    private Double thisMonthFetch;

    private Integer salaryMonth;

    private String auditStatus;

    private String auditStatusName;

    private String remark;

    private String auditRemark;

    private String isModified;
}
