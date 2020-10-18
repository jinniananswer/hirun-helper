package com.microtomato.hirun.modules.bss.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerDetailDTO;
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

    List<OrderWorker> queryValidByOrderId(Long orderId);

    /**
     *更新订单参与人
     * @param orderId
     * @param roleId
     * @param employeeId
     */
    Long updateOrderWorker(Long orderId,Long roleId,Long employeeId);

    Long updateOrderWorkerByEmployeeId(Long orderId,Long roleId,Long employeeId);

    List<OrderWorkerDTO> queryByOrderIdsRoleIds(List<Long> orderIds, List<Long> roleIds);

    List<OrderWorkerDetailDTO> queryOrderWorkerDetails(Long orderId);

    OrderWorker getOneOrderWorkerByOrderIdRoleId(Long orderId, Long roleId);

    void deleteOrderWorker(List<Long> ids);

    List<OrderWorker> queryByOrderIdRoleId(Long orderId, Long roleId);
}
