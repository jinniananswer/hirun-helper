package com.microtomato.hirun.modules.bss.order.controller;



import com.microtomato.hirun.modules.bss.order.service.IOrderPayNoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单支付流水表表(OrderPayNo)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-02-29 11:59:42
 */
@RestController
@RequestMapping("/api/OrderPayNo")
public class OrderPayNoController {

    /**
     * 服务对象
     */
    @Autowired
    private IOrderPayNoService orderPayNoService;

}