package com.microtomato.hirun.modules.bss.order.mapper;

import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayMoney;

/**
 * 付款类型明细表(OrderPayMoney)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-02-26 15:05:44
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface OrderPayMoneyMapper extends BaseMapper<OrderPayMoney> {

}