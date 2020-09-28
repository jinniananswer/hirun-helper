package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.bss.order.entity.consts.DesignerConst;
import com.microtomato.hirun.modules.bss.order.entity.consts.OrderConst;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderMeasureHouseDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderBase;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderMeasureHouse;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderWorker;
import com.microtomato.hirun.modules.bss.order.mapper.OrderMeasureHouseMapper;
import com.microtomato.hirun.modules.bss.order.mapper.OrderWorkerMapper;
import com.microtomato.hirun.modules.bss.order.service.*;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ：xiaocl
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

    @Autowired
    private IOrderWorkerActionService orderWorkerActionService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IOrderWorkerService orderWorkerService;

    @Autowired
    private IOrderBaseService orderBaseService ;

    @Autowired
    private IDesignerCommonService designerCommonService;

    @Autowired
    private OrderWorkerMapper orderWorkerMapper;

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
    public OrderMeasureHouseDTO getMeasureHouse(Long orderId) {
        LocalDateTime now = LocalDateTime.now();

        OrderWorker orderWorker = this.orderWorkerService.getOneOrderWorkerByOrderIdRoleId(orderId, 30L);
        OrderMeasureHouse orderMeasureHouse = this.getOne(
                Wrappers.<OrderMeasureHouse>lambdaQuery()
                        .eq(OrderMeasureHouse::getOrderId, orderId)
                        .ge(OrderMeasureHouse::getEndDate, now)
        );
        OrderMeasureHouseDTO orderMeasureHouseDTO = new OrderMeasureHouseDTO();

        if (orderMeasureHouse != null) {
            BeanUtils.copyProperties(orderMeasureHouse,orderMeasureHouseDTO);
        }
        orderMeasureHouseDTO.setDesigner(orderWorker.getEmployeeId());
        orderMeasureHouseDTO.setOrderId(orderWorker.getOrderId());
        return orderMeasureHouseDTO;
    }

    @Override
    public void submitToOnlyWoodworkFlow(Long orderId) {
        orderDomainService.orderStatusTrans(orderId, OrderConst.OPER_NEXT_STEP);
    }

    @Override
    public void saveMeasureHouseInfos(OrderMeasureHouseDTO dto) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        Long orderId = dto.getOrderId();
        OrderMeasureHouse orderMeasureHouse = this.getOne(
                Wrappers.<OrderMeasureHouse>lambdaQuery()
                        .eq(OrderMeasureHouse::getOrderId, orderId)
                        .ge(OrderMeasureHouse::getEndDate, now)
        );

        if ( orderMeasureHouse != null) {
            orderMeasureHouse.setEndDate(now);
            this.updateById(orderMeasureHouse);
        }
        OrderMeasureHouse orderMeasureHouseNew = new OrderMeasureHouse();
        LocalDateTime forever = TimeUtils.getForeverTime();
        BeanUtils.copyProperties(dto,orderMeasureHouseNew);
        orderMeasureHouseNew.setEndDate(forever);
        orderMeasureHouseNew.setStartDate(now);

        if (orderMeasureHouseNew.getId()==null) {
            this.save(orderMeasureHouseNew);
        } else {
            this.updateById(orderMeasureHouseNew);
        }

        OrderBase orderBase = this.orderBaseService.queryByOrderId(orderId);

        List<Long> workerIds = this.orderWorkerActionService.deleteOrderWorkerAction(orderId, DesignerConst.OPER_MEASURE);
        if (ArrayUtils.isNotEmpty(workerIds)) {
            this.orderWorkerService.deleteOrderWorker(workerIds);

        }

        Long assistantDesignerId = dto.getAssistantDesigner();
        if (assistantDesignerId != null) {
            Long workerId = this.orderWorkerService.updateOrderWorker(orderId, 41L, assistantDesignerId);
            this.orderWorkerActionService.createOrderWorkerAction(orderId, assistantDesignerId, workerId, orderBase.getStatus(), DesignerConst.OPER_MEASURE);
        }

        orderBase.setIndoorArea(dto.getMeasureArea());
        this.orderBaseService.updateById(orderBase);

    }

    public Long getWorkerId(Long orderId, Long roleId, Long employeeId) {
        Long workerId;
        OrderWorker orderWorker = this.orderWorkerMapper.selectOne(new QueryWrapper<OrderWorker>().lambda()
                .eq(OrderWorker::getOrderId, orderId).eq(OrderWorker::getRoleId,roleId).eq(OrderWorker::getEmployeeId,employeeId)
                .gt(OrderWorker::getEndDate, RequestTimeHolder.getRequestTime()));
        workerId = orderWorker.getId();
        return workerId;
    }

}
