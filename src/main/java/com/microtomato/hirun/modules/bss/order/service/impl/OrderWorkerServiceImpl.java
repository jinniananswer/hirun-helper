package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.exception.ErrorKind;
import com.microtomato.hirun.framework.exception.cases.NotFoundException;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderWorker;
import com.microtomato.hirun.modules.bss.order.mapper.OrderWorkerMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderWorkerService;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import com.microtomato.hirun.modules.organization.service.IEmployeeJobRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2020-02-03
 */
@Slf4j
@Service
public class OrderWorkerServiceImpl extends ServiceImpl<OrderWorkerMapper, OrderWorker> implements IOrderWorkerService {

    @Autowired
    private OrderWorkerMapper orderWorkerMapper;

    @Autowired
    private IEmployeeJobRoleService employeeJobRoleService;

    @Override
    public List<OrderWorkerDTO> queryByOrderId(Long orderId) {
        return this.orderWorkerMapper.queryByOrderId(orderId);
    }

    @Override
    public List<OrderWorker> queryValidByOrderId(Long orderId) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        List<OrderWorker> orderWorkers = this.list(Wrappers.<OrderWorker>lambdaQuery()
                .eq(OrderWorker::getOrderId, orderId)
                .le(OrderWorker::getStartDate, now)
                .ge(OrderWorker::getEndDate, now));
        return orderWorkers;
    }

    @Override
    public void updateOrderWorker(Long orderId, Long roleId, Long employeeId) {
        if (orderId == null || roleId == null || employeeId == null) {
            throw new NotFoundException("参数缺失", ErrorKind.NOT_FOUND.getCode());
        }
        OrderWorker orderWorker = this.orderWorkerMapper.selectOne(new QueryWrapper<OrderWorker>().lambda()
                .eq(OrderWorker::getOrderId, orderId).eq(OrderWorker::getRoleId, roleId)
                .gt(OrderWorker::getEndDate, RequestTimeHolder.getRequestTime()));

        if (orderWorker != null) {
            //如果Employee一样，则不更新
            if (orderWorker.getEmployeeId().equals(employeeId)) {
                return;
            }
            orderWorker.setEndDate(LocalDateTime.now());
            this.orderWorkerMapper.updateById(orderWorker);
        }
        OrderWorker newOrderWorker = new OrderWorker();
        newOrderWorker.setOrderId(orderId);
        newOrderWorker.setRoleId(roleId);
        newOrderWorker.setEmployeeId(employeeId);
        newOrderWorker.setStartDate(RequestTimeHolder.getRequestTime());
        newOrderWorker.setEndDate(TimeUtils.getForeverTime());

        //补充当前归属部门与当前岗位信息
        EmployeeJobRole jobRole = this.employeeJobRoleService.queryLast(employeeId);
        if (jobRole != null) {
            newOrderWorker.setJobRole(jobRole.getJobRole());
            newOrderWorker.setOrgId(jobRole.getOrgId());
        }

        this.orderWorkerMapper.insert(newOrderWorker);

    }
}
