package com.microtomato.hirun.modules.finance.mapper;

import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.finance.entity.po.FinanceVoucher;

/**
 * 财务领款单表(FinanceVoucher)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-25 21:25:21
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface FinanceVoucherMapper extends BaseMapper<FinanceVoucher> {

}