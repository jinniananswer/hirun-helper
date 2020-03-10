package com.microtomato.hirun.modules.bss.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderWholeRoomDraw;

/**
 * @author ：mmzs
 * @date ：Created in 2020/2/6 23:21
 * @description：订单全房图接口
 * @modified By：
 * @version: 1$
 */
public interface IOrderWholeRoomDrawService extends IService<OrderWholeRoomDraw> {

    void submitToAuditPicturesFlow(Long orderId);

    void submitToConfirmFlow(Long orderId);

    void submitCancelDesignExpensesFlow(Long orderId);

    void submitToWholeRoomDelayTimeFlow(Long orderId);

    void submitToTwoLevelActuarialCalculationFlow(Long orderId);

    void submitToCustomerLeaderFlow(Long orderId);

    void submitToBackWholeRoomFlow(Long orderId);

    void submitToBackToDesignerFlow(Long orderId);

    OrderWholeRoomDraw getOrderWholeRoomDrawByOrderId(Long orderId);

}
