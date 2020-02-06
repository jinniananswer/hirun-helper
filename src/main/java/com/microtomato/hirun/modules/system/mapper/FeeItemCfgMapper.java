package com.microtomato.hirun.modules.system.mapper;

import com.microtomato.hirun.modules.system.entity.po.FeeItemCfg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;

/**
 * <p>
 * 费用项配置表 Mapper 接口
 * </p>
 *
 * @author sunxin
 * @since 2020-02-06
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface FeeItemCfgMapper extends BaseMapper<FeeItemCfg> {

}
