package com.microtomato.hirun.modules.bss.salary.entity.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 员工补扣款信息查询结果DTO
 * @author: jinnian
 * @create: 2020-08-30 21:07
 **/
@Data
public class EmployeeRedebitDTO {

    private Long id;

    private Long employeeId;

    private Long orgId;

    private String orgPath;

    private String name;

    private String redebitItem;

    private String redebitItemName;

    private String salaryItem;

    private String salaryItemName;

    private Integer salaryMonth;

    private Double money;

    private Long inEmployeeId;

    private String inEmployeeName;

    private String auditStatus;

    private String auditStatusName;

    private Long auditEmployeeId;

    private String auditEmployeeName;

    private LocalDate inDate;

    private LocalDateTime auditTime;

    private String reason;
}
