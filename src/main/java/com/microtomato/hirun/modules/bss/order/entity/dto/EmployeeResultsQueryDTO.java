package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 员工业绩查询条件
 * @author: jinnian
 * @create: 2020-02-15 17:02
 **/
@Data
public class EmployeeResultsQueryDTO {

    private Long roleId;

    private Long employeeId;

    private LocalDateTime orderCreateStartDate;

    private LocalDateTime orderCreateEndDate;
}
