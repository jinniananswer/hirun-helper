package com.microtomato.hirun.modules.bss.order.service.impl;

import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.bss.order.entity.consts.DesignerConst;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderMeasureHouseDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderPlaneSketchDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWholeRoomDrawDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerActionDTO;
import com.microtomato.hirun.modules.bss.order.service.IDesignerCommonService;
import com.microtomato.hirun.modules.bss.order.service.IOrderWorkerActionService;
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

    @Override
    public void dealOrderWorkerAction(String action, Object obj) {
        Long orderId = null;
        OrderPlaneSketchDTO orderPlaneSketchDTO ;
        OrderMeasureHouseDTO orderMeasureHouseDTO;
        OrderWholeRoomDrawDTO orderWholeRoomDrawDTO;
        List<OrderWorkerActionDTO> orderWorkerActionDTO = new ArrayList<>();
        if ( obj instanceof OrderPlaneSketchDTO ) {
            orderPlaneSketchDTO = (OrderPlaneSketchDTO) obj;
            orderId = orderPlaneSketchDTO.getOrderId();
            orderWorkerActionDTO = orderPlaneSketchDTO.getOrderWorkActions();
        }

        if ( obj instanceof OrderMeasureHouseDTO ) {
            orderMeasureHouseDTO = (OrderMeasureHouseDTO) obj;
            orderId = orderMeasureHouseDTO.getOrderId();
            orderWorkerActionDTO = orderMeasureHouseDTO.getOrderWorkActions();
        }

        if ( obj instanceof OrderWholeRoomDrawDTO) {
            orderWholeRoomDrawDTO = (OrderWholeRoomDrawDTO) obj;
            orderId = orderWholeRoomDrawDTO.getOrderId();
            orderWorkerActionDTO = orderWholeRoomDrawDTO.getOrderWorkActions();
        }

        /**
         *订单动作
         */
        boolean bDelete = false;
        if (ArrayUtils.isNotEmpty(orderWorkerActionDTO)) {
            for (OrderWorkerActionDTO actionDTO : orderWorkerActionDTO) {
                if (!bDelete) {
                    this.orderWorkerActionService.deleteOrderWorkerAction(actionDTO.getOrderId(),actionDTO.getAction());
                    bDelete = true;
                }
                this.orderWorkerActionService.createOrderWorkerAction(actionDTO.getOrderId(),actionDTO.getEmployeeId(),1L,actionDTO.getOrderStatus(),actionDTO.getAction());
            }
        } else {
            if (DesignerConst.OPER_DRAW_PLAN.equals(action)) {
                this.orderWorkerActionService.deleteOrderWorkerAction(orderId,DesignerConst.OPER_DRAW_PLAN);
            } else if (DesignerConst.OPER_DRAW_CONSTRUCT.equals(action)) {
                this.orderWorkerActionService.deleteOrderWorkerAction(orderId, DesignerConst.OPER_DRAW_CONSTRUCT);
            } else if (DesignerConst.OPER_MEASURE.equals(action)) {
                this.orderWorkerActionService.deleteOrderWorkerAction(orderId, DesignerConst.OPER_MEASURE);
            }
        }
    }
}
