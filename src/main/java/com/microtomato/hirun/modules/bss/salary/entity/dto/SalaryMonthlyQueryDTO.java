package com.microtomato.hirun.modules.bss.salary.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 员工工资查询数据传输对象
 * @author: jinnian
 * @create: 2020-05-02 14:38
 **/
@Data
public class SalaryMonthlyQueryDTO {

    private String name;

    private String mobileNo;

    private String orgIds;

    private String type;

    private String status;

    private String auditStatus;

    private Integer salaryMonth;
}
