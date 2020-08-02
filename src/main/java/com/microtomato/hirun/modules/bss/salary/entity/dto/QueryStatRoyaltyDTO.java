package com.microtomato.hirun.modules.bss.salary.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 统计提成明细查询条件DTO
 * @author: jinnian
 * @create: 2020-07-15 23:40
 **/
@Data
public class QueryStatRoyaltyDTO {

    private String custName;

    private String mobileNo;

    private Long housesId;

    private String shopIds;

    private String feeTime;

    private Integer page;

    private Integer limit;

    private Integer count;
}
