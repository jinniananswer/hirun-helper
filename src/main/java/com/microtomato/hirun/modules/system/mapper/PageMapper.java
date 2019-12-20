package com.microtomato.hirun.modules.system.mapper;

import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.modules.system.entity.po.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;

/**
 * <p>
 * 不用挂菜单的页面 Mapper 接口
 * </p>
 *
 * @author Steven
 * @since 2019-11-15
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface PageMapper extends BaseMapper<Page> {

}
