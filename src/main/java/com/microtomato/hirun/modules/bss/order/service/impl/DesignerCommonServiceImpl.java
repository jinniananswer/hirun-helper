package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.bss.order.entity.consts.DesignerConst;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderMeasureHouseDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderPlaneSketchDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWholeRoomDrawDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerActionDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderWorker;
import com.microtomato.hirun.modules.bss.order.mapper.OrderWorkerMapper;
import com.microtomato.hirun.modules.bss.order.service.IDesignerCommonService;
import com.microtomato.hirun.modules.bss.order.service.IOrderWorkerActionService;
import com.microtomato.hirun.modules.bss.order.service.IOrderWorkerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：xiaocl
 * @date ：Created in 2020/6/17 19:40
 * @description：1
 * @modified By：
 * @version: 1$
 */
@Slf4j
@Service
public class DesignerCommonServiceImpl implements IDesignerCommonService {

    @Autowired
    private IOrderWorkerActionService orderWorkerActionService;

    @Autowired
    private IOrderWorkerService orderWorkerService;

    @Autowired
    private OrderWorkerMapper orderWorkerMapper;

    @Override
    public void dealOrderWorkerAction(String action, Object obj) {
        Long orderId = null;
        Long workerId = null;
        OrderPlaneSketchDTO orderPlaneSketchDTO = new OrderPlaneSketchDTO();
        OrderMeasureHouseDTO orderMeasureHouseDTO = new OrderMeasureHouseDTO();
        OrderWholeRoomDrawDTO orderWholeRoomDrawDTO = new OrderWholeRoomDrawDTO();
        List<OrderWorkerActionDTO> orderWorkerActionDTO = new ArrayList<>();
        if ( obj instanceof OrderPlaneSketchDTO ) {
            orderPlaneSketchDTO = (OrderPlaneSketchDTO) obj;
            orderId = orderPlaneSketchDTO.getOrderId();
            workerId = this.orderWorkerService.updateOrderWorkerByEmployeeId(orderId,30L,orderPlaneSketchDTO.getDesigner());
            if ( workerId==null ) {
                workerId =  getWorkerId(orderId,30L,orderPlaneSketchDTO.getDesigner());
            }

        }

        if ( obj instanceof OrderMeasureHouseDTO ) {
            orderMeasureHouseDTO = (OrderMeasureHouseDTO) obj;
            orderId = orderMeasureHouseDTO.getOrderId();
            workerId = this.orderWorkerService.updateOrderWorkerByEmployeeId(orderId,30L,orderMeasureHouseDTO.getDesigner());
            if ( workerId==null ) {
                workerId =  getWorkerId(orderId,30L,orderMeasureHouseDTO.getDesigner());
            }
            orderWorkerActionDTO = orderMeasureHouseDTO.getOrderWorkActions();
        }

        if ( obj instanceof OrderWholeRoomDrawDTO) {
            orderWholeRoomDrawDTO = (OrderWholeRoomDrawDTO) obj;
            orderId = orderWholeRoomDrawDTO.getOrderId();
            workerId = this.orderWorkerService.updateOrderWorkerByEmployeeId(orderId,30L,orderWholeRoomDrawDTO.getDesigner());
            if ( workerId==null ) {
                workerId =  getWorkerId(orderId,30L,orderWholeRoomDrawDTO.getDesigner());
            }
        }

        /**
         *订单动作
         */
        boolean bDelete = false;
        if (ArrayUtils.isNotEmpty(orderWorkerActionDTO)) {
            for (OrderWorkerActionDTO actionDTO : orderWorkerActionDTO) {
                if (!bDelete) {
                    //这地方把原来的设计师的都给删了
                    this.orderWorkerActionService.deleteOrderWorkerAction(actionDTO.getOrderId(),actionDTO.getAction());

                    if (DesignerConst.OPER_DRAW_PLAN.equals(action)) {
                        this.orderWorkerActionService.createOrderWorkerAction(orderPlaneSketchDTO.getOrderId(),orderPlaneSketchDTO.getDesigner(),workerId,null,DesignerConst.OPER_DRAW_PLAN);
                        bDelete = true;
                    } else if (DesignerConst.OPER_DRAW_CONSTRUCT.equals(action)) {
                        this.orderWorkerActionService.createOrderWorkerAction(orderWholeRoomDrawDTO.getOrderId(),orderWholeRoomDrawDTO.getDesigner(),workerId,null,DesignerConst.OPER_DRAW_CONSTRUCT);
                        bDelete = true;
                    } else if (DesignerConst.OPER_MEASURE.equals(action)) {
                        this.orderWorkerActionService.createOrderWorkerAction(orderMeasureHouseDTO.getOrderId(),orderMeasureHouseDTO.getDesigner(),workerId,null,DesignerConst.OPER_MEASURE);
                        bDelete = true;
                    }

                }
                if ( 30L == actionDTO.getRoleId()) {
                    continue;
                }
                workerId = this.orderWorkerService.updateOrderWorkerByEmployeeId(orderId,actionDTO.getRoleId(),actionDTO.getEmployeeId());
                if ( workerId==null ) {
                    OrderWorker orderWorker = this.orderWorkerMapper.selectOne(new QueryWrapper<OrderWorker>().lambda()
                            .eq(OrderWorker::getOrderId, orderId).eq(OrderWorker::getRoleId,41L).eq(OrderWorker::getEmployeeId,actionDTO.getEmployeeId())
                            .gt(OrderWorker::getEndDate, RequestTimeHolder.getRequestTime()));
                    workerId = orderWorker.getId();
                }
                if (DesignerConst.OPER_DRAW_PLAN.equals(action)) {
                    this.orderWorkerActionService.createOrderWorkerAction(orderPlaneSketchDTO.getOrderId(),actionDTO.getEmployeeId(),workerId,null,DesignerConst.OPER_DRAW_PLAN);
                } else if (DesignerConst.OPER_DRAW_CONSTRUCT.equals(action)) {
                    this.orderWorkerActionService.createOrderWorkerAction(orderWholeRoomDrawDTO.getOrderId(),actionDTO.getEmployeeId(),workerId,null,DesignerConst.OPER_DRAW_CONSTRUCT);
                } else if (DesignerConst.OPER_MEASURE.equals(action)) {
                    this.orderWorkerActionService.createOrderWorkerAction(orderMeasureHouseDTO.getOrderId(),actionDTO.getEmployeeId(),workerId,null,DesignerConst.OPER_MEASURE);
                }
            }
        } else {
            if (DesignerConst.OPER_DRAW_PLAN.equals(action)) {
                this.orderWorkerActionService.deleteOrderWorkerAction(orderId,DesignerConst.OPER_DRAW_PLAN);
                this.orderWorkerActionService.createOrderWorkerAction(orderId,orderPlaneSketchDTO.getDesigner(),workerId,null,DesignerConst.OPER_DRAW_PLAN);
            } else if (DesignerConst.OPER_DRAW_CONSTRUCT.equals(action)) {
                this.orderWorkerActionService.deleteOrderWorkerAction(orderId, DesignerConst.OPER_DRAW_CONSTRUCT);
                this.orderWorkerActionService.createOrderWorkerAction(orderId,orderWholeRoomDrawDTO.getDesigner(),workerId,null,DesignerConst.OPER_DRAW_CONSTRUCT);
            } else if (DesignerConst.OPER_MEASURE.equals(action)) {
                this.orderWorkerActionService.deleteOrderWorkerAction(orderId, DesignerConst.OPER_MEASURE);
                this.orderWorkerActionService.createOrderWorkerAction(orderId,orderMeasureHouseDTO.getDesigner(),workerId,null,DesignerConst.OPER_MEASURE);
            }

        }
    }

    @Override
    public Long getWorkerId(Long orderId, Long roleId, Long employeeId) {
        Long workerId;
        OrderWorker orderWorker = this.orderWorkerMapper.selectOne(new QueryWrapper<OrderWorker>().lambda()
                .eq(OrderWorker::getOrderId, orderId).eq(OrderWorker::getRoleId,30L).eq(OrderWorker::getEmployeeId,employeeId)
                .gt(OrderWorker::getEndDate, RequestTimeHolder.getRequestTime()));
        workerId = orderWorker.getId();
        return workerId;
    }
}
