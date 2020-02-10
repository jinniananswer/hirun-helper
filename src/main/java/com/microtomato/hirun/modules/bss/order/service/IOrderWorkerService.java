package com.microtomato.hirun.modules.bss.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderWorker;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jinnian
 * @since 2020-02-03
 */
public interface IOrderWorkerService extends IService<OrderWorker> {

    List<OrderWorkerDTO> queryByOrderId(Long orderId);

    /**
     *更新订单参与人
     * @param orderId
     * @param roleId
     * @param employeeId
     */
    void updateOrderWorker(Long orderId,Long roleId,Long employeeId);
}
