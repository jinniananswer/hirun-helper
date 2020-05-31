package com.microtomato.hirun.modules.organization.mapper;

import com.microtomato.hirun.modules.organization.entity.po.TrainSign;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuhui
 * @since 2020-04-09
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface TrainSignMapper extends BaseMapper<TrainSign> {

}
