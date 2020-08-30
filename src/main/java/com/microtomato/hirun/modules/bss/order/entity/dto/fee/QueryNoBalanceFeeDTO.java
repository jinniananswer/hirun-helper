package com.microtomato.hirun.modules.bss.order.entity.dto.fee;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 工地未收齐款项查询
 * @author: jinnian
 * @create: 2020-07-20 19:05
 **/
@Data
public class QueryNoBalanceFeeDTO {

    private Long orderId;

    private Integer periods;

    private String feeType;

    private String shopIds;

    private Long agent;

    private Long projectManager;

    private Long projectCharger;

    private Long cabinetDesigner;

    private Long materialManager;

    private Integer page;

    private Integer limit;

    private Integer count;
}
