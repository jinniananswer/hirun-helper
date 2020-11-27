package com.microtomato.hirun.modules.finance.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 复核领款单查询条件对象
 * @author: jinnian
 * @create: 2020-11-22 17:41
 **/
@Data
public class QueryVoucherDTO {

    private String type;

    private String[] voucherDate;

    private Long voucherEmployeeId;

    private String auditStatus;

    private Integer page;

    private Integer limit;

    private Integer count;
}
