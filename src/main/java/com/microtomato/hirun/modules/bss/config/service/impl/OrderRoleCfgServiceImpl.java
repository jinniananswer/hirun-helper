package com.microtomato.hirun.modules.bss.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.config.entity.po.OrderRoleCfg;
import com.microtomato.hirun.modules.bss.config.mapper.OrderRoleCfgMapper;
import com.microtomato.hirun.modules.bss.config.service.IOrderRoleCfgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单所需工作人员角色配置表 服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2020-02-03
 */
@Slf4j
@Service
public class OrderRoleCfgServiceImpl extends ServiceImpl<OrderRoleCfgMapper, OrderRoleCfg> implements IOrderRoleCfgService {

    @Override
    public List<OrderRoleCfg> queryAllValid() {
        return this.list(new QueryWrapper<OrderRoleCfg>().lambda().eq(OrderRoleCfg::getStatus, "1"));
    }
}
