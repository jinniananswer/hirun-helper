package com.microtomato.hirun.modules.bss.plan.mapper;

import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.bss.plan.entity.po.PlanAgentMonth;

/**
 * (PlanAgentMonth)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-06-30 00:13:37
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface PlanAgentMonthMapper extends BaseMapper<PlanAgentMonth> {

}