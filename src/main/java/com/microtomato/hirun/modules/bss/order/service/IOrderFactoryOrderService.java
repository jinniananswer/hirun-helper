package com.microtomato.hirun.modules.bss.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.order.entity.dto.factory.FactoryOrderDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.factory.FactoryOrderFollowDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.factory.FactoryOrderInfoDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.factory.QueryFactoryOrderDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFactoryOrder;

import java.util.List;

/**
 * (OrderFactoryOrder)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-09-26 16:32:41
 */
public interface IOrderFactoryOrderService extends IService<OrderFactoryOrder> {

    IPage<FactoryOrderInfoDTO> queryFactoryOrders(QueryFactoryOrderDTO condition);

    FactoryOrderDTO getFactoryOrderDTO(Long orderId);

    OrderFactoryOrder getFactoryOrder(Long orderId);

    void saveFactoryOrder(FactoryOrderDTO data);

    void closeFactoryOrder(FactoryOrderDTO data);

    void saveFollows(List<FactoryOrderFollowDTO> follows);
}