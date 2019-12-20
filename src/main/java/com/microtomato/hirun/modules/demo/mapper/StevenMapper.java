package com.microtomato.hirun.modules.demo.mapper;

import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.modules.demo.entity.po.Steven;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Steven
 * @since 2019-12-19
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface StevenMapper extends BaseMapper<Steven> {

}
