package com.microtomato.hirun.modules.bss.order.controller;

/**
 * @author ：mmzs
 * @date ：Created in 2020/2/6 22:49
 * @description：订单全房图流程控制
 * @modified By：
 * @version: 1$
 */

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWholeRoomDrawDTO;
import com.microtomato.hirun.modules.bss.order.service.IOrderWholeRoomDrawService;
import com.microtomato.hirun.modules.bss.order.service.IOrderWorkerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/bss.order/order-wholeRoomDrawing")
public class OrderWholeRoomDrawController {
    @Autowired
    private IOrderWholeRoomDrawService iOrderWholeRoomDrawService;

    @Autowired
    private IOrderWorkerService orderWorkerService;

    @PostMapping("/submitWholeRoomDrawing")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @RestResult
    public void save(@RequestBody OrderWholeRoomDrawDTO dto) {
        iOrderWholeRoomDrawService.submitWholeRoomDrawing(dto);
    }

    @GetMapping("/getWholeRoomDraw")
    @RestResult
    public OrderWholeRoomDrawDTO getWholeRoomDraw(Long orderId) {
        return iOrderWholeRoomDrawService.getOrderWholeRoomDrawByOrderId(orderId);
    }

    @PostMapping("/submitToAuditPicturesFlow")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @RestResult
    public void submitToAuditPicturesFlow(@RequestBody OrderWholeRoomDrawDTO dto) {
         this.save(dto);
         iOrderWholeRoomDrawService.submitToAuditPicturesFlow(dto.getOrderId());
    }

    @PostMapping("/submitToConfirmFlow")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @RestResult
    public void submitToConfirmFlow(@RequestBody OrderWholeRoomDrawDTO orderWholeRoomDraw) {
        iOrderWholeRoomDrawService.submitToConfirmFlow(orderWholeRoomDraw.getOrderId());
    }

    @PostMapping("/submitCancelDesignExpensesFlow")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @RestResult
    public void submitCancelDesignExpensesFlow(@RequestBody OrderWholeRoomDrawDTO orderWholeRoomDraw) {
        iOrderWholeRoomDrawService.submitCancelDesignExpensesFlow(orderWholeRoomDraw.getOrderId());
    }

    @PostMapping("/submitToWholeRoomDelayTimeFlow")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @RestResult
    public void submitToWholeRoomDelayTimeFlow(@RequestBody OrderWholeRoomDrawDTO orderWholeRoomDraw) {
        iOrderWholeRoomDrawService.submitToWholeRoomDelayTimeFlow(orderWholeRoomDraw.getOrderId());
    }

    @PostMapping("/submitToCustomerLeaderFlow")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @RestResult
    public void submitToCustomerLeaderFlow(@RequestBody OrderWholeRoomDrawDTO dto) {
        iOrderWholeRoomDrawService.saveAuditWholeRoom(dto);
        iOrderWholeRoomDrawService.submitToCustomerLeaderFlow(dto.getOrderId());
    }

    @PostMapping("/submitToBackWholeRoomFlow")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @RestResult
    public void submitToBackWholeRoomFlow(@RequestBody OrderWholeRoomDrawDTO orderWholeRoomDraw) {
        iOrderWholeRoomDrawService.submitToBackWholeRoomFlow(orderWholeRoomDraw.getOrderId());
    }

    @PostMapping("/submitToTwoLevelActuarialCalculationFlow")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @RestResult
    public void submitToTwoLevelActuarialCalculationFlow(@RequestBody OrderWholeRoomDrawDTO orderWholeRoomDraw) {
        iOrderWholeRoomDrawService.submitToTwoLevelActuarialCalculationFlow(orderWholeRoomDraw.getOrderId());
        /**
         * 需要提交二级精算角色？
         * */
    }

    @PostMapping("/submitToBackToDesignerFlow")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @RestResult
    public void submitToBackToDesignerFlow(@RequestBody OrderWholeRoomDrawDTO orderWholeRoomDraw) {
        iOrderWholeRoomDrawService.submitToBackToDesignerFlow(orderWholeRoomDraw.getOrderId());
    }

    @PostMapping("/saveAuditOrder")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @RestResult
    public void saveAuditOrder(@RequestBody OrderWholeRoomDrawDTO orderWholeRoomDraw) {
        iOrderWholeRoomDrawService.saveAuditOrder(orderWholeRoomDraw);
    }

}
