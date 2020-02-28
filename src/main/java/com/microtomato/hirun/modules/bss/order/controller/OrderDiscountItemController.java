package com.microtomato.hirun.modules.bss.order.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.bss.order.service.IOrderDiscountItemService;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 订单优惠项 前端控制器
 * </p>
 *
 * @author anwx
 * @since 2020-02-26
 */
@RestController
@Slf4j
@RequestMapping("/api/bss.order/order-discount-item")
public class OrderDiscountItemController {

    @Autowired
    private IOrderDiscountItemService orderDiscountItemServiceImpl;



}
