package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 订单任务查询条件数据对象
 * @author: jinnian
 * @create: 2020-07-11 11:27
 **/
@Data
public class OrderTaskQueryDTO {

    private String custName;

    private String mobileNo;

    private Long housesId;

    private String orderStatus;

    private Integer page;

    private Integer limit;

    private Integer count;
}
