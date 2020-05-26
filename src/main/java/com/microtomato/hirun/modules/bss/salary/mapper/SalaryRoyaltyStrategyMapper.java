package com.microtomato.hirun.modules.bss.salary.mapper;

import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.bss.salary.entity.po.SalaryRoyaltyStrategy;

/**
 * 员工提成策略配置(SalaryRoyaltyStrategy)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-17 18:05:14
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface SalaryRoyaltyStrategyMapper extends BaseMapper<SalaryRoyaltyStrategy> {

}