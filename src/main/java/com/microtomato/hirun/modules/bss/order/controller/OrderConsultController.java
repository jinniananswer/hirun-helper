package com.microtomato.hirun.modules.bss.order.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustConsultDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderConsult;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.bss.order.service.IOrderConsultService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2020-02-15
 */
@RestController
@Slf4j
@RequestMapping("/api/order/order-consult")
public class OrderConsultController {

    @Autowired
    private IOrderConsultService orderConsultServiceImpl;

    @GetMapping("/queryOrderConsult")
    @RestResult
    public OrderConsult queryOrderConsult(Long orderId){
        return orderConsultServiceImpl.queryOrderConsult(orderId);
    }

    @GetMapping("/queryOrderConsultForTrans")
    @RestResult
    public CustConsultDTO queryOrderConsultForTrans(Long orderId){
        return orderConsultServiceImpl.queryOrderConsultForTrans(orderId);
    }

    @PostMapping("/saveCustomerConsultInfo")
    @RestResult
    public void saveCustomerConsultInfo(@RequestBody CustConsultDTO dto) {
        orderConsultServiceImpl.saveCustomerConsultInfo(dto);
    }

    @PostMapping("/submitMeasure")
    @RestResult
    public void submitMeasure(@RequestBody CustConsultDTO dto) {
        orderConsultServiceImpl.submitMeasure(dto);
    }


    @PostMapping("/submitSneak")
    @RestResult
    public void submitSneak(@RequestBody CustConsultDTO dto) {
        orderConsultServiceImpl.submitSneak(dto);
    }

    @PostMapping("/transOrder")
    @RestResult
    public void transOrder(@RequestBody CustConsultDTO dto) {
        orderConsultServiceImpl.submitMeasure(dto);
    }
}
