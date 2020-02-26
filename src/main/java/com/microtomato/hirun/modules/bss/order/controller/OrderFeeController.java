package com.microtomato.hirun.modules.bss.order.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustConsultDTO;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustPreparationDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderConsult;
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

    @PostMapping("/loadProjectFeeInfo")
    @RestResult
    public OrderFeeDTO loadProjectFeeInfo(Long orderId,String period){
        System.out.print("orderId=====rrrrrrrrr===="+orderId);
        System.out.print("period=====rrrrrrrrr===="+period);
        return orderFeeServiceImpl.loadDesignFeeInfo(orderId);
    }

    @PostMapping("/auditUpdateForDesign")
    @RestResult
    public void auditUpdateForDesign(OrderFeeDTO orderFee){
        System.out.print("orderFee=====gggggg===="+orderFee);
        orderFeeServiceImpl.auditUpdateForTotal(orderFee);
    }

    @PostMapping("/auditUpdateForProject")
    @RestResult
    public void auditUpdateForProject(OrderFeeDTO orderFee){
        System.out.print("orderFee=====gggggg===="+orderFee);
        orderFeeServiceImpl.auditUpdateForTotal(orderFee);
    }

//    @PostMapping("/auditUpdate")
//    @RestResult
//    public void auditUpdate(OrderFeeDTO orderFee){
//        System.out.print("orderFee=====gggggg===="+orderFee);
//        orderFeeServiceImpl.auditUpdate(orderFee);
//    }

//初始化方法
    @GetMapping("/queryOrderConsult")
    @RestResult
    public OrderFee queryOrderCollectFee(Long orderId){
        return orderFeeServiceImpl.queryOrderCollectFee(orderId);
    }
//费用审核
    @PostMapping("/submitAudit")
    @RestResult
    public void submitAudit(OrderFeeDTO dto) {
        orderFeeServiceImpl.submitAudit(dto);
    }

}
