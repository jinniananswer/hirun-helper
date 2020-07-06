package com.microtomato.hirun.modules.bss.config.mapper;

import com.microtomato.hirun.modules.bss.config.entity.po.PrepareConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuhui
 * @since 2020-02-14
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface PrepareConfigMapper extends BaseMapper<PrepareConfig> {

}
