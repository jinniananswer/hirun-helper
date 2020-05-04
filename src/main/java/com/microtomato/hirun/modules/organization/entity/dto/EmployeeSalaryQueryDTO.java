package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 员工工资查询数据传输对象
 * @author: jinnian
 * @create: 2020-05-02 14:38
 **/
@Data
public class EmployeeSalaryQueryDTO {

    private String name;

    private String mobileNo;

    private List<Long> orgIds;

    private String orgId;

    private String type;

    private String status;

    private String auditStatus;

    private Integer salaryMonth;
}
