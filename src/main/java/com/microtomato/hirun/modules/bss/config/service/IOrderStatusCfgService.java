package com.microtomato.hirun.modules.bss.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.config.entity.po.OrderStatusCfg;

import java.util.List;

/**
 * <p>
 * 订单阶段及状态配置表 服务类
 * </p>
 *
 * @author jinnian
 * @since 2020-02-07
 */
public interface IOrderStatusCfgService extends IService<OrderStatusCfg> {

    List<OrderStatusCfg> getAll();

    OrderStatusCfg getCfgByStatus(String orderStatus);
}
