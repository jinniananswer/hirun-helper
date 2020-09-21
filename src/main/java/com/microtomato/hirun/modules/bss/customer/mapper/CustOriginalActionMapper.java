package com.microtomato.hirun.modules.bss.customer.mapper;

import com.microtomato.hirun.modules.bss.customer.entity.po.CustOriginalAction;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;

/**
 * <p>
 * 客户原始动作记录 Mapper 接口
 * </p>
 *
 * @author liuhui
 * @since 2020-04-30
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface CustOriginalActionMapper extends BaseMapper<CustOriginalAction> {

}
