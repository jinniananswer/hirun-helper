package com.microtomato.hirun.modules.bss.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFeeItem;

import java.util.List;

/**
 * 订单费用明细表(OrderFeeItem)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-03-03 21:57:46
 */
public interface IOrderFeeItemService extends IService<OrderFeeItem> {

    List<OrderFeeItem> queryByOrderId(Long orderId);

    List<OrderFeeItem> queryByOrderIds(List<Long> orderIds);

    List<OrderFeeItem> queryByOrderIdTypePeriod(Long orderId, String type, Integer period);

    List<OrderFeeItem> queryByOrderIdFeeNo(Long orderId, Long feeNo);
}