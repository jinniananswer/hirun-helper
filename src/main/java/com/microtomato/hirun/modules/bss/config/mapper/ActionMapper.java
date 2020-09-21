package com.microtomato.hirun.modules.bss.config.mapper;

import com.microtomato.hirun.modules.bss.config.entity.po.Action;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;

/**
 * <p>
 * 动作定义表 Mapper 接口
 * </p>
 *
 * @author liuhui
 * @since 2020-04-27
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface ActionMapper extends BaseMapper<Action> {

}
