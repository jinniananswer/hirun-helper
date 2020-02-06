package com.microtomato.hirun.modules.bss.order.service;

import com.microtomato.hirun.modules.bss.order.entity.po.OrderOperLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jinnian
 * @since 2020-02-07
 */
public interface IOrderOperLogService extends IService<OrderOperLog> {

    void createOrderOperLog(Long orderId, Integer orderState, String orderStatus, String content);
}
