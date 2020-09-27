package com.microtomato.hirun.modules.bss.order.controller;



import com.microtomato.hirun.modules.bss.order.service.IOrderFactoryOrderFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * (OrderFactoryOrderFollow)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-09-26 16:32:41
 */
@RestController
@RequestMapping("/api/bss/order/order-factory-order-follow")
public class OrderFactoryOrderFollowController {

    /**
     * 服务对象
     */
    @Autowired
    private IOrderFactoryOrderFollowService orderFactoryOrderFollowService;


}