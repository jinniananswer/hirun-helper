package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 订单收款项信息展示
 * @author: jinnian
 * @create: 2020-03-07 17:29
 **/
@Data
public class OrderPayItemInfoDTO {

    private String payItemName;

    private Double money;
}
