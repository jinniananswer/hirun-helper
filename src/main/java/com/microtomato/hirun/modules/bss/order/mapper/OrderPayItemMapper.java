package com.microtomato.hirun.modules.bss.order.mapper;

import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;

/**
 * <p>
 * 订单支付项明细表 Mapper 接口
 * </p>
 *
 * @author jinnian
 * @since 2020-02-21
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface OrderPayItemMapper extends BaseMapper<OrderPayItem> {

}
