package com.microtomato.hirun.modules.bss.config.mapper;

import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.bss.config.entity.po.SalaryStatusFeeMapping;

/**
 * 订单状态与费用映射关系配置(SalaryStatusFeeMapping)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-20 17:56:54
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface SalaryStatusFeeMappingMapper extends BaseMapper<SalaryStatusFeeMapping> {

}