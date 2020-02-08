package com.microtomato.hirun.modules.bss.order.service.impl;

import com.microtomato.hirun.modules.bss.order.entity.po.OrderPaymoney;
import com.microtomato.hirun.modules.bss.order.mapper.OrderPaymoneyMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderPaymoneyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 付款类型明细表 服务实现类
 * </p>
 *
 * @author sunxin
 * @since 2020-02-05
 */
@Slf4j
@Service
public class OrderPaymoneyServiceImpl extends ServiceImpl<OrderPaymoneyMapper, OrderPaymoney> implements IOrderPaymoneyService {

}
