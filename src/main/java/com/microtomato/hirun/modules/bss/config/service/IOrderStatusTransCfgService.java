package com.microtomato.hirun.modules.bss.config.service;

import com.microtomato.hirun.modules.bss.config.entity.po.OrderStatusTransCfg;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单状态转换配置表 服务类
 * </p>
 *
 * @author jinnian
 * @since 2020-02-09
 */
public interface IOrderStatusTransCfgService extends IService<OrderStatusTransCfg> {

    OrderStatusTransCfg getByStatusIdOper(Long id, String oper);
}
