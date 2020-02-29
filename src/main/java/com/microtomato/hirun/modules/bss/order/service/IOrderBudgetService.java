package com.microtomato.hirun.modules.bss.order.service;

import com.microtomato.hirun.modules.bss.order.entity.dto.OrderBudgetCheckedDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderBudgetDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderBudget;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单预算表 服务类
 * </p>
 *
 * @author anwenxuan
 * @since 2020-01-29
 */
public interface IOrderBudgetService extends IService<OrderBudget> {

    public void submitBudget(OrderBudgetDTO dto);

    public OrderBudget getBudgetByOrderId(Long orderId);

    public void submitBudgetCheckedResult(OrderBudgetCheckedDTO dto);

}
