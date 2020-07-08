package com.microtomato.hirun.modules.bss.supply.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.supply.entity.dto.SupplierQueryDTO;
import com.microtomato.hirun.modules.bss.supply.entity.po.Supplier;

/**
 * 供应商表(SupplySupplier)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-05 17:41:16
 */
public interface ISupplierService extends IService<Supplier> {
    /**
     * 根据id和供应商名称查询
     * @param supplierQueryDTO
     * @return
     */
    IPage<Supplier> queryByNameAndId(SupplierQueryDTO supplierQueryDTO);
}