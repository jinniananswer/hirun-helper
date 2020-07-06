package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFeeItem;
import com.microtomato.hirun.modules.bss.order.mapper.OrderFeeItemMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderFeeItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单费用明细表(OrderFeeItem)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-03-03 21:57:46
 */
@Service
@Slf4j
@DataSource(DataSourceKey.INS)
public class OrderFeeItemServiceImpl extends ServiceImpl<OrderFeeItemMapper, OrderFeeItem> implements IOrderFeeItemService {

    @Autowired
    private OrderFeeItemMapper orderFeeItemMapper;

    /**
     * 根据订单ID查询订单费用项信息
     * @param orderId
     * @return
     */
    @Override
    public List<OrderFeeItem> queryByOrderId(Long orderId) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        return this.list(new QueryWrapper<OrderFeeItem>().lambda().eq(OrderFeeItem::getOrderId, orderId).gt(OrderFeeItem::getEndDate, now));
    }

    /**
     * 根据传入的多个订单ID查询订单费用项信息
     * @param orderIds
     * @return
     */
    @Override
    public List<OrderFeeItem> queryByOrderIds(List<Long> orderIds) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        return this.list(Wrappers.<OrderFeeItem>lambdaQuery().in(OrderFeeItem::getOrderId, orderIds).gt(OrderFeeItem::getEndDate, now));
    }

    /**
     * 根据订单ID，费用类型，期数查询费用项信息
     * @param orderId
     * @param type
     * @param period
     * @return
     */
    @Override
    public List<OrderFeeItem> queryByOrderIdTypePeriod(Long orderId, String type, Integer period) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        return this.list(new QueryWrapper<OrderFeeItem>().lambda().eq(OrderFeeItem::getOrderId, orderId)
            .eq(OrderFeeItem::getType, type)
            .gt(OrderFeeItem::getEndDate, now)
            .eq(period != null, OrderFeeItem::getPeriods, period));
    }

    /**
     * 根据订单ID与费用编码查询费用信息
     * @param orderId
     * @param feeNo
     * @return
     */
    @Override
    public List<OrderFeeItem> queryByOrderIdFeeNo(Long orderId, Long feeNo) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        return this.list(new QueryWrapper<OrderFeeItem>().lambda()
            .eq(OrderFeeItem::getOrderId, orderId)
            .eq(OrderFeeItem::getFeeNo, feeNo)
            .gt(OrderFeeItem::getEndDate, now));
    }
}