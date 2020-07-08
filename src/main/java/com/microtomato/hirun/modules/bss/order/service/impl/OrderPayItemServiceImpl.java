package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayItem;
import com.microtomato.hirun.modules.bss.order.mapper.OrderPayItemMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderPayItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单支付项明细表 服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2020-02-21
 */
@Slf4j
@Service
@DataSource(DataSourceKey.INS)
public class OrderPayItemServiceImpl extends ServiceImpl<OrderPayItemMapper, OrderPayItem> implements IOrderPayItemService {

    /**
     * 根据订单ID和支付流水查询订单支付项信息
     * @param orderId
     * @param payNo
     * @return
     */
    @Override
    public List<OrderPayItem> queryByOrderIdPayNo(Long orderId, Long payNo) {
         return this.list(new QueryWrapper<OrderPayItem>().lambda()
             .eq(OrderPayItem::getOrderId, orderId)
             .eq(OrderPayItem::getPayNo, payNo)
             .gt(OrderPayItem::getEndDate, RequestTimeHolder.getRequestTime()));
    }

    /**
     * 根据订单ID查询订单支付项信息
     * @param orderId
     * @return
     */
    @Override
    public List<OrderPayItem> queryByOrderId(Long orderId) {
        return this.list(new QueryWrapper<OrderPayItem>().lambda()
                .eq(OrderPayItem::getOrderId, orderId)
                .gt(OrderPayItem::getEndDate, RequestTimeHolder.getRequestTime()));
    }

    /**
     * 根据订单ID列表查询订单支付项信息
     * @param orderIds
     * @return
     */
    @Override
    public List<OrderPayItem> queryByOrderIdsPayItems(List<Long> orderIds, List<Long> payItems) {
        return this.list(new QueryWrapper<OrderPayItem>().lambda()
                .in(OrderPayItem::getOrderId, orderIds)
                .in(OrderPayItem::getPayItemId, payItems)
                .gt(OrderPayItem::getEndDate, RequestTimeHolder.getRequestTime()));
    }

    /**
     * 根据订单ID，分期数，付款项编码查询订单付款项信息
     * @param orderId
     * @param payItemIds
     * @param period
     * @return
     */
    @Override
    public List<OrderPayItem> queryByPayItemIds(Long orderId, List<Long> payItemIds, Integer period) {
        QueryWrapper<OrderPayItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OrderPayItem::getOrderId, orderId)
            .eq(period != null, OrderPayItem::getPeriods, period)
            .in(OrderPayItem::getPayItemId, payItemIds);
        return this.list(queryWrapper);
    }
}
