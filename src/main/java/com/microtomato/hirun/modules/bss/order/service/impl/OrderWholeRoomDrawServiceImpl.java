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
        if (ArrayUtils.isEmpty(orderWorkerActionDTOS)) {
            orderWorkerActionDTOS = orderWorkerActionService.queryByOrderIdActionDto(orderId, DesignerConst.OPER_WALL_IN_DESIGN);
        }

        if (ArrayUtils.isNotEmpty(orderWorkerActionDTOS)) {
            List<Long> assistants = new ArrayList<>();
            orderWorkerActionDTOS.forEach(action -> {
                assistants.add(action.getEmployeeId());
            });
            orderWholeRoomDrawDTO.setAssistantDesigner(assistants);
        }

        List<OrderWorkerActionDTO> vrEmployees = orderWorkerActionService.queryByOrderIdActionDto(orderId, "vr_360");
        if (ArrayUtils.isNotEmpty(vrEmployees)) {
            orderWholeRoomDrawDTO.setVrEmployeeId(vrEmployees.get(0).getEmployeeId());
        }

        List<OrderWorkerActionDTO> whiteModels = orderWorkerActionService.queryByOrderIdActionDto(orderId, "su_white_model");
        if (ArrayUtils.isNotEmpty(whiteModels)) {
            orderWholeRoomDrawDTO.setSuEmployeeId(whiteModels.get(0).getEmployeeId());
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
        List<Long> vrWorkerIds = this.orderWorkerActionService.deleteOrderWorkerAction(dto.getOrderId(), "vr_360");
        List<Long> suWorkerIds = this.orderWorkerActionService.deleteOrderWorkerAction(dto.getOrderId(), "su_white_model");

        if (ArrayUtils.isNotEmpty(workerIds)) {
            //检查是否有其它动作
            List<OrderWorkerAction> otherActions = this.orderWorkerActionService.hasOtherAction(workerIds, DesignerConst.OPER_DRAW_CONSTRUCT);
            List<Long> onlyDrawConstruct = new ArrayList<>();

            for (Long workerId : workerIds) {

                boolean isFind = false;
                for (OrderWorkerAction otherAction : otherActions) {
                    if (otherAction.getWorkerId().equals(workerId)) {
                        isFind = true;
                        break;
                    }
                }

                if (!isFind) {
                    onlyDrawConstruct.add(workerId);
                }
            }

            if (ArrayUtils.isNotEmpty(onlyDrawConstruct)) {
                this.orderWorkerService.deleteOrderWorker(onlyDrawConstruct);
            }
        }

        if (ArrayUtils.isNotEmpty(vrWorkerIds)) {
            //检查是否有其它动作
            List<OrderWorkerAction> vrOtherActions = this.orderWorkerActionService.hasOtherAction(vrWorkerIds, "vr_360");
            List<Long> onlyVR360 = new ArrayList<>();

            for (Long workerId : vrWorkerIds) {

                boolean isFind = false;
                if (ArrayUtils.isNotEmpty(vrOtherActions)) {
                    for (OrderWorkerAction otherAction : vrOtherActions) {
                        if (otherAction.getWorkerId().equals(workerId)) {
                            isFind = true;
                            break;
                        }
                    }
                }

                if (!isFind) {
                    onlyVR360.add(workerId);
                }
            }

            if (ArrayUtils.isNotEmpty(onlyVR360)) {
                this.orderWorkerService.deleteOrderWorker(onlyVR360);
            }
        }

        if (ArrayUtils.isNotEmpty(suWorkerIds)) {
            List<OrderWorkerAction> suOtherActions = this.orderWorkerActionService.hasOtherAction(vrWorkerIds, "su_white_model");
            List<Long> onlySU = new ArrayList<>();

            for (Long workerId : suWorkerIds) {

                boolean isFind = false;
                if (ArrayUtils.isNotEmpty(suOtherActions)) {
                    for (OrderWorkerAction otherAction : suOtherActions) {
                        if (otherAction.getWorkerId().equals(workerId)) {
                            isFind = true;
                            break;
                        }
                    }
                }
                if (!isFind) {
                    onlySU.add(workerId);
                }
            }

            if (ArrayUtils.isNotEmpty(onlySU)) {
                this.orderWorkerService.deleteOrderWorker(onlySU);
            }
        }


        List<Long> assistantDesignerIds = dto.getAssistantDesigner();
        if (ArrayUtils.isNotEmpty(assistantDesignerIds)) {
            assistantDesignerIds.forEach(assistantDesignerId -> {
                Long workerId = this.orderWorkerService.updateOrderWorker(orderId,41L, assistantDesignerId);
                this.orderWorkerActionService.createOrderWorkerAction(orderId, assistantDesignerId, workerId, orderBase.getStatus(), DesignerConst.OPER_DRAW_CONSTRUCT);
            });
        }

        if (dto.getVrEmployeeId() != null) {
            Long workerId = this.orderWorkerService.updateOrderWorker(dto.getOrderId(),41L, dto.getVrEmployeeId());
            this.orderWorkerActionService.createOrderWorkerAction(dto.getOrderId(), dto.getVrEmployeeId(), workerId, orderBase.getStatus(), "vr_360");
        }

        if (dto.getSuEmployeeId() != null) {
            Long workerId = this.orderWorkerService.updateOrderWorker(dto.getOrderId(),41L, dto.getSuEmployeeId());
            this.orderWorkerActionService.createOrderWorkerAction(dto.getOrderId(), dto.getSuEmployeeId(), workerId, orderBase.getStatus(), "su_white_model");
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
        List<OrderWorkerActionDTO> measures = this.orderWorkerActionService.queryByOrderIdActionDto(orderId, DesignerConst.OPER_MEASURE);
        List<OrderWorkerActionDTO> planes = this.orderWorkerActionService.queryByOrderIdActionDto(orderId, DesignerConst.OPER_DRAW_PLAN);
        List<OrderWorkerActionDTO> constructs = this.orderWorkerActionService.queryByOrderIdActionDto(orderId, DesignerConst.OPER_DRAW_CONSTRUCT);

        if (ArrayUtils.isEmpty(measures) || ArrayUtils.isEmpty(planes) || ArrayUtils.isEmpty(constructs)) {
            return;
        }

        OrderWorkerActionDTO measure = measures.get(0);
        OrderWorkerActionDTO plane = planes.get(0);
        OrderWorkerActionDTO construct = constructs.get(0);

        if (measure.getEmployeeId().equals(plane.getEmployeeId()) && measure.getEmployeeId().equals(construct.getEmployeeId())) {
            LocalDateTime now = RequestTimeHolder.getRequestTime();
            /**
             * 如果同一个人做完量房，平面图，全房图，那么将三个动作合并为全程参与一个动作
             * */
            List<OrderWorkerAction> workerActions = new ArrayList<>();
            OrderWorkerAction measureAction = new OrderWorkerAction();
            measureAction.setId(measure.getId());
            measureAction.setEndDate(now);

            OrderWorkerAction planeAction = new OrderWorkerAction();
            planeAction.setId(plane.getId());
            planeAction.setEndDate(now);

            OrderWorkerAction constructAction = new OrderWorkerAction();
            constructAction.setId(construct.getId());
            constructAction.setEndDate(now);

            workerActions.add(measureAction);
            workerActions.add(planeAction);
            workerActions.add(constructAction);

            this.orderWorkerActionService.updateBatchById(workerActions);

            OrderBase orderBase = this.orderBaseService.getById(orderId);

            OrderWorker worker = this.orderWorkerService.getOneOrderWorkerByOrderIdEmployeeIdRoleId(measure.getOrderId(), measure.getEmployeeId(), measure.getRoleId());
            this.orderWorkerActionService.createOrderWorkerAction(orderId, worker.getEmployeeId(), worker.getId(), orderBase.getStatus(), DesignerConst.OPER_WALL_IN_DESIGN);
        }


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
