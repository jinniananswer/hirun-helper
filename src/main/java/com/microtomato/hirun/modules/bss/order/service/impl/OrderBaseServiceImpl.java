package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.config.entity.po.OrderStatusCfg;
import com.microtomato.hirun.modules.bss.config.service.IOrderStatusCfgService;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderBase;
import com.microtomato.hirun.modules.bss.order.mapper.OrderBaseMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderBaseService;
import com.microtomato.hirun.modules.system.entity.dto.PendingTaskDTO;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单主表 服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2020-02-03
 */
@Slf4j
@Service
public class OrderBaseServiceImpl extends ServiceImpl<OrderBaseMapper, OrderBase> implements IOrderBaseService {

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private IOrderStatusCfgService orderStatusCfgService;

    @Autowired
    private OrderBaseMapper orderBaseMapper;

    @Override
    public OrderBase queryByOrderId(Long orderId) {
        OrderBase order = this.getById(orderId);
        return order;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void updateOrderBase(OrderBase orderBase) {
        this.baseMapper.updateById(orderBase);
    }


    /**
     * 查询订单状态待办任务
     * @param orderStatuses
     * @param type
     * @return
     */
    @Override
    public List<PendingTaskDTO> queryOrderStatusPendingTasks(String orderStatuses, String type) {
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        List<PendingTaskDTO> pendingTasks = this.orderBaseMapper.queryOrderStatusPendingTasks(orderStatuses, employeeId, type);

        String[] statusArray = orderStatuses.split(",");
        if (pendingTasks == null) {
            pendingTasks = new ArrayList<>();
        }

        List<PendingTaskDTO> result = new ArrayList<>();
        if (statusArray.length != pendingTasks.size()) {
            //首页最多显示四个待办任务
            for (String status : statusArray) {
                PendingTaskDTO temp = null;
                for (PendingTaskDTO pendingTask : pendingTasks) {
                    if (StringUtils.equals(pendingTask.getName(), status)) {
                        temp = pendingTask;
                        break;
                    }
                }

                if (temp == null) {
                    temp = new PendingTaskDTO();
                    temp.setName(status);
                    temp.setNum("0");
                }
                result.add(temp);

            }
        } else {
            result.addAll(pendingTasks);
        }

        result.forEach(task -> {
            String status = task.getName();
            OrderStatusCfg statusConfig = this.orderStatusCfgService.getCfgByTypeStatus(type, status);
            task.setName(statusConfig.getStatusName());
            task.setLink("openUrl?url=modules/bss/order/employee_order_console&status="+status);
        });
        return result;
    }
}
