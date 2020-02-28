package com.microtomato.hirun.modules.bss.order.service.impl;

import com.microtomato.hirun.modules.bss.order.entity.consts.OrderConst;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderBudgetDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderBudget;
import com.microtomato.hirun.modules.bss.order.mapper.OrderBudgetMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderBudgetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.order.service.IOrderDomainService;
import com.microtomato.hirun.modules.bss.order.service.IOrderWorkerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private IOrderDomainService orderDomainService;

    @Autowired
    private IOrderWorkerService orderWorkerService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void submitBudget(OrderBudgetDTO dto) {
        OrderBudget orderBudget = new OrderBudget();
        BeanUtils.copyProperties(dto, orderBudget);
        this.save(orderBudget);

        orderDomainService.orderStatusTrans(dto.getOrderId(), OrderConst.OPER_NEXT_STEP);

        orderWorkerService.updateOrderWorker(dto.getOrderId(), 15L, dto.getCheckEmployeeId());
    }
}
