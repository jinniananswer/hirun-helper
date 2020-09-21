package com.microtomato.hirun.modules.bss.order.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderEscapeDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderEscape;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.bss.order.service.IOrderEscapeService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2020-02-14
 */
@RestController
@Slf4j
@RequestMapping("/api/order/order-escape")
public class OrderEscapeController {

    @Autowired
    private IOrderEscapeService orderEscapeServiceImpl;

    @PostMapping("/save")
    @RestResult
    public void save(@RequestBody OrderEscape orderEscape){
        orderEscapeServiceImpl.saveOrderEscape(orderEscape);
    }

    @GetMapping("/getEscapeInfo")
    @RestResult
    public OrderEscapeDTO getEscapeInfo(Long orderId){
        return orderEscapeServiceImpl.getEscapeInfo(orderId);
    }

    @PostMapping("submitDirectorAudit")
    @RestResult
    public void submitDirectorAudit(@RequestBody OrderEscape orderEscape){
        orderEscapeServiceImpl.submitDirectorAudit(orderEscape);
    }

    @PostMapping("submitBack")
    @RestResult
    public void submitBack(@RequestBody OrderEscape orderEscape){
        orderEscapeServiceImpl.submitBack(orderEscape);
    }

    @PostMapping("/closeOrder")
    @RestResult
    public void closeOrder(@RequestBody OrderEscape orderEscape){
        orderEscapeServiceImpl.closeOrder(orderEscape);
    }

    @PostMapping("/auditBack")
    @RestResult
    public void auditBack(@RequestBody OrderEscape orderEscape){
        orderEscapeServiceImpl.auditBack(orderEscape);
    }

}
