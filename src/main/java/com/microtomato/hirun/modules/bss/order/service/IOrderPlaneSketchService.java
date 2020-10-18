package com.microtomato.hirun.modules.bss.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderPlaneSketchDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPlaneSketch;

import java.util.List;

/**
 * @author ：mmzs
 * @date ：Created in 2020/2/6 20:10
 * @description：订单平面图服务
 * @modified By：
 * @version: 1$
 */
public interface IOrderPlaneSketchService extends IService<OrderPlaneSketch> {

    void saveSignDesignContract(OrderPlaneSketchDTO dto);

    void submitToDelayTimeFlow(Long orderId);

    //void updateOrderWork(Long orderId,Long roleId,Long employeeId);

    void submitToBackToDesignerFlow(Long orderId);

    OrderPlaneSketchDTO getPlaneSketch(Long orderId);

    List<OrderPlaneSketch> queryByOrderIds(List<Long> orderId);

    OrderPlaneSketch getByOrderId(Long orderId);

    void submitPlaneSketch(OrderPlaneSketchDTO dto);

    void submitToConfirmFlow(OrderPlaneSketch orderPlaneSketch);

    void submitToSignContractFlow(OrderPlaneSketchDTO dto);

    void submitToAuditDesignFee(OrderPlaneSketchDTO dto);
}
