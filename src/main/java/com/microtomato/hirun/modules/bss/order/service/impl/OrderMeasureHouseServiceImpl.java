package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustConsultDTO;
import com.microtomato.hirun.modules.bss.order.entity.consts.OrderConst;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderMeasureHouse;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPlaneSketch;
import com.microtomato.hirun.modules.bss.order.mapper.OrderMeasureHouseMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderDomainService;
import com.microtomato.hirun.modules.bss.order.service.IOrderMeasureHouseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ：mmzs
 * @date ：Created in 2020/2/4 21:09
 * @description：订单量房信息服务
 * @modified By：
 * @version: 1$
 */
@Slf4j
@Service
public class OrderMeasureHouseServiceImpl extends ServiceImpl<OrderMeasureHouseMapper, OrderMeasureHouse> implements IOrderMeasureHouseService {

    @Autowired
    private IOrderDomainService orderDomainService;

    @Override
    public void submitToSneakFlow(Long orderId) {
        orderDomainService.orderStatusTrans(orderId, OrderConst.OPER_RUN);
    }

    @Override
    public void submitToPlanesketchFlow(Long orderId) {
        orderDomainService.orderStatusTrans(orderId, OrderConst.OPER_NEXT_STEP);
    }

    @Override
    public void submitToMeasureSuspendFlow(Long orderId) {
        orderDomainService.orderStatusTrans(orderId, OrderConst.OPER_DELAY);
    }

    @Override
    public OrderMeasureHouse getMeasureHouse(Long orderId) {
        OrderMeasureHouse orderMeasureHouse = this.getOne(
                Wrappers.<OrderMeasureHouse>lambdaQuery()
                        .eq(OrderMeasureHouse::getOrderId, orderId)
                        .orderByDesc(OrderMeasureHouse::getCreateTime)
        );
        if (orderMeasureHouse != null) {
            orderMeasureHouse.setCreateTime(null);
            orderMeasureHouse.setUpdateTime(null);
        }
        return orderMeasureHouse;
    }
}
