package com.microtomato.hirun.modules.bss.order.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderMeasureHouseDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderPlaneSketchDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderMeasureHouse;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPlaneSketch;
import com.microtomato.hirun.modules.bss.order.service.IOrderMeasureHouseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/submitToPlanesketchFlow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitToPlanesketchFlow(@RequestBody OrderMeasureHouse orderMeasureHouse) {
        orderMeasureHouseServiceImpl.submitToPlanesketchFlow(orderMeasureHouse.getOrderId());
    }

    @PostMapping("/submitToOnlyWoodworkFlow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitToOnlyWoodworkFlow(@RequestBody OrderMeasureHouse orderMeasureHouse) {
        log.debug("getOrderId"+orderMeasureHouse.getOrderId());
        orderMeasureHouseServiceImpl.submitToPlanesketchFlow(orderMeasureHouse.getOrderId());
    }

    @PostMapping("/saveMeasureHouseInfos")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void save(@RequestBody OrderMeasureHouse orderMeasureHouse) {
        orderMeasureHouseServiceImpl.save(orderMeasureHouse);
    }

    @PostMapping("/submitToSneakFlow")
    @RestResult
    public void submitToSneakFlow(@RequestBody OrderMeasureHouse orderMeasureHouse) {
        orderMeasureHouseServiceImpl.submitToSneakFlow(orderMeasureHouse.getOrderId());
    }

    @PostMapping("/submitToMeasureSuspendFlow")
    @RestResult
    public void submitToMeasureSuspendFlow(@RequestBody OrderMeasureHouse orderMeasureHouse) {
        orderMeasureHouseServiceImpl.submitToMeasureSuspendFlow(orderMeasureHouse.getOrderId());
    }

    @GetMapping("/getMeasureHouse")
    @RestResult
    public OrderMeasureHouseDTO getMeasureHouse(Long orderId) {
        UserContext userContext = WebContextUtils.getUserContext();
        Long employeeId = userContext.getEmployeeId();
        OrderMeasureHouse orderMeasureHouse =  orderMeasureHouseServiceImpl.getMeasureHouse(orderId);
        OrderMeasureHouseDTO orderMeasureHouseDTO = new OrderMeasureHouseDTO();
        if (orderMeasureHouse != null) {
            BeanUtils.copyProperties(orderMeasureHouse,orderMeasureHouseDTO);
        }
        orderMeasureHouseDTO.setDesigner(employeeId);
        return orderMeasureHouseDTO;
    }
}
