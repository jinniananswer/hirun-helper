package com.microtomato.hirun.modules.bss.order.service;

import com.microtomato.hirun.modules.bss.order.entity.dto.OrderInfoDTO;

/**
 * @program: hirun-helper
 * @description: 订单领域服务接口类
 * @author: jinnian
 * @create: 2020-02-03 01:34
 **/
public interface IOrderDomainService {

    OrderInfoDTO getOrderInfo(Long orderId);
}
