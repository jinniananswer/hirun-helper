package com.microtomato.hirun.modules.bss.order.mapper;

import com.microtomato.hirun.modules.bss.order.entity.po.OrderSettlement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuhui
 * @since 2020-03-07
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface OrderSettlementMapper extends BaseMapper<OrderSettlement> {

}
