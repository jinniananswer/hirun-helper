package com.microtomato.hirun.modules.bss.config.mapper;

import com.microtomato.hirun.modules.bss.config.entity.po.OrderRoleCfg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;

/**
 * <p>
 * 订单所需工作人员角色配置表 Mapper 接口
 * </p>
 *
 * @author jinnian
 * @since 2020-02-03
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface OrderRoleCfgMapper extends BaseMapper<OrderRoleCfg> {

}
