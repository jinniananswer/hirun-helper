package com.microtomato.hirun.modules.bss.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFactoryOrderFollow;

import java.util.List;

/**
 * (OrderFactoryOrderFollow)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-09-26 16:32:41
 */
public interface IOrderFactoryOrderFollowService extends IService<OrderFactoryOrderFollow> {

    List<OrderFactoryOrderFollow> queryByFactoryOrderId(Long factoryOrderId);
}