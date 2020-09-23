package com.microtomato.hirun.modules.bss.order.mapper;

import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderMaterialContract;

/**
 * (OrderMaterialContract)表数据库访问层
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-09-24 00:05:42
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface OrderMaterialContractMapper extends BaseMapper<OrderMaterialContract> {

}