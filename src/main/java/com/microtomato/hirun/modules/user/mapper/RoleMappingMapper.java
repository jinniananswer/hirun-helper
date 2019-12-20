package com.microtomato.hirun.modules.user.mapper;

import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.modules.user.entity.po.RoleMapping;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Steven
 * @since 2019-11-17
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface RoleMappingMapper extends BaseMapper<RoleMapping> {

}
