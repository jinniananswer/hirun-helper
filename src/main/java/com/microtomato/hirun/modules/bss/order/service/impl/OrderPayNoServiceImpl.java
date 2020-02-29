package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.order.mapper.OrderPayNoMapper;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayNo;
import com.microtomato.hirun.modules.bss.order.service.IOrderPayNoService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 订单支付流水表表(OrderPayNo)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-02-29 11:01:48
 */
@Service
@Slf4j
public class OrderPayNoServiceImpl extends ServiceImpl<OrderPayNoMapper, OrderPayNo> implements IOrderPayNoService {

    @Autowired
    private OrderPayNoMapper orderPayNoMapper;
    

}