package com.microtomato.hirun.modules.bss.salary.entity.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * @program: hirun-helper
 * @description: 新增员工补扣款数据传输对象
 * @author: jinnian
 * @create: 2020-08-31 15:39
 **/
@Data
public class CreateEmployeeRedebitDTO {

    private Long employeeId;

    private String salaryItem;

    private String redebitItem;

    private Double money;

    private Integer salaryMonth;

    private LocalDate inDate;

    private String reason;
}
