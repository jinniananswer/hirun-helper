package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.order.entity.consts.OrderConst;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFile;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPlaneSketch;
import com.microtomato.hirun.modules.bss.order.mapper.OrderPlaneSketchMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderDomainService;
import com.microtomato.hirun.modules.bss.order.service.IOrderPlaneSketchService;
import com.microtomato.hirun.modules.bss.order.service.IOrderWorkerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ：mmzs
 * @date ：Created in 2020/2/6 20:11
 * @description：订单平面图服务
 * @modified By：
 * @version: 1$
 */
@Slf4j
@Service
public class OrderPlaneSketchServiceImpl extends ServiceImpl<OrderPlaneSketchMapper, OrderPlaneSketch> implements IOrderPlaneSketchService {

    @Autowired
    private IOrderDomainService orderDomainService;

    @Autowired
    private IOrderWorkerService workerService;

    @Override
    public void submitToSignContractFlow(Long orderId) {
        orderDomainService.orderStatusTrans(orderId, OrderConst.OPER_NEXT_STEP);
    }

    @Override
    public void updateOrderWork(Long orderId,Long roleId,Long employeeId) {
        workerService.updateOrderWorker(orderId,roleId,employeeId);
    }

    @Override
    public void submitToBackToDesignerFlow(Long orderId) {
        orderDomainService.orderStatusTrans(orderId, OrderConst.OPER_NEXT_STEP);
    }

    @Override
    public OrderPlaneSketch getPlaneSketch(Long orderId) {
        OrderPlaneSketch orderPlaneSketch = this.getOne(
                Wrappers.<OrderPlaneSketch>lambdaQuery()
                        .eq(OrderPlaneSketch::getOrderId, orderId)
                        .orderByDesc(OrderPlaneSketch::getCreateTime)
        );
        return orderPlaneSketch;
    }

    @Override
    public void submitToConfirmFlow(Long orderId) {
        orderDomainService.orderStatusTrans(orderId, OrderConst.OPER_CONFIRM);
    }

    @Override
    public void submitToDelayTimeFlow(Long orderId) {
        orderDomainService.orderStatusTrans(orderId, OrderConst.OPER_DELAY);
    }

}
