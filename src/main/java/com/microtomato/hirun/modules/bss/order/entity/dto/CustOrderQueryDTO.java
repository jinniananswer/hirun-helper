package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 客户订单查询条件数据传输对象
 * @author: jinnian
 * @create: 2020-02-15 22:17
 **/
@Data
public class CustOrderQueryDTO {

    private String custName;

    private String sex;

    private String mobileNo;

    private Long housesId;

    private String orderStatus;

    private Integer page;

    private Integer limit;

    private Integer count;
}
