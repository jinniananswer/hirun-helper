package com.microtomato.hirun.modules.organization.mapper;

import com.microtomato.hirun.modules.organization.entity.po.EmployeeTag;
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
 * @since 2020-05-04
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface EmployeeTagMapper extends BaseMapper<EmployeeTag> {

}
