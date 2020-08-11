package com.microtomato.hirun.modules.bss.order.mapper;

import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderInspect;

/**
 * (OrderInspect)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-08-11 18:07:44
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface OrderInspectMapper extends BaseMapper<OrderInspect> {

}