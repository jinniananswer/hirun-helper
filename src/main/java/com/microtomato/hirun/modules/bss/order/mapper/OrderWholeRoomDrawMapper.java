package com.microtomato.hirun.modules.bss.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderWholeRoomDraw;

/**
 * @author ：mmzs
 * @date ：Created in 2020/2/6 23:20
 * @description：订单全房图数据映射器
 * @modified By：
 * @version: 1.0$
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface OrderWholeRoomDrawMapper extends BaseMapper<OrderWholeRoomDraw> {

}
