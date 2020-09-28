package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFactoryOrderFollow;
import com.microtomato.hirun.modules.bss.order.mapper.OrderFactoryOrderFollowMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderFactoryOrderFollowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * (OrderFactoryOrderFollow)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-09-26 16:32:41
 */
@Service
@Slf4j
@DataSource(DataSourceKey.INS)
public class OrderFactoryOrderFollowServiceImpl extends ServiceImpl<OrderFactoryOrderFollowMapper, OrderFactoryOrderFollow> implements IOrderFactoryOrderFollowService {

    @Autowired
    private OrderFactoryOrderFollowMapper orderFactoryOrderFollowMapper;


    /**
     * 查询工厂订单跟单信息
     * @param factoryOrderId
     * @return
     */
    @Override
    public List<OrderFactoryOrderFollow> queryByFactoryOrderId(Long factoryOrderId) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        return this.list(Wrappers.<OrderFactoryOrderFollow>lambdaQuery()
                .eq(OrderFactoryOrderFollow::getFactoryOrderId, factoryOrderId)
                .ge(OrderFactoryOrderFollow::getEndDate, now));
    }
}