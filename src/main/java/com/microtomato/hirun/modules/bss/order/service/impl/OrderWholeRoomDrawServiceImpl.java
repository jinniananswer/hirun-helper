package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.bss.order.entity.consts.DesignerConst;
import com.microtomato.hirun.modules.bss.order.entity.consts.OrderConst;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWholeRoomDrawDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerActionDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.*;
import com.microtomato.hirun.modules.bss.order.exception.OrderException;
import com.microtomato.hirun.modules.bss.order.mapper.OrderWholeRoomDrawMapper;
import com.microtomato.hirun.modules.bss.order.mapper.OrderWorkerMapper;
import com.microtomato.hirun.modules.bss.order.service.*;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Autowired
    private IOrderBaseService orderBaseService;

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
        this.mergeAssistantWorkerAction(orderId);
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
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        OrderWholeRoomDraw orderWholeRoomDraw = this.getOne(
                Wrappers.<OrderWholeRoomDraw>lambdaQuery()
                        .eq(OrderWholeRoomDraw::getOrderId, orderId)
                        .ge(OrderWholeRoomDraw::getEndDate, now)
        );

        OrderWholeRoomDrawDTO orderWholeRoomDrawDTO = new OrderWholeRoomDrawDTO();
        if (orderWholeRoomDraw != null) {
            BeanUtils.copyProperties(orderWholeRoomDraw,orderWholeRoomDrawDTO);
        }

        orderWholeRoomDrawDTO.setOrderId(orderId);

        List<OrderWorkerActionDTO> orderWorkerActionDTOS = orderWorkerActionService.queryByOrderIdActionDto(orderId, DesignerConst.OPER_DRAW_CONSTRUCT);
        if (ArrayUtils.isNotEmpty(orderWorkerActionDTOS)) {
            orderWholeRoomDrawDTO.setAssistantDesigner(orderWorkerActionDTOS.get(0).getEmployeeId());
        }

        List<OrderWorker> orderWorkers = this.orderWorkerService.queryValidByOrderId(orderId);
        if (ArrayUtils.isNotEmpty(orderWorkers)) {
            for (OrderWorker orderWorker : orderWorkers) {
                if (orderWorker.getRoleId().equals(30L)) {
                    orderWholeRoomDrawDTO.setDesigner(orderWorker.getEmployeeId());
                } else if (orderWorker.getRoleId().equals(44L)) {
                    orderWholeRoomDrawDTO.setDrawingAuditor(orderWorker.getEmployeeId());
                } else if (orderWorker.getRoleId().equals(38L)) {
                    orderWholeRoomDrawDTO.setHydropowerDesigner(orderWorker.getEmployeeId());
                } else if (orderWorker.getRoleId().equals(42L)) {
                    orderWholeRoomDrawDTO.setProductionLeader(orderWorker.getEmployeeId());
                } else if (orderWorker.getRoleId().equals(40L)) {
                    orderWholeRoomDrawDTO.setAdminAssistant(orderWorker.getEmployeeId());
                } else if (orderWorker.getRoleId().equals(58L)) {
                    orderWholeRoomDrawDTO.setCustomerLeader(orderWorker.getEmployeeId());
                }
            }
        }

        if (orderWholeRoomDrawDTO.getDrawStartDate() == null) {
            LocalDate today = RequestTimeHolder.getRequestTime().toLocalDate();
            LocalDate endDate = today.plusMonths(3);
            orderWholeRoomDrawDTO.setDrawStartDate(today);
            orderWholeRoomDrawDTO.setDrawEndDate(endDate);
        }

        return orderWholeRoomDrawDTO;
    }

    @Override
    public void submitWholeRoomDrawing(OrderWholeRoomDrawDTO dto) {
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
        orderWholeRoomDrawNew.setId(null);
        this.save(orderWholeRoomDrawNew);

        if (orderWholeRoomDrawNew.getHydropowerDesigner() != null) {
            this.orderWorkerService.updateOrderWorkerByEmployeeId(orderId,38L,orderWholeRoomDrawNew.getHydropowerDesigner());
        }

        if (dto.getDrawingAuditor() != null) {
            orderWorkerService.updateOrderWorker(dto.getOrderId(),44L,dto.getDrawingAuditor());
        }
        if (dto.getProductionLeader() != null) {
            orderWorkerService.updateOrderWorker(dto.getOrderId(),42L,dto.getProductionLeader());
        }

        if (dto.getAdminAssistant() != null) {
            orderWorkerService.updateOrderWorker(dto.getOrderId(),40L,dto.getAdminAssistant());
        }

        OrderBase orderBase = this.orderBaseService.queryByOrderId(orderId);

        List<Long> workerIds = this.orderWorkerActionService.deleteOrderWorkerAction(orderId, DesignerConst.OPER_DRAW_CONSTRUCT);
        if (ArrayUtils.isNotEmpty(workerIds)) {
            //检查是否有其它动作
            List<OrderWorkerAction> otherActions = this.orderWorkerActionService.hasOtherAction(workerIds, DesignerConst.OPER_DRAW_CONSTRUCT);
            List<Long> onlyDrawContracts = new ArrayList<>();

            for (Long workerId : workerIds) {

                boolean isFind = false;
                for (OrderWorkerAction otherAction : otherActions) {
                    if (otherAction.getWorkerId().equals(workerId)) {
                        isFind = true;
                        break;
                    }
                }

                if (!isFind) {
                    onlyDrawContracts.add(workerId);
                }
            }

            if (ArrayUtils.isNotEmpty(onlyDrawContracts)) {
                this.orderWorkerService.deleteOrderWorker(workerIds);
            }
        }
        Long assistantDesignerId = dto.getAssistantDesigner();
        if (assistantDesignerId != null) {
            Long workerId = this.orderWorkerService.updateOrderWorker(orderId,41L,dto.getAssistantDesigner());
            this.orderWorkerActionService.createOrderWorkerAction(orderId, assistantDesignerId, workerId, orderBase.getStatus(), DesignerConst.OPER_DRAW_CONSTRUCT);
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

    /**
     * 图纸审核通过后看是否助理设计师要合并动作
     * @param orderId
     */
    public void mergeAssistantWorkerAction(Long orderId) {
        //先查助理设计师是不是有多人，如果有多人，则不需要合并
        List<OrderWorker> assistants = this.orderWorkerService.queryByOrderIdRoleId(orderId, 41L);
        if (ArrayUtils.isEmpty(assistants) || assistants.size() > 1) {
            return;
        }

        OrderWorker assistant = assistants.get(0);
        Long id = assistant.getId();

        List<OrderWorkerAction> workerActions = this.orderWorkerActionService.queryByWorkerId(id);

        if (ArrayUtils.isEmpty(workerActions) || workerActions.size() < 3) {
            return;
        }

        /**
         * 如果同一个人做完量房，平面图，全房图，那么将三个动作合并为全程参与一个动作
         * */

        LocalDateTime now = RequestTimeHolder.getRequestTime();
        workerActions.forEach(workerAction -> {
            workerAction.setEndDate(now);
        });

        this.orderWorkerActionService.updateBatchById(workerActions);

        OrderBase orderBase = this.orderBaseService.getById(orderId);
        this.orderWorkerActionService.createOrderWorkerAction(orderId, assistant.getEmployeeId(), assistant.getId(),orderBase.getStatus(), DesignerConst.OPER_WALL_IN_DESIGN);
    }

    /**
     * 保存全房图审图信息
     * @param dto
     */
    @Override
    public void saveAuditWholeRoom(OrderWholeRoomDrawDTO dto) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        Long orderId = dto.getOrderId();
        OrderWholeRoomDraw orderWholeRoomDraw = this.getOne(
                Wrappers.<OrderWholeRoomDraw>lambdaQuery()
                        .eq(OrderWholeRoomDraw::getOrderId, orderId)
                        .ge(OrderWholeRoomDraw::getEndDate, now)
        );
        if ( orderWholeRoomDraw != null) {
            orderWholeRoomDraw.setReviewedComments(dto.getReviewedComments());
            this.updateById(orderWholeRoomDraw);

            if (dto.getCustomerLeader() != null) {
                this.orderWorkerService.updateOrderWorker(orderId, 58L, dto.getCustomerLeader());
            }
        }
    }

    /**
     * 保存全房图审图信息
     * @param dto
     */
    @Override
    public void saveAuditOrder(OrderWholeRoomDrawDTO dto) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        Long orderId = dto.getOrderId();
        OrderWholeRoomDraw orderWholeRoomDraw = this.getOne(
                Wrappers.<OrderWholeRoomDraw>lambdaQuery()
                        .eq(OrderWholeRoomDraw::getOrderId, orderId)
                        .ge(OrderWholeRoomDraw::getEndDate, now)
        );
        if ( orderWholeRoomDraw != null) {
            orderWholeRoomDraw.setOrderComments(dto.getOrderComments());
            this.updateById(orderWholeRoomDraw);
        }
    }

}
