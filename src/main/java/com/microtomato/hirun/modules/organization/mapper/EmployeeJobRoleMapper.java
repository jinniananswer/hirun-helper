package com.microtomato.hirun.modules.organization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jinnian
 * @since 2019-10-09
 */
@Storage
@DS("ins")
public interface EmployeeJobRoleMapper extends BaseMapper<EmployeeJobRole> {

}
