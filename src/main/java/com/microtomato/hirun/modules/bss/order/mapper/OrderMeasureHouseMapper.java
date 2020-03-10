package com.microtomato.hirun.modules.bss.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderMeasureHouse;

/**
 * @author ：mmzs
 * @date ：Created in 2020/2/4 21:11
 * @description：量房
 * @modified By：
 * @version: 1$
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface OrderMeasureHouseMapper extends BaseMapper<OrderMeasureHouse>{
}
