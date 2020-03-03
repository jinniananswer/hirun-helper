package com.microtomato.hirun.modules.bss.order.mapper;

import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFeeItem;

/**
 * 订单费用明细表(OrderFeeItem)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-03-03 21:57:46
 */
@Storage
@DataSource(DataSourceKey.ORD)
public interface OrderFeeItemMapper extends BaseMapper<OrderFeeItem> {

}