package com.microtomato.hirun.modules.bss.salary.mapper;

import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.bss.salary.entity.po.SalaryRoyaltyDetail;

/**
 * 员工工资提成明细(SalaryRoyaltyDetail)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-17 17:57:21
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface SalaryRoyaltyDetailMapper extends BaseMapper<SalaryRoyaltyDetail> {

}