package com.microtomato.hirun.modules.bss.order.controller;

/**
 * @author ：mmzs
 * @date ：Created in 2020/2/6 22:49
 * @description：订单全房图流程控制
 * @modified By：
 * @version: 1$
 */

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPlaneSketch;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderWholeRoomDraw;
import com.microtomato.hirun.modules.bss.order.service.IOrderWholeRoomDrawService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@Slf4j
@RequestMapping("/api/bss.order/order-wholeRoomDrawing")
public class OrderWholeRoomDrawController {
    @Autowired
    private IOrderWholeRoomDrawService iOrderWholeRoomDrawService;

    @PostMapping("/submitWholeRoomDrawing")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submit(OrderWholeRoomDraw orderWholeRoomDraw) {
        iOrderWholeRoomDrawService.save(orderWholeRoomDraw);
    }

    @PostMapping("/getWholeRoomDraw")
    @RestResult
    public OrderWholeRoomDraw getWholeRoomDraw(String id) {
        return iOrderWholeRoomDrawService.getById(id);
    }

    @PostMapping("/submitToAuditPicturesFlow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitToDelayTimeFlow(OrderWholeRoomDraw orderWholeRoomDraw) {
        iOrderWholeRoomDrawService.submitToAuditPicturesFlow(orderWholeRoomDraw.getOrderId());
    }

    @PostMapping("/submitToConfirmFlow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitToConfirmFlow(OrderWholeRoomDraw orderWholeRoomDraw) {
        iOrderWholeRoomDrawService.submitToConfirmFlow(orderWholeRoomDraw.getOrderId());
    }

    @PostMapping("/submitCancelDesignExpensesFlow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitCancelDesignExpensesFlow(OrderWholeRoomDraw orderWholeRoomDraw) {
        iOrderWholeRoomDrawService.submitCancelDesignExpensesFlow(orderWholeRoomDraw.getOrderId());
    }

    @PostMapping("/submitToWholeRoomDelayTimeFlow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitToWholeRoomDelayTimeFlow(OrderWholeRoomDraw orderWholeRoomDraw) {
        iOrderWholeRoomDrawService.submitToWholeRoomDelayTimeFlow(orderWholeRoomDraw.getOrderId());
    }

    @PostMapping("/submitToCustomerLeaderFlow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitToCustomerLeaderFlow(OrderWholeRoomDraw orderWholeRoomDraw) {
        iOrderWholeRoomDrawService.submitToCustomerLeaderFlow(orderWholeRoomDraw.getOrderId());
    }

    @PostMapping("/submitToBackWholeRoomFlow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitToBackWholeRoomFlow(OrderWholeRoomDraw orderWholeRoomDraw) {
        iOrderWholeRoomDrawService.submitToBackWholeRoomFlow(orderWholeRoomDraw.getOrderId());
    }

    @PostMapping("/submitToTwoLevelActuarialCalculationFlow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitToTwoLevelActuarialCalculationFlow(OrderWholeRoomDraw orderWholeRoomDraw) {
        iOrderWholeRoomDrawService.submitToTwoLevelActuarialCalculationFlow(orderWholeRoomDraw.getOrderId());
    }

    @PostMapping("/submitToBackToDesignerFlow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitToBackToDesignerFlow(OrderWholeRoomDraw orderWholeRoomDraw) {
        iOrderWholeRoomDrawService.submitToBackToDesignerFlow(orderWholeRoomDraw.getOrderId());
    }

}
