package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.exception.ErrorKind;
import com.microtomato.hirun.framework.exception.cases.NotFoundException;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerDetailDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderWorker;
import com.microtomato.hirun.modules.bss.order.mapper.OrderWorkerMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderWorkerService;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.service.IEmployeeJobRoleService;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IStaticDataService staticDataService;

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

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public Long updateOrderWorker(Long orderId, Long roleId, Long employeeId) {
        if (orderId == null || roleId == null || employeeId == null) {
            throw new NotFoundException("参数缺失", ErrorKind.NOT_FOUND.getCode());
        }
        OrderWorker orderWorker = this.orderWorkerMapper.selectOne(new QueryWrapper<OrderWorker>().lambda()
                .eq(OrderWorker::getOrderId, orderId).eq(OrderWorker::getRoleId, roleId)
                .gt(OrderWorker::getEndDate, RequestTimeHolder.getRequestTime()));

        if (orderWorker != null) {
            //如果Employee一样，则不更新
            if (orderWorker.getEmployeeId().equals(employeeId)) {
                return null;
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
        this.orderWorkerMapper.insert(newOrderWorker);
        return newOrderWorker.getId();

    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public Long updateOrderWorkerByEmployeeId(Long orderId, Long roleId, Long employeeId) {
        if (orderId == null || roleId == null || employeeId == null) {
            throw new NotFoundException("参数缺失", ErrorKind.NOT_FOUND.getCode());
        }
        OrderWorker orderWorker = this.orderWorkerMapper.selectOne(new QueryWrapper<OrderWorker>().lambda()
                .eq(OrderWorker::getOrderId, orderId).eq(OrderWorker::getRoleId, roleId).eq(OrderWorker::getEmployeeId,employeeId)
                .gt(OrderWorker::getEndDate, RequestTimeHolder.getRequestTime()));

        if (orderWorker != null) {
            //如果Employee一样，则不更新
            if (orderWorker.getEmployeeId().equals(employeeId)) {
                return null;
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
        this.orderWorkerMapper.insert(newOrderWorker);
        return newOrderWorker.getId();
    }

    /**
     * 根据订单ID列表及角色列表查询订单参与人相关信息
     * @param orderIds
     * @param roleIds
     * @return
     */
    @Override
    public List<OrderWorkerDTO> queryByOrderIdsRoleIds(List<Long> orderIds, List<Long> roleIds) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        List<OrderWorker> orderWorkers = this.list(Wrappers.<OrderWorker>lambdaQuery()
                .in(OrderWorker::getOrderId, orderIds)
                .in(OrderWorker::getRoleId, roleIds)
                .ge(OrderWorker::getEndDate, now));

        if (ArrayUtils.isEmpty(orderWorkers)) {
            return null;
        }

        List<OrderWorkerDTO> result = new ArrayList<>();
        orderWorkers.forEach(orderWorker -> {
            Long employeeId = orderWorker.getEmployeeId();
            Employee employee = this.employeeService.getById(employeeId);
            OrderWorkerDTO worker = new OrderWorkerDTO();
            BeanUtils.copyProperties(orderWorker, worker);
            worker.setName(employee.getName());
            result.add(worker);
        });
        return result;
    }

    /**
     * 订单详情获取时展示工作人员信息
     * @param orderId
     * @return
     */
    @Override
    public List<OrderWorkerDetailDTO> queryOrderWorkerDetails(Long orderId) {
        List<OrderWorkerDetailDTO> orderWorkers = this.orderWorkerMapper.queryAllByOrderId(orderId);
        if (ArrayUtils.isEmpty(orderWorkers)) {
            return orderWorkers;
        }
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        orderWorkers.forEach(orderWorker -> {
            orderWorker.setStatusName(this.staticDataService.getCodeName("EMPLOYEE_STATUS", orderWorker.getStatus()));
            LocalDateTime endDate = orderWorker.getEndDate();

            if (TimeUtils.compareTwoTime(endDate, now) < 0) {
                orderWorker.setWorkerStatus("已退出");
            } else {
                orderWorker.setWorkerStatus("正常");
            }
        });

        return orderWorkers;
    }
}
