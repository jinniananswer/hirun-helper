package com.microtomato.hirun.modules.bss.config.mapper;

import com.microtomato.hirun.modules.bss.config.entity.po.OrderStatusCfg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;

/**
 * <p>
 * 订单阶段及状态配置表 Mapper 接口
 * </p>
 *
 * @author jinnian
 * @since 2020-02-09
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface OrderStatusCfgMapper extends BaseMapper<OrderStatusCfg> {

}
