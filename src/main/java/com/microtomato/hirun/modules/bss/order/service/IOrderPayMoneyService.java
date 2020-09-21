package com.microtomato.hirun.modules.bss.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayMoney;

import java.util.List;

/**
 * 付款类型明细表(OrderPayMoney)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-02-26 15:02:26
 */
public interface IOrderPayMoneyService extends IService<OrderPayMoney> {

    List<OrderPayMoney> queryByOrderIdPayNo(Long orderId, Long payNo);
}