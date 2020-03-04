package com.microtomato.hirun.modules.bss.config.mapper;

import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.bss.config.entity.po.FeeItemCfg;

/**
 * 费用项配置表(FeeItemCfg)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-03-04 22:22:39
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface FeeItemCfgMapper extends BaseMapper<FeeItemCfg> {

}