package com.microtomato.hirun.modules.bss.order.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustPreparationDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFee;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.bss.order.service.IOrderFeeService;

import java.util.List;

/**
 * <p>
 * 订单费用表 前端控制器
 * </p>
 *
 * @author sunxin
 * @since 2020-02-05
 */
@RestController
@Slf4j
@RequestMapping("/api/bss/order/order-fee")
public class OrderFeeController {

    @Autowired
    private IOrderFeeService orderFeeServiceImpl;

    @PostMapping("/addOrderFee")
    @RestResult
    public void addOrderFee(@RequestBody OrderFeeDTO orderFee) {
        log.debug(orderFee.toString());
        orderFeeServiceImpl.addOrderFee(orderFee);
    }




}
