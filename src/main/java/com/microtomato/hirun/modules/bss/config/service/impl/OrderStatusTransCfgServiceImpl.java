package com.microtomato.hirun.modules.bss.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.config.entity.po.OrderStatusTransCfg;
import com.microtomato.hirun.modules.bss.config.mapper.OrderStatusTransCfgMapper;
import com.microtomato.hirun.modules.bss.config.service.IOrderStatusTransCfgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单状态转换配置表 服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2020-02-09
 */
@Slf4j
@Service
public class OrderStatusTransCfgServiceImpl extends ServiceImpl<OrderStatusTransCfgMapper, OrderStatusTransCfg> implements IOrderStatusTransCfgService {

    /**
     * 根据状态ID与操作码查询状态转换关系
     * @param statusId
     * @param oper
     * @return
     */
    @Override
    public OrderStatusTransCfg getByStatusIdOper(Long statusId, String oper) {
        return this.getOne(new QueryWrapper<OrderStatusTransCfg>().lambda().eq(OrderStatusTransCfg::getOrderStatusId, statusId).eq(OrderStatusTransCfg::getOperCode, oper));
    }
}
