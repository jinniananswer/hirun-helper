package com.microtomato.hirun.modules.bss.config.service.impl;

import com.microtomato.hirun.modules.bss.config.entity.po.OrderStatusTransCfg;
import com.microtomato.hirun.modules.bss.config.mapper.OrderStatusTransCfgMapper;
import com.microtomato.hirun.modules.bss.config.service.IOrderStatusTransCfgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 订单状态转换配置表 服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2020-02-07
 */
@Slf4j
@Service
public class OrderStatusTransCfgServiceImpl extends ServiceImpl<OrderStatusTransCfgMapper, OrderStatusTransCfg> implements IOrderStatusTransCfgService {

}
