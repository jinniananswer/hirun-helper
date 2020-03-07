package com.microtomato.hirun.modules.bss.order.service;

import com.microtomato.hirun.modules.bss.order.entity.po.OrderSettlement;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuhui
 * @since 2020-03-07
 */
public interface IOrderSettlementService extends IService<OrderSettlement> {
    void saveOrderSettlement(OrderSettlement orderSettlement);

    OrderSettlement queryOrderSettlement(Long orderId);

    void submitCollectLastFee(OrderSettlement orderSettlement);
}
