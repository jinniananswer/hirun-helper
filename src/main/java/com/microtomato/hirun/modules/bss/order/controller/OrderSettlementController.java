package com.microtomato.hirun.modules.bss.order.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderSettlement;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.bss.order.service.IOrderSettlementService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2020-03-07
 */
@RestController
@Slf4j
@RequestMapping("/api/bss.order/order-settlement")
public class OrderSettlementController {

    @Autowired
    private IOrderSettlementService orderSettlementServiceImpl;

    @PostMapping("/saveOrderSettlement")
    @RestResult
    public void saveOrderSettlement(@RequestBody OrderSettlement orderSettlement){
          orderSettlementServiceImpl.saveOrderSettlement(orderSettlement);
    }

    @GetMapping("/queryOrderSettlement")
    @RestResult
    public OrderSettlement queryOrderSettlement(Long orderId){
        return orderSettlementServiceImpl.queryOrderSettlement(orderId);
    }

    @PostMapping("/submitCollectLastFee")
    @RestResult
    public void submitCollectLastFee(@RequestBody OrderSettlement orderSettlement){
        orderSettlementServiceImpl.submitCollectLastFee(orderSettlement);
    }
}
