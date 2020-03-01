package com.microtomato.hirun.modules.bss.order.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderDiscountItemDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.bss.order.service.IOrderDiscountItemService;

import java.util.List;

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

    @GetMapping("list")
    @RestResult
    public List<OrderDiscountItemDTO> list(Long orderId) {
        return orderDiscountItemServiceImpl.list(orderId);
    }

    @PostMapping("save")
    @RestResult
    public void save(@RequestBody List<OrderDiscountItemDTO> dtoList) {
        log.info("=====================");
        log.info("=====================");
        log.info("{}", dtoList);
//        orderDiscountItemServiceImpl.save(dtoList);
        return;
    }
}
