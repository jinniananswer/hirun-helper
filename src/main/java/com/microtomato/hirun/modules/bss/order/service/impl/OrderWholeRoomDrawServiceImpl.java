package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.order.entity.consts.OrderConst;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWholeRoomDrawDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderWholeRoomDraw;
import com.microtomato.hirun.modules.bss.order.mapper.OrderWholeRoomDrawMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderDomainService;
import com.microtomato.hirun.modules.bss.order.service.IOrderWholeRoomDrawService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
        log.debug("submitToCustomerLeaderFlow"+orderId);
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
        /*if (orderWholeRoomDraw != null) {
            orderWholeRoomDraw.setCreateTime(null);
            orderWholeRoomDraw.setUpdateTime(null);
        }*/
        OrderWholeRoomDrawDTO orderWholeRoomDrawDTO = new OrderWholeRoomDrawDTO();
        if (orderWholeRoomDraw != null) {
            BeanUtils.copyProperties(orderWholeRoomDraw,orderWholeRoomDrawDTO);
        }
        orderWholeRoomDrawDTO.setDesigner(employeeId);
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
    }
}
