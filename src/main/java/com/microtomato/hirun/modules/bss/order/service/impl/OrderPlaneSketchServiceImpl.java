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
    private IDesignerCommonService designerCommonService;

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

        if (orderPlaneSketch != null) {
            orderPlaneSketch.setCreateTime(null);
            orderPlaneSketch.setUpdateTime(null);
        }
        UserContext userContext = WebContextUtils.getUserContext();
        Long employeeId = userContext.getEmployeeId();
        OrderPlaneSketchDTO orderPlaneSketchDTO = new OrderPlaneSketchDTO();

        if (orderPlaneSketch != null) {
            BeanUtils.copyProperties(orderPlaneSketch,orderPlaneSketchDTO);
        }
        orderPlaneSketchDTO.setDesigner(employeeId);
        List<OrderWorkerActionDTO> orderWorkerActionDTOS = orderWorkerActionService.queryByOrderIdActionDto(orderId,DesignerConst.OPER_DRAW_PLAN);

        if (ArrayUtils.isNotEmpty(orderWorkerActionDTOS)) {
            orderWorkerActionDTOS.forEach(action -> {
                Long id = action.getEmployeeId();
                orderPlaneSketchDTO.setAssistantDesigner(id);
            });
        }
        orderPlaneSketchDTO.setOrderWorkActions(orderWorkerActionDTOS);

        List<OrderWorker> workers =  orderWorkerService.queryValidByOrderId(orderId);
        if (ArrayUtils.isNotEmpty(workers)) {
                workers.forEach(worker -> {
                Long id = worker.getEmployeeId();
                if ( DesignerConst.ROLE_CODE_CUSTOMER_ClERK.equals(worker.getRoleId())) {
                    String name = employeeService.getEmployeeNameEmployeeId(id);
                    orderPlaneSketchDTO.setFinanceEmployeeName(name);
                    orderPlaneSketchDTO.setFinanceEmployeeId(id);
                }
            });
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
        this.updateIndorrArea(orderPlaneSketchNew.getOrderId(),orderPlaneSketchNew.getIndoorArea());
        /**
         *订单动作
         */
        if (dto.getFinanceEmployeeId() != null) {
            orderWorkerService.updateOrderWorker(dto.getOrderId(),34L,dto.getFinanceEmployeeId());
        }

        Long workerId = null;
        workerId = this.orderWorkerService.updateOrderWorkerByEmployeeId(orderId,30L,dto.getDesigner());
        if ( workerId==null ) {
            workerId =  getWorkerId(orderId,30L,dto.getDesigner());
        }

        if (dto.getDesigner().equals(dto.getAssistantDesigner())) {
            this.orderWorkerActionService.deleteOrderWorkerAction(orderId,DesignerConst.OPER_DRAW_PLAN);
            this.orderWorkerActionService.createOrderWorkerAction(orderId,dto.getAssistantDesigner(),workerId,null,DesignerConst.OPER_DRAW_PLAN);
        } else {
            //如果助理设计师为空，那么啥都不弄
            if (dto.getAssistantDesigner() == null) {
                this.orderWorkerActionService.deleteOrderWorkerAction(orderId,DesignerConst.OPER_DRAW_PLAN);
            } else {
                workerId = this.orderWorkerService.updateOrderWorker(orderId,41L,dto.getAssistantDesigner());
                if ( workerId==null ) {
                    workerId =  getWorkerId(orderId,41L,dto.getAssistantDesigner());
                }
                this.orderWorkerActionService.deleteOrderWorkerAction(orderId,DesignerConst.OPER_DRAW_PLAN);
                this.orderWorkerActionService.createOrderWorkerAction(orderId,dto.getAssistantDesigner(),workerId,null,DesignerConst.OPER_DRAW_PLAN);
            }
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

    private void updateIndorrArea(Long orderId,String indoorArea){
        OrderBase order = this.orderBaseService.queryByOrderId(orderId);

        if (order == null) {
            throw new OrderException(OrderException.OrderExceptionEnum.ORDER_FEE_NOT_FOUND);
        }
        order.setIndoorArea(indoorArea);
        this.orderBaseService.updateById(order);
    }

    @Override
    public void submitToDelayTimeFlow(Long orderId) {
        orderDomainService.orderStatusTrans(orderId, OrderConst.OPER_DELAY);
    }

    @Override
    public void submitToSignContractFlow(@RequestBody OrderPlaneSketchDTO dto) {
        this.existsFile(dto.getOrderId());
        /**
         * 回写套内面积
         * */
        OrderBase order = this.orderBaseService.queryByOrderId(dto.getOrderId());
        if (order == null) {
            throw new OrderException(OrderException.OrderExceptionEnum.ORDER_FEE_NOT_FOUND);
        }
        order.setIndoorArea(dto.getIndoorArea());
        this.orderBaseService.updateById(order);
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
