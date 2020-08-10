package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerActionDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderWorkerAction;
import com.microtomato.hirun.modules.bss.order.mapper.OrderWorkerActionMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderWorkerActionService;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import com.microtomato.hirun.modules.organization.service.IEmployeeJobRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * (OrderWorkerAction)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-06-07 15:16:56
 */
@Service
@Slf4j
public class OrderWorkerActionServiceImpl extends ServiceImpl<OrderWorkerActionMapper, OrderWorkerAction> implements IOrderWorkerActionService {

    @Autowired
    private OrderWorkerActionMapper orderWorkerActionMapper;

    @Autowired
    private IEmployeeJobRoleService employeeJobRoleService;

    /**
     * 根据订单ID查询订单参与工作人员与工作人员动作数据
     * @param orderId
     * @return
     */
    @Override
    public List<OrderWorkerActionDTO> queryByOrderId(Long orderId) {
        return this.orderWorkerActionMapper.queryByOrderId(orderId);
    }

    /**
     * 根据订单ID与动作查询动作记录
     * @param orderId
     * @param action
     * @return
     */
    @Override
    public List<OrderWorkerAction> queryByOrderIdEmployeeIdAction(Long orderId, Long employeeId,String action) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        return this.list(Wrappers.<OrderWorkerAction>lambdaQuery()
                .eq(OrderWorkerAction::getOrderId, orderId)
                .eq(OrderWorkerAction::getAction, action)
                .eq(OrderWorkerAction::getEmployeeId, employeeId)
                .ge(OrderWorkerAction::getEndDate, now));
    }

    /**
     * 根据订单ID与动作查询动作记录
     * @param orderId
     * @param action
     * @return
     */
    @Override
    public List<OrderWorkerActionDTO> queryByOrderIdActionDto(Long orderId, String action) {
        return this.orderWorkerActionMapper.queryByOrderIdAction(orderId,action);
    }

    @Override
    public List<OrderWorkerAction> queryByOrderIdAction(Long orderId, String action) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        return this.list(Wrappers.<OrderWorkerAction>lambdaQuery()
                .eq(OrderWorkerAction::getOrderId, orderId)
                .eq(OrderWorkerAction::getAction, action)
                .ge(OrderWorkerAction::getEndDate, now));
    }

    /**
     * 先终止数据
     * @param orderId
     * @param action
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void deleteOrderWorkerAction(Long orderId,String action) {
        //先终止原来的数据
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        //List<OrderWorkerAction> oldActions = this.queryByOrderIdEmployeeIdAction(orderId,employeeId,action);
        List<OrderWorkerAction> oldActions = this.queryByOrderIdAction(orderId,action);
        if (ArrayUtils.isNotEmpty(oldActions)) {
            oldActions.forEach(oldAction -> {
                oldAction.setEndDate(now);
            });
            this.updateBatchById(oldActions);
        }
    }

    /**
     * 创建订单参与工作人员动作数据
     * @param orderId
     * @param employeeId
     * @param workerId
     * @param action
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void createOrderWorkerAction(Long orderId, Long employeeId, Long workerId, String currentOrderStatus, String action) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();

        List<OrderWorkerAction> orderWorkerActions = this.queryByOrderIdEmployeeIdAction(orderId, employeeId, action);
        if (ArrayUtils.isNotEmpty(orderWorkerActions)) {
            orderWorkerActions.forEach(orderWorkerAction -> {
                orderWorkerAction.setEndDate(now);
            });

            this.updateBatchById(orderWorkerActions);
        }

        //新增新的记录
        EmployeeJobRole employeeJobRole = this.employeeJobRoleService.queryLast(employeeId);
        OrderWorkerAction workerAction = new OrderWorkerAction();
        workerAction.setOrderId(orderId);
        workerAction.setStartDate(now);
        workerAction.setEndDate(TimeUtils.getForeverTime());
        workerAction.setWorkerId(workerId);
        workerAction.setAction(action);
        workerAction.setJobRole(employeeJobRole.getJobRole());
        workerAction.setEmployeeId(employeeId);
        workerAction.setJobGrade(employeeJobRole.getJobGrade());
        workerAction.setOrgId(employeeJobRole.getOrgId());
        workerAction.setOrderStatus(currentOrderStatus);

        this.save(workerAction);
    }
}