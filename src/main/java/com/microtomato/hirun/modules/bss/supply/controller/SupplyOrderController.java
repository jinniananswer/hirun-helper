package com.microtomato.hirun.modules.bss.supply.controller;



import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.supply.entity.dto.SupplyOrderDTO;
import com.microtomato.hirun.modules.bss.supply.service.ISupplyOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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

    /**
     * 下单新增
     */
    @PostMapping("/materialOrderDeal")
    @RestResult
    public Map materialOrderDeal(@RequestBody SupplyOrderDTO supplyOrderInfo) {
        System.out.println("supplyOrderInfo=========="+supplyOrderInfo);
        this.supplyOrderService.materialOrderDeal(supplyOrderInfo);
        return new HashMap();
    }

}