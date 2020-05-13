package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: hirun-helper
 * @description: 员工工资固定项数据传输对象
 * @author: jinnian
 * @create: 2020-05-05 00:00
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSalaryFixDTO {

    private Long employeeId;

    private Long id;

    private String bankAcctOne;

    private String bankAcctTwo;

    private String bankAcctThree;

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

    private Double performance;

    private Double duty;

    private Double overtime;

    private Double floatAward;

    private Double other;

    private Double backPay;

    private Double medical;

    private Double overage;

    private Double unemployment;

    private Double seriousIll;

    private Double tax;

    private String remark;

    private String auditStatus;

    private String auditStatusName;

    private String auditRemark;

    private Long auditEmployeeId;

    private String isModified;
}
