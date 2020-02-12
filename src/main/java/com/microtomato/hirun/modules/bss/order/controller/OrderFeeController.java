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
import java.util.Map;

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

    @PostMapping("/addDesignFee")
    @RestResult
    public void addDesignFee(@RequestBody OrderFeeDTO orderFee) {
        System.out.println("orderFee=========="+orderFee.toString());
        orderFee.setOrderId((long)7);//测试用
        orderFeeServiceImpl.addDesignFee(orderFee);
    }

    @PostMapping("/addDownPayment")
    @RestResult
    public void addDownPayment(@RequestBody OrderFeeDTO orderFee) {
        log.debug(orderFee.toString());
        orderFee.setOrderId((long)7);//测试用
        orderFeeServiceImpl.addDownPayment(orderFee);
    }

    @PostMapping("/loadDesignFeeInfo")
    @RestResult
    public OrderFeeDTO loadDesignFeeInfo(Long orderId){
        System.out.print("orderId=====rrrrrrrrr===="+orderId);
        return orderFeeServiceImpl.loadDesignFeeInfo(orderId);
    }

    @PostMapping("/auditUpdate")
    @RestResult
    public void auditUpdate(OrderFeeDTO orderFee){
        System.out.print("orderFee=====gggggg===="+orderFee);
        orderFeeServiceImpl.auditUpdate(orderFee);
    }


}
