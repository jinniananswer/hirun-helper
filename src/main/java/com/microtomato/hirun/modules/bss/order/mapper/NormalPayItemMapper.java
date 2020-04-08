package com.microtomato.hirun.modules.bss.order.mapper;

import com.microtomato.hirun.modules.bss.order.entity.po.NormalPayItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;

/**
 * <p>
 * 非主营支付项明细表 Mapper 接口
 * </p>
 *
 * @author sunxin
 * @since 2020-03-30
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface NormalPayItemMapper extends BaseMapper<NormalPayItem> {

}
