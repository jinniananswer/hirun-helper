package com.microtomato.hirun.modules.bss.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPlaneSketch;

/**
 * @author ：mmzs
 * @date ：Created in 2020/2/6 20:09
 * @description：平面图数据映射
 * @modified By：
 * @version: 1$
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface OrderPlaneSketchMapper extends BaseMapper<OrderPlaneSketch> {
}
