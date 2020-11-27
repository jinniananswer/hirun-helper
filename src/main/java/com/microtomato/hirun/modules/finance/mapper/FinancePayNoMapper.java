package com.microtomato.hirun.modules.finance.mapper;

import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.finance.entity.po.FinancePayNo;

/**
 * 会计付款流水表(FinancePayNo)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-11-24 15:57:13
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface FinancePayNoMapper extends BaseMapper<FinancePayNo> {

}