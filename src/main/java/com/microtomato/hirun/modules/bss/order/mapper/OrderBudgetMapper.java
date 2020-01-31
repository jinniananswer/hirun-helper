package com.microtomato.hirun.modules.bss.order.mapper;

import com.microtomato.hirun.modules.bss.order.entity.po.OrderBudget;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;

/**
 * <p>
 * 订单预算表 Mapper 接口
 * </p>
 *
 * @author anwenxuan
 * @since 2020-01-29
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface OrderBudgetMapper extends BaseMapper<OrderBudget> {

}
