package com.microtomato.hirun.modules.bss.customer.mapper;

import com.microtomato.hirun.modules.bss.customer.entity.po.CustBase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;

/**
 * <p>
 * 客户信息 Mapper 接口
 * </p>
 *
 * @author liuhui
 * @since 2020-02-01
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface CustBaseMapper extends BaseMapper<CustBase> {

}
