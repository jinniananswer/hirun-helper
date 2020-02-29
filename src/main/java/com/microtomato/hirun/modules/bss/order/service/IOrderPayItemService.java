package com.microtomato.hirun.modules.bss.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayItem;

import java.util.List;

/**
 * <p>
 * 订单支付项明细表 服务类
 * </p>
 *
 * @author jinnian
 * @since 2020-02-21
 */
public interface IOrderPayItemService extends IService<OrderPayItem> {

    List<OrderPayItem> queryByOrderIdPayNo(Long orderId, Long payNo);
}
