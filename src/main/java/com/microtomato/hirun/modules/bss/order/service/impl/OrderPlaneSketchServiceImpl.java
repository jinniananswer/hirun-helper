package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.bss.order.entity.consts.DesignerConst;
import com.microtomato.hirun.modules.bss.order.entity.consts.OrderConst;
import com.microtomato.hirun.modules.bss.order.entity.dto.FeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderPlaneSketchDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerActionDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.*;
import com.microtomato.hirun.modules.bss.order.exception.OrderException;
import com.microtomato.hirun.modules.bss.order.mapper.OrderPlaneSketchMapper;
import com.microtomato.hirun.modules.bss.order.mapper.OrderWorkerMapper;
import com.microtomato.hirun.modules.bss.order.service.*;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：xiaocl
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
    private IFeeDomainService ifeeDomainService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IOrderWorkerActionService orderWorkerActionService;

    @Autowired
    private IOrderBaseService orderBaseService;

    @Autowired
    private IOrderWorkerService orderWorkerService;

    @Autowired
    private OrderWorkerMapper orderWorkerMapper;

    @Autowired
    private IOrderFileService orderFileService;

    public void submitToSignContractFlow(Long orderId) {
        orderDomainService.orderStatusTrans(orderId, OrderConst.OPER_NEXT_STEP);
    }

    @Override
    public void submitToBackToDesignerFlow(Long orderId) {
        orderDomainService.orderStatusTrans(orderId, OrderConst.OPER_NEXT_STEP);
    }

    @Override
    public void submitToConfirmFlow(@RequestBody OrderPlaneSketch orderPlaneSketch) {
        this.existsFile(orderPlaneSketch.getOrderId());
        orderDomainService.orderStatusTrans(orderPlaneSketch.getOrderId(), OrderConst.OPER_CONFIRM);
        OrderBase order = this.orderBaseService.queryByOrderId(orderPlaneSketch.getOrderId());
        if (order == null) {
            throw new OrderException(OrderException.OrderExceptionEnum.ORDER_FEE_NOT_FOUND);
        }
        order.setIndoorArea(orderPlaneSketch.getIndoorArea());
        this.orderBaseService.updateById(order);
    }

    @Override
    public OrderPlaneSketchDTO getPlaneSketch(Long orderId) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        OrderPlaneSketch orderPlaneSketch = this.getOne(
                Wrappers.<OrderPlaneSketch>lambdaQuery()
                        .eq(OrderPlaneSketch::getOrderId, orderId)
                        .ge(OrderPlaneSketch::getEndDate, now)
        );

        OrderWorker orderWorker = this.orderWorkerService.getOneOrderWorkerByOrderIdRoleId(orderId, 30L);
        OrderPlaneSketchDTO orderPlaneSketchDTO = new OrderPlaneSketchDTO();

        if (orderPlaneSketch != null) {
            BeanUtils.copyProperties(orderPlaneSketch,orderPlaneSketchDTO);
        }
        orderPlaneSketchDTO.setDesigner(orderWorker.getEmployeeId());
        List<OrderWorkerActionDTO> orderWorkerActionDTOS = orderWorkerActionService.queryByOrderIdActionDto(orderId, DesignerConst.OPER_DRAW_PLAN);

        if (ArrayUtils.isNotEmpty(orderWorkerActionDTOS)) {
            Long id = orderWorkerActionDTOS.get(0).getEmployeeId();
            orderPlaneSketchDTO.setAssistantDesigner(id);
        }

        OrderWorker financer = this.orderWorkerService.getOneOrderWorkerByOrderIdRoleId(orderId, 34L);
        if (financer != null) {
            orderPlaneSketchDTO.setFinanceEmployeeId(financer.getEmployeeId());
        }

        OrderBase orderBase = this.orderBaseService.queryByOrderId(orderId);
        orderPlaneSketchDTO.setIndoorArea(orderBase.getIndoorArea());
        orderPlaneSketchDTO.setOrderId(orderId);
        if (orderPlaneSketchDTO.getPlaneSketchStartDate() == null) {
            LocalDate today = RequestTimeHolder.getRequestTime().toLocalDate();
            LocalDate endDate = today.plusMonths(3);
            orderPlaneSketchDTO.setPlaneSketchStartDate(today);
            orderPlaneSketchDTO.setPlaneSketchEndDate(endDate);
        }
        return orderPlaneSketchDTO;
    }

    /**
     * 根据多个订单ID查询设计费信息
     * @param orderIds
     * @return
     */
    @Override
    public List<OrderPlaneSketch> queryByOrderIds(List<Long> orderIds) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        return this.list(Wrappers.<OrderPlaneSketch>lambdaQuery().in(OrderPlaneSketch::getOrderId, orderIds).ge(OrderPlaneSketch::getEndDate, now));
    }

    /**
     * 根据订单ID查询设计费信息
     * @param orderId
     * @return
     */
    @Override
    public OrderPlaneSketch getByOrderId(Long orderId) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        return this.getOne(Wrappers.<OrderPlaneSketch>lambdaQuery().eq(OrderPlaneSketch::getOrderId, orderId).ge(OrderPlaneSketch::getEndDate, now), false);
    }

    @Override
    public void submitPlaneSketch(OrderPlaneSketchDTO dto) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        Long orderId = dto.getOrderId();

        OrderPlaneSketch orderPlaneSketch = this.getOne(
                Wrappers.<OrderPlaneSketch>lambdaQuery()
                        .eq(OrderPlaneSketch::getOrderId, orderId)
                        .ge(OrderPlaneSketch::getEndDate, now)
        );

        if ( orderPlaneSketch != null) {
            orderPlaneSketch.setEndDate(now);
            this.updateById(orderPlaneSketch);
        }

        OrderPlaneSketch orderPlaneSketchNew = new OrderPlaneSketch();
        LocalDateTime forever = TimeUtils.getForeverTime();
        BeanUtils.copyProperties(dto,orderPlaneSketchNew);
        orderPlaneSketchNew.setEndDate(forever);
        orderPlaneSketchNew.setStartDate(now);

        if (orderPlaneSketchNew.getId()==null) {
            this.save(orderPlaneSketchNew);
        } else {
            this.updateById(orderPlaneSketchNew);
        }
        /**
         *订单动作,设置收银员
         */
        if (dto.getFinanceEmployeeId() != null) {
            orderWorkerService.updateOrderWorker(dto.getOrderId(),34L,dto.getFinanceEmployeeId());
        }

        OrderBase orderBase = this.orderBaseService.queryByOrderId(orderId);
        orderBase.setIndoorArea(orderPlaneSketchNew.getIndoorArea());
        this.orderBaseService.updateById(orderBase);

        List<Long> workerIds = this.orderWorkerActionService.deleteOrderWorkerAction(orderId, DesignerConst.OPER_DRAW_PLAN);
        if (ArrayUtils.isNotEmpty(workerIds)) {
            //检查是否有其它动作
            List<OrderWorkerAction> otherActions = this.orderWorkerActionService.hasOtherAction(workerIds, DesignerConst.OPER_DRAW_PLAN);
            List<Long> onlyDrawPlanWorkerIds = new ArrayList<>();

            for (Long workerId : workerIds) {

                boolean isFind = false;
                for (OrderWorkerAction otherAction : otherActions) {
                    if (otherAction.getWorkerId().equals(workerId)) {
                        isFind = true;
                        break;
                    }
                }

                if (!isFind) {
                    onlyDrawPlanWorkerIds.add(workerId);
                }
            }

            if (ArrayUtils.isNotEmpty(onlyDrawPlanWorkerIds)) {
                this.orderWorkerService.deleteOrderWorker(workerIds);
            }
        }

        Long assistantDesignerId = dto.getAssistantDesigner();
        if (assistantDesignerId != null) {
            Long workerId = this.orderWorkerService.updateOrderWorker(orderId,41L,dto.getAssistantDesigner());
            this.orderWorkerActionService.createOrderWorkerAction(orderId, assistantDesignerId, workerId, orderBase.getStatus(), DesignerConst.OPER_DRAW_PLAN);
        }

    }

    @Override
    public void submitToDelayTimeFlow(Long orderId) {
        orderDomainService.orderStatusTrans(orderId, OrderConst.OPER_DELAY);
    }

    @Override
    public void submitToSignContractFlow(@RequestBody OrderPlaneSketchDTO dto) {
        //this.existsFile(dto.getOrderId());
        /**
         * 回写费用
         * */
        List<FeeDTO> FeeDTOs = new ArrayList();
        FeeDTO fee  = new FeeDTO();
        fee.setFeeItemId(1L);
        fee.setMoney(Double.valueOf(dto.getContractDesignFee()));
        FeeDTOs.add(fee);
        ifeeDomainService.createOrderFee(dto.getOrderId(),"1",null,FeeDTOs);
        /**
         * 状态扭转
         * */
        this.submitToSignContractFlow(dto.getOrderId());
    }

    public void existsFile(Long OrderId) {
        /**
         * 判斷文件是否上傳
         * */
        OrderFile orderFile = orderFileService.getOrderFile(OrderId, 456);
        if (orderFile == null) {
            throw new OrderException(OrderException.OrderExceptionEnum.FILE_PLANESKETCH_NOT_FOUND);
        }
    }
}
