package com.microtomato.hirun.modules.bss.supply.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.supply.entity.po.SupplierBrand;

import java.util.List;

/**
 * 供应商品牌表(SupplySupplierBrand)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-05 17:46:45
 */
public interface ISupplierBrandService extends IService<SupplierBrand> {

    List<SupplierBrand> queryAllInfo();

    SupplierBrand getSupplierBrand(Long supplierId);

    SupplierBrand queryByPayItem(Long payItemId);
}