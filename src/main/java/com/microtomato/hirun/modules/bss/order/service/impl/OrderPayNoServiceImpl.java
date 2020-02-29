package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayNo;
import com.microtomato.hirun.modules.bss.order.mapper.OrderPayNoMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderPayNoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单支付流水表表(OrderPayNo)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-02-29 11:01:48
 */
@Service
@Slf4j
public class OrderPayNoServiceImpl extends ServiceImpl<OrderPayNoMapper, OrderPayNo> implements IOrderPayNoService {

    @Autowired
    private OrderPayNoMapper orderPayNoMapper;

    /**
     * 根据订单ID和付款流水号查询订单支付流水信息
     * @param orderId
     * @param orderPayNo
     * @return
     */
    @Override
    public OrderPayNo getByOrderIdAndPayNo(Long orderId, Long orderPayNo) {
        return this.getOne(new QueryWrapper<OrderPayNo>().lambda().eq(OrderPayNo::getOrderId, orderId).eq(OrderPayNo::getPayNo, orderPayNo));
    }

    /**
     * 根据订单ID查询订单支付信息
     * @param orderId
     * @return
     */
    @Override
    public List<OrderPayNo> queryByOrderId(Long orderId) {
        return this.list(new QueryWrapper<OrderPayNo>().lambda().eq(OrderPayNo::getOrderId, orderId));
    }

}