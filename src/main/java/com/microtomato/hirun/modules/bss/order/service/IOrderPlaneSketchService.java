package com.microtomato.hirun.modules.bss.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPlaneSketch;

/**
 * @author ：mmzs
 * @date ：Created in 2020/2/6 20:10
 * @description：订单平面图服务
 * @modified By：
 * @version: 1$
 */
public interface IOrderPlaneSketchService extends IService<OrderPlaneSketch> {

    void submitToSignContractFlow(Long orderId);

    void submitToConfirmFlow(Long orderId);

    void submitToDelayTimeFlow(Long orderId);

    void updateOrderWork(Long orderId,Long roleId,Long employeeId);

    void submitToBackToDesignerFlow(Long orderId);

    OrderPlaneSketch getPlaneSketch(Long orderId);
}
