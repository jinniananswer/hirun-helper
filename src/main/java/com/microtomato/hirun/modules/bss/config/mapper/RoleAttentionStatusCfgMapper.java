package com.microtomato.hirun.modules.bss.config.mapper;

import com.microtomato.hirun.modules.bss.config.entity.po.RoleAttentionStatusCfg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;

/**
 * <p>
 * 角色关注的订单状态配置表 Mapper 接口
 * </p>
 *
 * @author jinnian
 * @since 2020-02-11
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface RoleAttentionStatusCfgMapper extends BaseMapper<RoleAttentionStatusCfg> {

}
