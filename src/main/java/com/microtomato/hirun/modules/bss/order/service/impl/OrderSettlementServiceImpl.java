package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderSettlement;
import com.microtomato.hirun.modules.bss.order.mapper.OrderSettlementMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderDomainService;
import com.microtomato.hirun.modules.bss.order.service.IOrderSettlementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2020-03-07
 */
@Slf4j
@Service
public class OrderSettlementServiceImpl extends ServiceImpl<OrderSettlementMapper, OrderSettlement> implements IOrderSettlementService {

    @Autowired
    private IOrderDomainService orderDomainService;
    @Override
    public void saveOrderSettlement(OrderSettlement orderSettlement) {
        UserContext userContext = WebContextUtils.getUserContext();
        Long orgId = userContext.getOrgId();
        if (orderSettlement.getId() == null) {
            orderSettlement.setOrgId(orgId);
            this.baseMapper.insert(orderSettlement);
        } else {
            this.baseMapper.updateById(orderSettlement);
        }
    }

    @Override
    public OrderSettlement queryOrderSettlement(Long orderId) {
        return this.baseMapper.selectOne(new QueryWrapper<OrderSettlement>().lambda().eq(OrderSettlement::getOrderId,orderId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void submitCollectLastFee(OrderSettlement orderSettlement) {
        this.saveOrderSettlement(orderSettlement);
        orderDomainService.orderStatusTrans(orderSettlement.getOrderId(),"NEXT");
    }
}
