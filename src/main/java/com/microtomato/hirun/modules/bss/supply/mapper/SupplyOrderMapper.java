package com.microtomato.hirun.modules.bss.supply.mapper;

import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.bss.supply.entity.po.SupplyOrder;

/**
 * 供应订单表(SupplyOrder)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-15 11:26:08
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface SupplyOrderMapper extends BaseMapper<SupplyOrder> {

}