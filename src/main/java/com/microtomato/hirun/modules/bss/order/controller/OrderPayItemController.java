package com.microtomato.hirun.modules.bss.order.controller;

import com.microtomato.hirun.modules.bss.order.service.IOrderPayItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 订单支付项明细表 前端控制器
 * </p>
 *
 * @author jinnian
 * @since 2020-02-21
 */
@RestController
@Slf4j
@RequestMapping("/api/bss.order/order-pay-item")
public class OrderPayItemController {

    @Autowired
    private IOrderPayItemService orderPayItemServiceImpl;



}
