package com.microtomato.hirun.modules.bss.supply.controller;



import com.microtomato.hirun.modules.bss.supply.service.ISupplyOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 供应订单表(SupplyOrder)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-15 11:26:08
 */
@RestController
@RequestMapping("/api/bss.supply/supply-order")
public class SupplyOrderController {

    /**
     * 服务对象
     */
    @Autowired
    private ISupplyOrderService supplyOrderService;

}