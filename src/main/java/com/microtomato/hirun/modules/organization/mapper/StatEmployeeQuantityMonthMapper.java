package com.microtomato.hirun.modules.organization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.organization.entity.po.StatEmployeeQuantityMonth;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuhui
 * @since 2019-12-17
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface StatEmployeeQuantityMonthMapper extends BaseMapper<StatEmployeeQuantityMonth> {

}
