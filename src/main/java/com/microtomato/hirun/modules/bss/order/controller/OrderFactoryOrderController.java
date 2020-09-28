package com.microtomato.hirun.modules.bss.order.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.order.entity.dto.factory.FactoryOrderDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.factory.FactoryOrderFollowDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.factory.FactoryOrderInfoDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.factory.QueryFactoryOrderDTO;
import com.microtomato.hirun.modules.bss.order.service.IOrderFactoryOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * (OrderFactoryOrder)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-09-26 16:32:41
 */
@RestController
@RequestMapping("/api/bss/order/order-factory-order")
public class OrderFactoryOrderController {

    /**
     * 服务对象
     */
    @Autowired
    private IOrderFactoryOrderService orderFactoryOrderService;

    @GetMapping("/queryFactoryOrders")
    @RestResult
    public IPage<FactoryOrderInfoDTO> queryFactoryOrders(QueryFactoryOrderDTO condition) {
        return this.orderFactoryOrderService.queryFactoryOrders(condition);
    }

    @GetMapping("/getFactoryOrder")
    @RestResult
    public FactoryOrderDTO getFactoryOrder(Long orderId) {
        return this.orderFactoryOrderService.getFactoryOrderDTO(orderId);
    }

    @PostMapping("/saveFollows")
    @RestResult
    public void saveFollows(@RequestBody List<FactoryOrderFollowDTO> follows) {
        this.orderFactoryOrderService.saveFollows(follows);
    }

    @PostMapping("/saveFactoryOrder")
    @RestResult
    public void saveFactoryOrder(@RequestBody FactoryOrderDTO factoryOrder) {
        this.orderFactoryOrderService.saveFactoryOrder(factoryOrder);
    }

    @PostMapping("/closeFactoryOrder")
    @RestResult
    public void closeFactoryOrder(@RequestBody FactoryOrderDTO factoryOrder) {
        this.orderFactoryOrderService.closeFactoryOrder(factoryOrder);
    }
}