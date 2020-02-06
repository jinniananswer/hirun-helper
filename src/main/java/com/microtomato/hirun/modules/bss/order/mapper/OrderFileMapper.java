package com.microtomato.hirun.modules.bss.order.mapper;

import com.microtomato.hirun.modules.bss.order.entity.po.OrderFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;

/**
 * <p>
 * 订单文件 Mapper 接口
 * </p>
 *
 * @author Steven
 * @since 2020-02-06
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface OrderFileMapper extends BaseMapper<OrderFile> {

}
