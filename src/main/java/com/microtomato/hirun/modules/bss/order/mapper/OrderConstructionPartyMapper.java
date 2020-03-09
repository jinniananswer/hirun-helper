package com.microtomato.hirun.modules.bss.order.mapper;

import com.microtomato.hirun.modules.bss.order.entity.po.OrderConstructionParty;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;

/**
 * <p>
 * 施工参与人表 Mapper 接口
 * </p>
 *
 * @author sunxin
 * @since 2020-03-04
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface OrderConstructionPartyMapper extends BaseMapper<OrderConstructionParty> {

}
