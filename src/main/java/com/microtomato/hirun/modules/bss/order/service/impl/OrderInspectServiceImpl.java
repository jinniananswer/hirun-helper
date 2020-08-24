package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderInspect;
import com.microtomato.hirun.modules.bss.order.mapper.OrderInspectMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderInspectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (OrderInspect)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-08-11 18:07:44
 */
@Service
@Slf4j
public class OrderInspectServiceImpl extends ServiceImpl<OrderInspectMapper, OrderInspect> implements IOrderInspectService {

    @Autowired
    private OrderInspectMapper orderInspectMapper;
    

}