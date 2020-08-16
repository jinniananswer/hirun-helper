package com.microtomato.hirun.modules.bss.order.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderPlaneSketchDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPlaneSketch;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderWorker;
import com.microtomato.hirun.modules.bss.order.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private IFeeDomainService ifeeDomainService;

    @Autowired
    private IOrderBaseService orderBaseService;

    @Autowired
    private IOrderWorkerService orderWorkerService;

    @Autowired
    private IOrderPlaneSketchService orderPlaneSketchServiceImpl;

    @PostMapping("/submitPlaneSketch")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void save(@RequestBody OrderPlaneSketchDTO dto) {
        this.orderPlaneSketchServiceImpl.submitPlaneSketch(dto);
        orderWorkerService.updateOrderWorker(dto.getOrderId(),30L,dto.getDesigner());
        if (dto.getFinanceEmployeeId()!=null) {
            orderWorkerService.updateOrderWorker(dto.getOrderId(),34L,dto.getFinanceEmployeeId());
        }
        if (dto.getProjectAssistant()!=null) {
            orderWorkerService.updateOrderWorker(dto.getOrderId(),37L,dto.getProjectAssistant());
        }
    }

    @PostMapping("/submitToConfirmFlow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitToConfirmFlow(@RequestBody OrderPlaneSketch orderPlaneSketch) {
        this.orderPlaneSketchServiceImpl.submitToConfirmFlow(orderPlaneSketch);
    }

    @PostMapping("/submitToSignContractFlow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitToSignContractFlow(@RequestBody OrderPlaneSketchDTO dto) {
        //orderPlaneSketchServiceImpl.submitPlaneSketch(dto);
        //orderWorkerService.updateOrderWorker(dto.getOrderId(),34L,dto.getFinanceEmployeeId());
        //orderWorkerService.updateOrderWorker(dto.getOrderId(),30L,dto.getDesigner());
        orderPlaneSketchServiceImpl.submitToSignContractFlow(dto);
    }

    @GetMapping("/getPlaneSketch")
    @RestResult
    public OrderPlaneSketchDTO getPlaneSketch(Long orderId) {
        return orderPlaneSketchServiceImpl.getPlaneSketch(orderId);

    }

    @PostMapping("/submitToDelayTimeFlow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitToDelayTimeFlow(@RequestBody OrderPlaneSketch orderPlaneSketch) {
        orderPlaneSketchServiceImpl.submitToDelayTimeFlow(orderPlaneSketch.getOrderId());
    }

    @PostMapping("/submitToBackToDesignerFlow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitToBackToDesignerFlow(@RequestBody OrderPlaneSketch orderPlaneSketch) {
        orderPlaneSketchServiceImpl.submitToBackToDesignerFlow(orderPlaneSketch.getOrderId());
    }
}
