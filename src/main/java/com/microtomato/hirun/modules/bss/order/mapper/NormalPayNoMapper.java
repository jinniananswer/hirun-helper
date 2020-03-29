package com.microtomato.hirun.modules.bss.order.mapper;

import com.microtomato.hirun.modules.bss.order.entity.po.NormalPayNo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;

/**
 * <p>
 * 非主营支付流水表 Mapper 接口
 * </p>
 *
 * @author sunxin
 * @since 2020-03-26
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface NormalPayNoMapper extends BaseMapper<NormalPayNo> {

}
