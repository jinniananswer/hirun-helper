package com.microtomato.hirun.modules.bss.order.service;

import com.microtomato.hirun.modules.bss.order.entity.po.OrderBase;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.system.entity.dto.PendingTaskDTO;

import java.util.List;

/**
 * <p>
 * 订单主表 服务类
 * </p>
 *
 * @author jinnian
 * @since 2020-02-03
 */
public interface IOrderBaseService extends IService<OrderBase> {

    OrderBase queryByOrderId(Long orderId);

    void updateOrderBase(OrderBase orderBase);

    List<PendingTaskDTO> queryOrderStatusPendingTasks(String orderStatuses, String type);
}
