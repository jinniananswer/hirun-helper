package com.microtomato.hirun.modules.bss.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderMeasureHouse;

/**
 * @author ：mmzs
 * @date ：Created in 2020/2/4 21:07
 * @description：订单量房信息
 * @modified By：
 * @version: 1$
 */
public interface IOrderMeasureHouseService extends IService<OrderMeasureHouse> {

    void submitToSneakFlow(Long orderId) ;

    void submitToPlanesketchFlow(Long orderId);

    void submitToMeasureSuspendFlow(Long orderId);

    OrderMeasureHouse getMeasureHouse(Long orderId);
}
