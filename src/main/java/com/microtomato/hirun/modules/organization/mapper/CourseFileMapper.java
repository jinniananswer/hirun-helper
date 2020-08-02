package com.microtomato.hirun.modules.organization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.organization.entity.po.CourseFile;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huanghua
 * @since 2020-07-21
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface CourseFileMapper extends BaseMapper<CourseFile> {

}
