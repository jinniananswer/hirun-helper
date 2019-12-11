package com.microtomato.hirun.modules.organization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeKeyman;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jinnian
 * @since 2019-12-10
 */
@Storage
@DS("ins")
public interface EmployeeKeymanMapper extends BaseMapper<EmployeeKeyman> {

}
