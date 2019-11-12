package com.microtomato.hirun.modules.organization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeWorkExperience;
import org.springframework.core.annotation.Order;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jinnian
 * @since 2019-10-19
 */
@Storage
@DS("ins")
@Order(-1)
public interface EmployeeWorkExperienceMapper extends BaseMapper<EmployeeWorkExperience> {

}
