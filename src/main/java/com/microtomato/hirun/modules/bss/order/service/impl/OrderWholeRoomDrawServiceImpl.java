package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.order.entity.consts.DesignerConst;
import com.microtomato.hirun.modules.bss.order.entity.consts.OrderConst;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWholeRoomDrawDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerActionDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderWholeRoomDraw;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderWorker;
import com.microtomato.hirun.modules.bss.order.mapper.OrderWholeRoomDrawMapper;
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
 * @date ：Created in 2020/2/6 23:22
 * @description：订单全房图服务
 * @modified By：
 * @version: 1$
 */
@Slf4j
@Service
public class OrderWholeRoomDrawServiceImpl extends ServiceImpl<OrderWholeRoomDrawMapper, OrderWholeRoomDraw> implements IOrderWholeRoomDrawService {

    @Autowired
    private IOrderDomainService orderDomainService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IOrderWorkerService orderWorkerService;

    @Autowired
    private IDesignerCommonService designerCommonService;

    @Autowired
    private IOrderWorkerActionService orderWorkerActionService;

    @Override
    public void submitToAuditPicturesFlow(Long orderId) {
        orderDomainService.orderStatusTrans(orderId, OrderConst.OPER_NEXT_STEP);
    }

    @Override
    public void submitToConfirmFlow(Long orderId) {
        orderDomainService.orderStatusTrans(orderId, OrderConst.OPER_CONFIRM);
    }

    @Override
    public void submitCancelDesignExpensesFlow(Long orderId) {
        orderDomainService.orderStatusTrans(orderId, OrderConst.OPER_RUN);
    }

    @Override
    public void submitToWholeRoomDelayTimeFlow(Long orderId) {
        orderDomainService.orderStatusTrans(orderId, OrderConst.OPER_DELAY);
    }


    @Override
    public void submitToTwoLevelActuarialCalculationFlow(Long orderId) {
        orderDomainService.orderStatusTrans(orderId, OrderConst.OPER_NEXT_STEP);
    }

    @Override
    public void submitToCustomerLeaderFlow(Long orderId) {
        orderDomainService.orderStatusTrans(orderId, OrderConst.OPER_NEXT_STEP);
    }

    @Override
    public void submitToBackWholeRoomFlow(Long orderId) {
        orderDomainService.orderStatusTrans(orderId, OrderConst.OPER_AUDIT_NO);
    }

    @Override
    public void submitToBackToDesignerFlow(Long orderId) {
        orderDomainService.orderStatusTrans(orderId, OrderConst.OPER_NEXT_STEP);
    }

    @Override
    public OrderWholeRoomDrawDTO getOrderWholeRoomDrawByOrderId(Long orderId) {
        UserContext userContext = WebContextUtils.getUserContext();
        Long employeeId = userContext.getEmployeeId();
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        OrderWholeRoomDraw orderWholeRoomDraw = this.getOne(
                Wrappers.<OrderWholeRoomDraw>lambdaQuery()
                        .eq(OrderWholeRoomDraw::getOrderId, orderId)
                        .ge(OrderWholeRoomDraw::getEndDate, now)
        );
        if (orderWholeRoomDraw != null) {
            orderWholeRoomDraw.setCreateTime(null);
            orderWholeRoomDraw.setUpdateTime(null);
        }
        OrderWholeRoomDrawDTO orderWholeRoomDrawDTO = new OrderWholeRoomDrawDTO();
        if (orderWholeRoomDraw != null) {
            BeanUtils.copyProperties(orderWholeRoomDraw,orderWholeRoomDrawDTO);
        }
        orderWholeRoomDrawDTO.setDesigner(employeeId);

        List<OrderWorkerActionDTO> orderWorkerActionDTOS = orderWorkerActionService.queryByOrderIdActionDto(orderId,DesignerConst.OPER_DRAW_CONSTRUCT);
        List<OrderWorkerActionDTO> orderWorkerActionWaterDTOS = orderWorkerActionService.queryByOrderIdActionDto(orderId,DesignerConst.OPER_WATER_ELEC_DESIGN);
        //orderWorkerActionDTOS.addAll(orderWorkerActionWaterDTOS);
        if (ArrayUtils.isNotEmpty(orderWorkerActionDTOS)) {
            orderWorkerActionDTOS.forEach(action -> {
                Long id = action.getEmployeeId();
                action.setEmployeeName(employeeService.getEmployeeNameEmployeeId(id));
            });
        }
        orderWholeRoomDrawDTO.setOrderWorkActions(orderWorkerActionDTOS);

        List<OrderWorker> workers =  orderWorkerService.queryValidByOrderId(orderId);
        if (ArrayUtils.isNotEmpty(workers)) {
            workers.forEach(worker -> {
                Long id = worker.getEmployeeId();
                if ( 34 == worker.getRoleId()) {
                    String name = employeeService.getEmployeeNameEmployeeId(id);
                    orderWholeRoomDrawDTO.setDrawingAuditorName(name);
                    orderWholeRoomDrawDTO.setDrawingAuditor(id);
                }
                if ( 19 == worker.getRoleId()) {
                    String name = employeeService.getEmployeeNameEmployeeId(id);
                    orderWholeRoomDrawDTO.setCustomerLeader(id);
                    orderWholeRoomDrawDTO.setCustomerLeaderName(name);
                }
            });
        }

        if (ArrayUtils.isNotEmpty(orderWorkerActionWaterDTOS)) {
             orderWorkerActionWaterDTOS.forEach(orderWorkerActionWaterDTO -> {
                    orderWholeRoomDrawDTO.setHydropowerDesigner(orderWorkerActionWaterDTO.getEmployeeId());
            });
        }

        return orderWholeRoomDrawDTO;
    }

    @Override
    public void  submitWholeRoomDrawing(OrderWholeRoomDrawDTO dto) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        Long orderId = dto.getOrderId();

        OrderWholeRoomDraw orderWholeRoomDraw = this.getOne(
                Wrappers.<OrderWholeRoomDraw>lambdaQuery()
                        .eq(OrderWholeRoomDraw::getOrderId, orderId)
                        .ge(OrderWholeRoomDraw::getEndDate, now)
        );
        if ( orderWholeRoomDraw != null) {
            orderWholeRoomDraw.setEndDate(now);
            this.updateById(orderWholeRoomDraw);
        }

        OrderWholeRoomDraw orderWholeRoomDrawNew = new OrderWholeRoomDraw();
        LocalDateTime forever = TimeUtils.getForeverTime();
        BeanUtils.copyProperties(dto,orderWholeRoomDrawNew);
        orderWholeRoomDrawNew.setEndDate(forever);
        orderWholeRoomDrawNew.setStartDate(now);

        if (orderWholeRoomDrawNew.getId()==null) {
            this.save(orderWholeRoomDrawNew);
        } else {
            this.updateById(orderWholeRoomDrawNew);
        }

        this.orderWorkerActionService.createOrderWorkerAction(dto.getOrderId(),dto.getHydropowerDesigner(),1L,"",DesignerConst.OPER_WATER_ELEC_DESIGN);
        designerCommonService.dealOrderWorkerAction(DesignerConst.OPER_DRAW_CONSTRUCT,dto);
    }
}
