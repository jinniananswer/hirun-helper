package com.microtomato.hirun.modules.bss.salary.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 员工工资提成明细数据对象
 * @author: jinnian
 * @create: 2020-06-14 21:44
 **/
@Data
public class EmployeeSalaryRoyaltyDetailDTO {

    private Long strategyId;

    private String type;

    private String item;

    private Long employeeId;

    private Long totalRoyalty;

    private Long alreadyFetch;

    private Long thisMonthFetch;

    private Integer salaryMonth;

    private String employeeName;

    private String employeeStatus;

    private Long orgId;

    private String orgName;

    private String jobRole;

    private String jobGrade;

    private String auditStatus;

    private String auditRemark;

    private String remark;
}
