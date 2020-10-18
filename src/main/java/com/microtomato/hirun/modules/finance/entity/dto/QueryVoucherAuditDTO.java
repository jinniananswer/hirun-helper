package com.microtomato.hirun.modules.finance.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 供应查询条件
 * @author: sunxin
 * @create: 2020-07-19 12:18
 **/
@Data
public class QueryVoucherAuditDTO {


    private Long employeeId;

    private Long supplierId;

    private Long supplyId;

    private Integer page;

    private Integer limit;

    private Integer count;
}
