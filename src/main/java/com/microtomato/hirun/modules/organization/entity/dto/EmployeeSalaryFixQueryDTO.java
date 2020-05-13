package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 员工工资固定项目查询
 * @author: jinnian
 * @create: 2020-05-05 00:09
 **/
@Data
public class EmployeeSalaryFixQueryDTO {

    private String name;

    private String mobileNo;

    private List<Long> orgIds;

    private String orgId;

    private String type;

    private String status;

    private String auditStatus;
}
