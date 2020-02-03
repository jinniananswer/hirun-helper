package com.microtomato.hirun.modules.bss.order.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderInfoDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerDTO;
import com.microtomato.hirun.modules.bss.order.service.IOrderBaseService;
import com.microtomato.hirun.modules.bss.order.service.IOrderDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 订单主表 前端控制器
 * </p>
 *
 * @author jinnian
 * @since 2020-02-03
 */
@RestController
@Slf4j
@RequestMapping("/api/bss.order/order-base")
public class OrderBaseController {

    @Autowired
    private IOrderBaseService orderBaseServiceImpl;

    @Autowired
    private IOrderDomainService orderDomainService;

    @GetMapping("/getOrderInfo")
    @RestResult
    public OrderInfoDTO getOrderInfo(Long orderId) {
        return this.orderDomainService.getOrderInfo(orderId);
    }

    @GetMapping("/getOrderWorkers")
    @RestResult
    public List<OrderWorkerDTO> queryOrderWorkers(Long orderId) {
        return this.orderDomainService.queryOrderWorkers(orderId);
    }
}
