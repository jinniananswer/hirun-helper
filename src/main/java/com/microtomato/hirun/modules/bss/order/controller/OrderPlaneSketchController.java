package com.microtomato.hirun.modules.bss.order.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderMeasureHouse;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPlaneSketch;
import com.microtomato.hirun.modules.bss.order.service.IOrderPlaneSketchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
    public void submit(@RequestBody OrderPlaneSketch orderPlaneSketch) {
        log.debug("OrderPlaneSketchController"+orderPlaneSketch.toString());
        orderPlaneSketchServiceImpl.save(orderPlaneSketch);
    }

    @PostMapping("/submitToConfirmFlow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitToConfirmFlow(@RequestBody OrderPlaneSketch orderPlaneSketch) {
        log.debug("orderPlaneSketch"+orderPlaneSketch.getOrderId());
        orderPlaneSketchServiceImpl.submitToConfirmFlow(orderPlaneSketch.getOrderId());
    }

    @PostMapping("/submitToSignContractFlow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitToSignContractFlow(@RequestBody OrderPlaneSketch orderPlaneSketch) {
        orderPlaneSketchServiceImpl.submitToSignContractFlow(orderPlaneSketch.getOrderId());
    }

    @GetMapping("/updateOrderWork")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void updateOrderWork(Long orderId,Long roleId,Long employeeId) {
        log.debug("orderId_employeeId"+employeeId+"|"+orderId);
        orderPlaneSketchServiceImpl.updateOrderWork(orderId,roleId,employeeId);
    }

    @GetMapping("/getPlaneSketch")
    @RestResult
    public OrderPlaneSketch getPlaneSketch(Long orderId) {
        log.debug("orderPlaneSketchServiceImpl"+orderPlaneSketchServiceImpl.getPlaneSketch(orderId));
        return orderPlaneSketchServiceImpl.getPlaneSketch(orderId);
    }

    @PostMapping("/submitToDelayTimeFlow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitToDelayTimeFlow(@RequestBody OrderPlaneSketch orderPlaneSketch) {
        log.debug("orderPlaneSketch"+orderPlaneSketch.getOrderId());
        orderPlaneSketchServiceImpl.submitToDelayTimeFlow(orderPlaneSketch.getOrderId());
    }

    @PostMapping("/submitToBackToDesignerFlow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitToBackToDesignerFlow(@RequestBody OrderPlaneSketch orderPlaneSketch) {
        orderPlaneSketchServiceImpl.submitToBackToDesignerFlow(orderPlaneSketch.getOrderId());
    }
}
