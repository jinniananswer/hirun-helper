package com.microtomato.hirun.modules.bss.order.controller;



import com.microtomato.hirun.modules.bss.order.service.IOrderInspectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * (OrderInspect)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-08-11 18:07:44
 */
@RestController
@RequestMapping("/api/bss.order/order-inspect")
public class OrderInspectController {

    /**
     * 服务对象
     */
    @Autowired
    private IOrderInspectService orderInspectService;


}