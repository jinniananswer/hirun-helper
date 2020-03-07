package com.microtomato.hirun.modules.bss.config.mapper;

import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.bss.config.entity.po.FeeItemStageCfg;

/**
 * 费用项分期配置表(FeeItemStageCfg)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-03-08 00:43:22
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface FeeItemStageCfgMapper extends BaseMapper<FeeItemStageCfg> {

}