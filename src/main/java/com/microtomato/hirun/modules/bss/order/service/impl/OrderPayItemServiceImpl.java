package com.microtomato.hirun.modules.bss.order.service.impl;

import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayItem;
import com.microtomato.hirun.modules.bss.order.mapper.OrderPayItemMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderPayItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 订单支付项明细表 服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2020-02-21
 */
@Slf4j
@Service
@DataSource(DataSourceKey.INS)
public class OrderPayItemServiceImpl extends ServiceImpl<OrderPayItemMapper, OrderPayItem> implements IOrderPayItemService {

}
