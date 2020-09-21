package com.microtomato.hirun.modules.bss.supply.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.bss.supply.entity.dto.SupplierQueryDTO;
import com.microtomato.hirun.modules.bss.supply.entity.po.Supplier;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 供应商表(SupplySupplier)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-05 17:41:16
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface SupplierMapper extends BaseMapper<Supplier> {
    @Select(
            "select * from supply_supplier  ${ew.customSqlSegment}"
    )
    IPage<Supplier> queryByNameAndId(Page<SupplierQueryDTO> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}