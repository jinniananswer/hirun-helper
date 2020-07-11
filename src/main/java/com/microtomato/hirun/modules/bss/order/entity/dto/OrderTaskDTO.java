package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 订单任务数据对象
 * @author: jinnian
 * @create: 2020-07-11 11:16
 **/
@Data
public class OrderTaskDTO {

    private Long custId;

    private String custName;

    private String mobileNo;

    private Long orderId;

    private String decorateAddress;

    private String status;

    private String statusName;

    private String indoorArea;

    private String houseLayout;

    private String houseLayoutName;

    private String type;

    private String typeName;

    private Long housesId;

    private String housesName;

    private String pageUrl;
}
