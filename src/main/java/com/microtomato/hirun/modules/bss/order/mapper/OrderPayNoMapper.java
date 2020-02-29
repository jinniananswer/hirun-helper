package com.microtomato.hirun.modules.bss.order.mapper;

import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayNo;

/**
 * 订单支付流水表表(OrderPayNo)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-02-29 11:01:48
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface OrderPayNoMapper extends BaseMapper<OrderPayNo> {

}