package com.microtomato.hirun.modules.bss.order.entity.dto.fee;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 工程款查询条件
 * @author: jinnian
 * @create: 2020-07-12 02:18
 **/
@Data
public class QueryProjectFeeDTO {

    private String custName;

    private String mobileNo;

    private Long housesId;

    private String shopIds;

    private Long agent;

    private Long designer;

    private Long projectCharger;

    private Long projectManager;

    private Integer page;

    private Integer limit;

    private Integer count;
}
