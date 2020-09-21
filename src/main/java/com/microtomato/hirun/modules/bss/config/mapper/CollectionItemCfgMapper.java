package com.microtomato.hirun.modules.bss.config.mapper;

import com.microtomato.hirun.modules.bss.config.entity.po.CollectionItemCfg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;

/**
 * <p>
 * 非主营收款项配置表 Mapper 接口
 * </p>
 *
 * @author sunxin
 * @since 2020-03-09
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface CollectionItemCfgMapper extends BaseMapper<CollectionItemCfg> {

}
