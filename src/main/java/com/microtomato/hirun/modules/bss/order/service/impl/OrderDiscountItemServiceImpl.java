package com.microtomato.hirun.modules.bss.order.service.impl;

import com.microtomato.hirun.modules.bss.order.entity.po.OrderDiscountItem;
import com.microtomato.hirun.modules.bss.order.mapper.OrderDiscountItemMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderDiscountItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 订单优惠项 服务实现类
 * </p>
 *
 * @author anwx
 * @since 2020-02-26
 */
@Slf4j
@Service
public class OrderDiscountItemServiceImpl extends ServiceImpl<OrderDiscountItemMapper, OrderDiscountItem> implements IOrderDiscountItemService {

}
