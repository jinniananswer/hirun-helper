package com.microtomato.hirun.modules.bss.order.mapper;

import com.microtomato.hirun.modules.bss.order.entity.po.OrderDiscountItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;

/**
 * <p>
 * 订单优惠项 Mapper 接口
 * </p>
 *
 * @author anwx
 * @since 2020-02-26
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface OrderDiscountItemMapper extends BaseMapper<OrderDiscountItem> {

}
