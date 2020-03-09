package com.microtomato.hirun.modules.bss.order.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderMeasureHouse;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPlaneSketch;
import com.microtomato.hirun.modules.bss.order.service.IOrderPlaneSketchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：mmzs
 * @date ：Created in 2020/2/6 20:13
 * @description：订单平面图控制器
 * @modified By：
 * @version: 1$
 */
@RestController
@Slf4j
@RequestMapping("/api/bss.order/order-planSketch")
public class OrderPlaneSketchController {

    @Autowired
    private IOrderPlaneSketchService orderPlaneSketchServiceImpl;

    @PostMapping("/submitPlaneSketch")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submit(OrderPlaneSketch orderPlaneSketch) {
        log.debug("OrderPlaneSketchController"+orderPlaneSketch.toString());
        orderPlaneSketchServiceImpl.save(orderPlaneSketch);
    }

    @PostMapping("/submitToConfirmFlow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitToConfirmFlow(OrderPlaneSketch orderPlaneSketch) {
        log.debug("orderPlaneSketch"+orderPlaneSketch.getOrderId());
        orderPlaneSketchServiceImpl.submitToConfirmFlow(orderPlaneSketch.getOrderId());
    }

    @PostMapping("/submitToSignContractFlow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitToSignContractFlow(OrderPlaneSketch orderPlaneSketch) {
        log.debug("orderPlaneSketch"+orderPlaneSketch.getOrderId());
        orderPlaneSketchServiceImpl.submitToSignContractFlow(orderPlaneSketch.getOrderId());
    }

    @PostMapping("/updateOrderWork")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void updateOrderWork(Long orderId,Long roleId,Long employeeId) {
        log.debug("orderId_employeeId"+employeeId+"|"+orderId);
        orderPlaneSketchServiceImpl.updateOrderWork(orderId,roleId,employeeId);
    }

    @PostMapping("/submitToDelayTimeFlow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitToDelayTimeFlow(OrderPlaneSketch orderPlaneSketch) {
        log.debug("orderPlaneSketch"+orderPlaneSketch.getOrderId());
        orderPlaneSketchServiceImpl.submitToDelayTimeFlow(orderPlaneSketch.getOrderId());
    }

    @PostMapping("/submitToBackToDesignerFlow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitToBackToDesignerFlow(OrderPlaneSketch orderPlaneSketch) {
        orderPlaneSketchServiceImpl.submitToBackToDesignerFlow(orderPlaneSketch.getOrderId());
    }
}
