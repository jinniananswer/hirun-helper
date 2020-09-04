package com.microtomato.hirun.modules.bss.salary.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: hirun-helper
 * @description: 员工工资表数据传输对象
 * @author: jinnian
 * @create: 2020-05-02 01:10
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalaryMonthlyDTO {

    private Long employeeId;

    private Long id;

    private Integer salaryMonth;

    private String name;

    private String orgPath;

    private Long orgId;

    private String orgName;

    private String jobRoleName;

    private String jobRole;

    private String type;

    private String typeName;

    private String status;

    private String statusName;

    private Double basic;

    private Double rank;

    private Double job;

    private Double performance;

    private Double fullTime;

    private Double duty;

    private Double overtime;

    private Double performanceAssess;

    private Double floatAward;

    private Double other;

    private Double backPay;

    private Double royalty;

    private Double medical;

    private Double overage;

    private Double unemployment;

    private Double seriousIll;

    private Double debit;

    private Double companyPart;

    private Double tax;

    private Double vacation;

    private Double late;

    private Double notice;

    private String remark;

    private String auditStatus;

    private String auditStatusName;

    private String auditRemark;

    private Long auditEmployeeId;

    private String isModified;
}
