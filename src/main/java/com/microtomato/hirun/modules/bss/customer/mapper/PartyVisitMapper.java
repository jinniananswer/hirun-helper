package com.microtomato.hirun.modules.bss.customer.mapper;

import com.microtomato.hirun.modules.bss.customer.entity.po.PartyVisit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuhui
 * @since 2020-02-04
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface PartyVisitMapper extends BaseMapper<PartyVisit> {

}
