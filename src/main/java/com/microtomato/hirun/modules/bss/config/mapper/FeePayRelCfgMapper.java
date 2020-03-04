package com.microtomato.hirun.modules.bss.config.mapper;

import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.bss.config.entity.po.FeePayRelCfg;

/**
 * 费用项与收款项配置表(FeePayRelCfg)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-03-05 00:12:28
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface FeePayRelCfgMapper extends BaseMapper<FeePayRelCfg> {

}