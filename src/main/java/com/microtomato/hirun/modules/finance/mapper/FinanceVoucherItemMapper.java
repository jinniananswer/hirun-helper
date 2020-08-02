package com.microtomato.hirun.modules.finance.mapper;

import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.finance.entity.po.FinanceVoucherItem;

/**
 * 财务领款单明细表(FinanceVoucherItem)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-25 21:25:22
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface FinanceVoucherItemMapper extends BaseMapper<FinanceVoucherItem> {

}