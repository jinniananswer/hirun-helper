package com.microtomato.hirun.modules.bss.salary.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 员工工资固定项目查询
 * @author: jinnian
 * @create: 2020-05-05 00:09
 **/
@Data
public class SalaryFixQueryDTO {

    private String name;

    private String mobileNo;

    private String orgIds;

    private String type;

    private String status;

    private String auditStatus;
}
