package com.microtomato.hirun.modules.bss.order.service.impl;

import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderOperLog;
import com.microtomato.hirun.modules.bss.order.mapper.OrderOperLogMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderOperLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2020-02-07
 */
@Slf4j
@Service
public class OrderOperLogServiceImpl extends ServiceImpl<OrderOperLogMapper, OrderOperLog> implements IOrderOperLogService {

    /**
     * 创建订单操作日志
     * @param orderId
     * @param type
     * @param orderStage
     * @param orderStatus
     * @param content
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void createOrderOperLog(Long orderId, String type, Integer orderStage, String orderStatus, String content) {
        OrderOperLog orderOperLog = new OrderOperLog();
        orderOperLog.setOrderId(orderId);
        orderOperLog.setType(type);
        orderOperLog.setEmployeeId(WebContextUtils.getUserContext().getEmployeeId());
        orderOperLog.setOperContent(content);
        orderOperLog.setOrderStage(orderStage);
        orderOperLog.setOrderStatus(orderStatus);

        this.save(orderOperLog);
    }
}
