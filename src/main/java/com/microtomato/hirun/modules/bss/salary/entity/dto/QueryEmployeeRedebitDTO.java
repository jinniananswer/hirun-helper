package com.microtomato.hirun.modules.bss.salary.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 员工补扣款查询条件DTO
 * @author: jinnian
 * @create: 2020-08-30 21:25
 **/
@Data
public class QueryEmployeeRedebitDTO {

    private String salaryItem;

    private String redebitItem;

    private String orgIds;

    private Long employeeId;

    private String auditStatus;

    private Integer salaryMonth;

    private Integer page;

    private Integer limit;

    private Integer count;
}
