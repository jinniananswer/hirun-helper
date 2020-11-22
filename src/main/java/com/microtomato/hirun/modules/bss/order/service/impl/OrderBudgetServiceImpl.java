package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.exception.cases.NotFoundException;
import com.microtomato.hirun.modules.bss.order.entity.consts.OrderConst;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderBudgetCheckedDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderBudgetDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderBudget;
import com.microtomato.hirun.modules.bss.order.mapper.OrderBudgetMapper;
import com.microtomato.hirun.modules.bss.order.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    @Autowired
    private IOrderWorkerActionService orderWorkerActionService;

    @Autowired
    private IOrderBaseService orderBaseService;

    @Autowired
    private IOrderFileService orderFileService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void submitBudget(OrderBudgetDTO dto) {
        //校验规则
        if(orderFileService.getOrderFile(dto.getOrderId(), 13) == null) {
            throw new NotFoundException("请先上传预算表", -1);
        }

        OrderBudget orderBudget = null;
        if(dto.getId() == null) {
            orderBudget = new OrderBudget();
        } else {
            orderBudget = this.getById(dto.getId());
        }
        BeanUtils.copyProperties(dto, orderBudget);
        this.saveOrUpdate(orderBudget);

        if (dto.getCheckEmployeeId() != null) {
            orderWorkerService.updateOrderWorker(dto.getOrderId(), 43L, dto.getCheckEmployeeId());
        }

        orderDomainService.orderStatusTrans(dto.getOrderId(), OrderConst.OPER_NEXT_STEP);
    }

    @Override
    public OrderBudget getBudgetByOrderId(Long orderId) {
        return baseMapper.selectOne((new QueryWrapper<OrderBudget>().lambda()
                .eq(OrderBudget::getOrderId, orderId)));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void submitBudgetCheckedResult(OrderBudgetCheckedDTO dto) {
        OrderBudget orderBudget = this.getById(dto.getId());
        BeanUtils.copyProperties(dto, orderBudget);
        baseMapper.updateById(orderBudget);
//
        if("ok".equals(dto.getCheckResult())) {
            orderDomainService.orderStatusTrans(dto.getOrderId(), OrderConst.OPER_NEXT_STEP);
        } else {
            orderDomainService.orderStatusTrans(dto.getOrderId(), OrderConst.OPER_AUDIT_NO);
        }
    }

    @Override
    public void submitWoodContract(OrderBudgetDTO dto) {
        orderDomainService.orderStatusTrans(dto.getOrderId(),"NEXT");
    }
}
