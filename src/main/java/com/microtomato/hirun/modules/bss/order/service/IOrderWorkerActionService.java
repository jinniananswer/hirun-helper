package com.microtomato.hirun.modules.bss.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerActionDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderWorkerAction;

import java.util.List;

/**
 * (OrderWorkerAction)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-06-07 15:16:56
 */
public interface IOrderWorkerActionService extends IService<OrderWorkerAction> {

    List<OrderWorkerActionDTO> queryByOrderId(Long orderId);

    List<OrderWorkerActionDTO> queryByOrderIdActionDto(Long orderId, String action);

    List<OrderWorkerAction> queryByOrderIdAction(Long orderId, String action);

    List<OrderWorkerAction> queryByOrderIdEmployeeIdAction(Long orderId, Long employeeId,String action) ;

    List<OrderWorkerAction> hasOtherAction(List<Long> workerIds, String action);

    void createOrderWorkerAction(Long orderId, Long employeeId, Long workerId, String currentOrderStatus, String action);

    List<Long> deleteOrderWorkerAction(Long orderId,String action);

}