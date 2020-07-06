package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * @program: hirun-helper
 * @description: 非主营收费查询条件数据传输对象
 * @author: sunxin
 * @create: 2020-04-1
 **/
@Data
public class NonCollectFeeQueryDTO {


    private Long employeeId;

    private LocalDate startDate;

    private LocalDate endDate;

    private String auditStatus;

    private Integer page;

    private Integer limit;

    private Integer count;
}
