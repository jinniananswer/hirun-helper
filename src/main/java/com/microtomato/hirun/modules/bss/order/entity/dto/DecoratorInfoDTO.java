package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 订单费用数据传输对象
 * @author: sunxin
 * @create: 2020-02-05
 **/
@Data
public class DecoratorInfoDTO {

    private String name;

    private String identityNo;

    private String mobileNo;

    private String decoratorType;

    private String agreementTag;
}
