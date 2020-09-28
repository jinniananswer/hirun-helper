package com.microtomato.hirun.modules.bss.order.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderMeasureHouseDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderMeasureHouse;
import com.microtomato.hirun.modules.bss.order.service.IOrderMeasureHouseService;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author ：xiaocl
 * @date ：Created in 2020/2/4 20:56
 * @description：量房控制器
 * @modified By：
 * @version: 1$
 */
@RestController
@Slf4j
@RequestMapping("/api/bss.order/order-measurehouse")
public class OrderMeasureHouseController {

    @Autowired
    private IOrderMeasureHouseService orderMeasureHouseServiceImpl;

    @Autowired
    private IEmployeeService employeeService;

    @PostMapping("/submitToPlanesketchFlow")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @RestResult
    public void submitToPlanesketchFlow(@RequestBody OrderMeasureHouseDTO orderMeasureHouse) {
        this.save(orderMeasureHouse);
        orderMeasureHouseServiceImpl.submitToPlanesketchFlow(orderMeasureHouse.getOrderId());
    }

    @PostMapping("/submitToOnlyWoodworkFlow")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @RestResult
    public void submitToOnlyWoodworkFlow(@RequestBody OrderMeasureHouse orderMeasureHouse) {
        orderMeasureHouseServiceImpl.submitToPlanesketchFlow(orderMeasureHouse.getOrderId());
    }

    @PostMapping("/saveMeasureHouseInfos")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @RestResult
    public void save(@RequestBody OrderMeasureHouseDTO dto) {
        orderMeasureHouseServiceImpl.saveMeasureHouseInfos(dto);
    }

    @PostMapping("/submitToSneakFlow")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @RestResult
    public void submitToSneakFlow(@RequestBody OrderMeasureHouse orderMeasureHouse) {
        orderMeasureHouseServiceImpl.submitToSneakFlow(orderMeasureHouse.getOrderId());
    }

    @PostMapping("/submitToMeasureSuspendFlow")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @RestResult
    public void submitToMeasureSuspendFlow(@RequestBody OrderMeasureHouse orderMeasureHouse) {
        orderMeasureHouseServiceImpl.submitToMeasureSuspendFlow(orderMeasureHouse.getOrderId());
    }

    @GetMapping("/getMeasureHouse")
    @RestResult
    public OrderMeasureHouseDTO getMeasureHouse(Long orderId) {
        return orderMeasureHouseServiceImpl.getMeasureHouse(orderId);
    }
}
