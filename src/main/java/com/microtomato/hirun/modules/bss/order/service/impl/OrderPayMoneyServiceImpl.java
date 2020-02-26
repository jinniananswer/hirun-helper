package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.bss.order.mapper.OrderPayMoneyMapper;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayMoney;
import com.microtomato.hirun.modules.bss.order.service.IOrderPayMoneyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 付款类型明细表(OrderPayMoney)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-02-26 15:02:26
 */
@Slf4j
@Service
@DataSource(DataSourceKey.INS)
public class OrderPayMoneyServiceImpl extends ServiceImpl<OrderPayMoneyMapper, OrderPayMoney> implements IOrderPayMoneyService {

    @Autowired
    private OrderPayMoneyMapper orderPayMoneyMapper;
    

}