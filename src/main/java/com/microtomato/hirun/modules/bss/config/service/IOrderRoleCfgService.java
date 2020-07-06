package com.microtomato.hirun.modules.bss.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.config.entity.po.OrderRoleCfg;

import java.util.List;

/**
 * <p>
 * 订单所需工作人员角色配置表 服务类
 * </p>
 *
 * @author jinnian
 * @since 2020-02-03
 */
public interface IOrderRoleCfgService extends IService<OrderRoleCfg> {

    List<OrderRoleCfg> queryAllValid();
}
