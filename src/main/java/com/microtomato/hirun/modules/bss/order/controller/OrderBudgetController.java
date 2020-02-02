package com.microtomato.hirun.modules.bss.order.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderBudget;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.bss.order.service.IOrderBudgetService;

/**
 * <p>
 * 订单预算表 前端控制器
 * </p>
 *
 * @author anwenxuan
 * @since 2020-01-29
 */
@RestController
@Slf4j
@RequestMapping("/api/bss.order/order-budget")
public class OrderBudgetController {

    @Autowired
    private IOrderBudgetService orderBudgetServiceImpl;

    @PostMapping("/submitBudget")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitBudget(OrderBudget orderBudget) {
//        orderBudgetServiceImpl.save(orderBudget);
    }

    @PostMapping("/submitBudgetCheckedResult")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitBudgetCheckedResult(OrderBudget orderBudget) {
        orderBudgetServiceImpl.updateById(orderBudget);
    }

    @PostMapping("/getBudgetById")
    @RestResult
    public OrderBudget getBudgetById(String id) {
        return orderBudgetServiceImpl.getById(id);
    }
}
