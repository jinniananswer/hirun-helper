package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.exception.ErrorKind;
import com.microtomato.hirun.framework.exception.cases.NotFoundException;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderWorker;
import com.microtomato.hirun.modules.bss.order.mapper.OrderWorkerMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderWorkerService;
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

    @Override
    public List<OrderWorkerDTO> queryByOrderId(Long orderId) {
        return this.orderWorkerMapper.queryByOrderId(orderId);
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
        OrderWorker newOrderWork = new OrderWorker();
        newOrderWork.setOrderId(orderId);
        newOrderWork.setRoleId(roleId);
        newOrderWork.setEmployeeId(employeeId);
        newOrderWork.setStartDate(RequestTimeHolder.getRequestTime());
        newOrderWork.setEndDate(TimeUtils.getForeverTime());
        this.orderWorkerMapper.insert(newOrderWork);

    }
}
