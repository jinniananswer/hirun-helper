package com.microtomato.hirun.modules.bss.order.mapper;

import com.microtomato.hirun.modules.bss.order.entity.po.OrderContract;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;

/**
 * <p>
 * 订单合同 Mapper 接口
 * </p>
 *
 * @author anwx
 * @since 2020-02-23
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface OrderContractMapper extends BaseMapper<OrderContract> {

}
