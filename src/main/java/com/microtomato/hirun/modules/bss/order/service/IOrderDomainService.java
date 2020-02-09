package com.microtomato.hirun.modules.bss.order.service;

import com.microtomato.hirun.modules.bss.order.entity.dto.NewOrderDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderInfoDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderBase;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 订单领域服务接口类
 * @author: jinnian
 * @create: 2020-02-03 01:34
 **/
public interface IOrderDomainService {

    OrderInfoDTO getOrderInfo(Long orderId);

    List<OrderWorkerDTO> queryOrderWorkers(Long orderId);

    void createNewOrder(NewOrderDTO newOrder);

    void orderStatusTrans(Long orderId, String oper);

    void orderStatusTrans(OrderBase order, String oper);
}
