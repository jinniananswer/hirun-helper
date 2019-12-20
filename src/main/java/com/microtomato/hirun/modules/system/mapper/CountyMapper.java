package com.microtomato.hirun.modules.system.mapper;

import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.modules.system.entity.po.County;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jinnian
 * @since 2019-11-17
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface CountyMapper extends BaseMapper<County> {

}
