package com.microtomato.hirun.modules.system.mapper;

import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.system.entity.po.RoleConsoleConfig;

/**
 * 角色控制台配置(RoleConsoleConfig)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-27 22:04:30
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface RoleConsoleConfigMapper extends BaseMapper<RoleConsoleConfig> {

}