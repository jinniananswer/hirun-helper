package com.microtomato.hirun.modules.organization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
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
@Order(-1)
@DataSource(DataSourceKey.INS)
public interface EmployeeWorkExperienceMapper extends BaseMapper<EmployeeWorkExperience> {

}
