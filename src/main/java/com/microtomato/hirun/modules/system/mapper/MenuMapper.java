package com.microtomato.hirun.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.modules.system.entity.po.Menu;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jinnian
 * @since 2019-09-05
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface MenuMapper extends BaseMapper<Menu> {

}
