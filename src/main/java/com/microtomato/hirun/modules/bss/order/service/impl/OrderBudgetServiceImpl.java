package com.microtomato.hirun.modules.bss.order.service.impl;

import com.microtomato.hirun.modules.bss.order.entity.po.OrderBudget;
import com.microtomato.hirun.modules.bss.order.mapper.OrderBudgetMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderBudgetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 订单预算表 服务实现类
 * </p>
 *
 * @author anwenxuan
 * @since 2020-01-29
 */
@Slf4j
@Service
public class OrderBudgetServiceImpl extends ServiceImpl<OrderBudgetMapper, OrderBudget> implements IOrderBudgetService {

}
