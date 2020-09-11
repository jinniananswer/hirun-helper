package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFile;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderWholeRoomDraw;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderWorker;
import com.microtomato.hirun.modules.bss.order.exception.OrderException;
import com.microtomato.hirun.modules.bss.order.mapper.OrderWholeRoomDrawMapper;
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

    @Autowired
    private OrderWorkerMapper orderWorkerMapper;

    @Autowired
    private IOrderFileService orderFileService;

    @Override
    public void submitToAuditPicturesFlow(Long orderId) {
        this.existsFile(orderId);
        orderDomainService.orderStatusTrans(orderId, OrderConst.OPER_NEXT_STEP);
    }

    @Override
    public void submitToConfirmFlow(Long orderId) {
        this.existsFile(orderId);
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
                if ( DesignerConst.ROLE_CODE_DRAWING_REVIEWER.equals(worker.getRoleId())) {
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

    public void reckon(OrderWholeRoomDrawDTO dto,Long workerId) {
        Long orderId = dto.getOrderId();
        int iCount = 0 ;
        List<OrderWorkerActionDTO> orderWorkerActionDTOS = orderWorkerActionService.queryByOrderId(orderId);
        if (ArrayUtils.isNotEmpty(orderWorkerActionDTOS)) {
            for (OrderWorkerActionDTO orderWorkerActionDTO : orderWorkerActionDTOS) {
                if (DesignerConst.OPER_DRAW_CONSTRUCT.equals(orderWorkerActionDTO.getAction())) {
                    iCount ++ ;
                }
                if (DesignerConst.OPER_DRAW_PLAN.equals(orderWorkerActionDTO.getAction())) {
                    iCount ++ ;
                }
                if (DesignerConst.OPER_MEASURE.equals(orderWorkerActionDTO.getAction())) {
                    iCount ++ ;
                }
            }
        }
        /**
         * 如果同一个人做完量房，平面图，全房图，那么
         * */
        if (iCount == 3) {
            this.orderWorkerActionService.deleteOrderWorkerAction(orderId,DesignerConst.OPER_DRAW_CONSTRUCT);
            this.orderWorkerActionService.deleteOrderWorkerAction(orderId,DesignerConst.OPER_DRAW_PLAN);
            this.orderWorkerActionService.deleteOrderWorkerAction(orderId,DesignerConst.OPER_MEASURE);
            this.orderWorkerActionService.createOrderWorkerAction(orderId,dto.getAssistantDesigner(),workerId,null,DesignerConst.OPER_WALL_IN_DESIGN);
        }
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
        if (orderWholeRoomDrawNew.getHydropowerDesigner() != null) {
            Long workerId = this.orderWorkerService.updateOrderWorkerByEmployeeId(orderId,38L,orderWholeRoomDrawNew.getHydropowerDesigner());
            if ( workerId==null ) {
                workerId =  getWorkerId(orderId,38L,dto.getHydropowerDesigner());
            }
            this.orderWorkerActionService.createOrderWorkerAction(dto.getOrderId(),dto.getHydropowerDesigner(),workerId,"",DesignerConst.OPER_WATER_ELEC_DESIGN);
        }

        if (dto.getDrawingAuditor() != null) {
            orderWorkerService.updateOrderWorker(dto.getOrderId(),44L,dto.getDrawingAuditor());
        }
        if (dto.getProductionLeader() != null) {
            orderWorkerService.updateOrderWorker(dto.getOrderId(),42L,dto.getProductionLeader());
        }
        if (dto.getDrawingAssistant() != null) {
            orderWorkerService.updateOrderWorker(dto.getOrderId(),39L,dto.getDrawingAssistant());
        }
        if (dto.getAdminAssistant() != null) {
            orderWorkerService.updateOrderWorker(dto.getOrderId(),40L,dto.getAdminAssistant());
        }
        if (dto.getCustomerLeader() != null) {
            orderWorkerService.updateOrderWorker(dto.getOrderId(),58L,dto.getCustomerLeader());
        }
        Long workerId = null;
        workerId = this.orderWorkerService.updateOrderWorkerByEmployeeId(orderId,30L,dto.getDesigner());
        if ( workerId==null ) {
            workerId =  getWorkerId(orderId,30L,dto.getDesigner());
        }
        if (dto.getDesigner().equals(dto.getAssistantDesigner()) && dto.getCustomerLeader() == null && !"orderaudit".equals(dto.getPageTag())) {
            this.orderWorkerActionService.deleteOrderWorkerAction(orderId,DesignerConst.OPER_DRAW_CONSTRUCT);
            this.orderWorkerActionService.createOrderWorkerAction(orderId,dto.getAssistantDesigner(),workerId,null,DesignerConst.OPER_DRAW_CONSTRUCT);
        } else if (!dto.getDesigner().equals(dto.getAssistantDesigner())){
            //如果助理设计师为空，那么啥都不弄
            if (dto.getAssistantDesigner() == null) {
                this.orderWorkerActionService.deleteOrderWorkerAction(orderId,DesignerConst.OPER_DRAW_CONSTRUCT);
            } else if (dto.getAssistantDesigner() != null && dto.getCustomerLeader() == null && !"orderaudit".equals(dto.getPageTag())) {
                workerId = this.orderWorkerService.updateOrderWorker(orderId,DesignerConst.ROLE_CODE_ASSISTANT_DESIGNER,dto.getAssistantDesigner());
                if ( workerId==null ) {
                    workerId =  getWorkerId(orderId,DesignerConst.ROLE_CODE_ASSISTANT_DESIGNER,dto.getAssistantDesigner());
                }
                this.orderWorkerActionService.deleteOrderWorkerAction(orderId,DesignerConst.OPER_DRAW_CONSTRUCT);
                this.orderWorkerActionService.createOrderWorkerAction(orderId,dto.getAssistantDesigner(),workerId,null,DesignerConst.OPER_DRAW_CONSTRUCT);
            }
        }

        if (dto.getCustomerLeader() != null) {
            reckon(dto,workerId);
        }
    }

    public Long getWorkerId(Long orderId, Long roleId, Long employeeId) {
        Long workerId;
        OrderWorker orderWorker = this.orderWorkerMapper.selectOne(new QueryWrapper<OrderWorker>().lambda()
                .eq(OrderWorker::getOrderId, orderId).eq(OrderWorker::getRoleId,roleId).eq(OrderWorker::getEmployeeId,employeeId)
                .gt(OrderWorker::getEndDate, RequestTimeHolder.getRequestTime()));
        workerId = orderWorker.getId();
        return workerId;
    }

    public void existsFile(Long OrderId) {
        /**
         * 判斷文件是否上傳
         * */
        OrderFile orderFile = orderFileService.getOrderFile(OrderId, 567);
        if (orderFile == null) {
            throw new OrderException(OrderException.OrderExceptionEnum.FILE_WHOLEROOM_DRAWING_NOT_FOUND);
        }
    }
}
