package com.microtomato.hirun.modules.bss.order.mapper;

import com.microtomato.hirun.modules.bss.order.entity.po.SupplierBrand;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;

/**
 * <p>
 * 供应商品牌表 Mapper 接口
 * </p>
 *
 * @author sunxin
 * @since 2020-03-29
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface SupplierBrandMapper extends BaseMapper<SupplierBrand> {

}
