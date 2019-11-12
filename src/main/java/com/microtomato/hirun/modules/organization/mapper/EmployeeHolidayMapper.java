package com.microtomato.hirun.modules.organization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeHoliday;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuhui
 * @since 2019-10-20
 */
@Storage
@DS("ins")
public interface EmployeeHolidayMapper extends BaseMapper<EmployeeHoliday> {

}
