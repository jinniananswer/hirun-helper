package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFeeItem;
import com.microtomato.hirun.modules.bss.order.mapper.OrderFeeItemMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderFeeItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单费用明细表(OrderFeeItem)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-03-03 21:57:46
 */
@Service
@Slf4j
@DataSource(DataSourceKey.INS)
public class OrderFeeItemServiceImpl extends ServiceImpl<OrderFeeItemMapper, OrderFeeItem> implements IOrderFeeItemService {

    @Autowired
    private OrderFeeItemMapper orderFeeItemMapper;
    

}