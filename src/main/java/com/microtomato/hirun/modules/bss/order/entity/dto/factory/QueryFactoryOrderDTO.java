package com.microtomato.hirun.modules.bss.order.entity.dto.factory;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 查询工厂订单条件
 * @author: jinnian
 * @create: 2020-09-26 19:30
 **/
@Data
public class QueryFactoryOrderDTO {

    private String custName;

    private String mobileNo;

    private Long housesId;

    private String shopIds;

    private Long report;

    private Long agent;

    private Long designer;

    private Long projectManager;

    private Long projectCharger;

    private Long factoryOrderManager;

    private Integer page;

    private Integer limit;

    private Integer count;
}
