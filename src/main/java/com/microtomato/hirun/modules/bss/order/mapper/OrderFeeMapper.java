package com.microtomato.hirun.modules.bss.order.mapper;

import com.microtomato.hirun.modules.bss.order.entity.po.OrderFee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;

/**
 * <p>
 * 订单费用表 Mapper 接口
 * </p>
 *
 * @author sunxin
 * @since 2020-02-05
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface OrderFeeMapper extends BaseMapper<OrderFee> {

}
