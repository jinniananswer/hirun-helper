package com.microtomato.hirun.modules.organization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.modules.organization.entity.po.Org;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jinnian
 * @since 2019-09-16
 */
@Storage
@DS("ins")
public interface OrgMapper extends BaseMapper<Org> {

}
