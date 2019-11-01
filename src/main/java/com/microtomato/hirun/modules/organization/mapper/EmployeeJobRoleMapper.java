package com.microtomato.hirun.modules.organization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jinnian
 * @since 2019-10-09
 */
@Mapper
@DS("ins")
public interface EmployeeJobRoleMapper extends BaseMapper<EmployeeJobRole> {

}
