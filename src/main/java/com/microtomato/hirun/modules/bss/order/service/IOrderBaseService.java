package com.microtomato.hirun.modules.bss.order.service;

import com.microtomato.hirun.modules.bss.order.entity.po.OrderBase;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jinnian
 * @since 2020-02-02
 */
public interface IOrderBaseService extends IService<OrderBase> {

    OrderBase queryByOrderId(Long orderId);
}
