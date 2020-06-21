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

    private Long strategyId;

    private String nodeCondition;

    private String orgName;

    private String employeeName;

    private String jobRoleName;

    private String jobGradeName;

    private String employeeStatusName;

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
}
