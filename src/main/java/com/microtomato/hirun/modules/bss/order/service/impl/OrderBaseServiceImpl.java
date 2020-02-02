package com.microtomato.hirun.modules.bss.order.service.impl;

import com.microtomato.hirun.modules.bss.order.entity.po.OrderBase;
import com.microtomato.hirun.modules.bss.order.mapper.OrderBaseMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderBaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 订单主表 服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2020-02-03
 */
@Slf4j
@Service
public class OrderBaseServiceImpl extends ServiceImpl<OrderBaseMapper, OrderBase> implements IOrderBaseService {

    @Autowired
    private IStaticDataService staticDataService;

    @Override
    public OrderBase queryByOrderId(Long orderId) {
        OrderBase order = this.getById(orderId);
        return order;
    }
}
