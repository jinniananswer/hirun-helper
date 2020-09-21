package com.microtomato.hirun.modules.bss.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderGarrisonDesign;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.bss.order.service.IOrderGarrisonDesignService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2020-03-04
 */
@RestController
@Slf4j
@RequestMapping("/api/bss.order/order-garrison-design")
public class OrderGarrisonDesignController {

    @Autowired
    private IOrderGarrisonDesignService orderGarrisonDesignServiceImpl;

    @GetMapping("queryGarrisonDesignInfo")
    @RestResult
    public OrderGarrisonDesign queryGarrisonDesignInfo(Long orderId){
        OrderGarrisonDesign orderGarrisonDesign=orderGarrisonDesignServiceImpl.getOne(new QueryWrapper<OrderGarrisonDesign>().lambda()
                .eq(OrderGarrisonDesign::getOrderId,orderId));
        if(orderGarrisonDesign!=null){
            orderGarrisonDesign.setCreateTime(null);
            orderGarrisonDesign.setUpdateTime(null);
        }
        return orderGarrisonDesign;
    }

    @PostMapping("saveGarrisonDesignInfo")
    @RestResult
    public void saveGarrisonDesignInfo(@RequestBody OrderGarrisonDesign orderGarrisonDesign){
        orderGarrisonDesignServiceImpl.saveGarrisonDesignInfo(orderGarrisonDesign);
    }

    @PostMapping("submitBudget")
    @RestResult
    public void submitBudget(@RequestBody OrderGarrisonDesign orderGarrisonDesign){
        orderGarrisonDesignServiceImpl.submitBudget(orderGarrisonDesign);
    }
}
