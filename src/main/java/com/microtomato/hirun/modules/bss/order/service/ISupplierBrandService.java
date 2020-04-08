package com.microtomato.hirun.modules.bss.order.service;

import com.microtomato.hirun.modules.bss.order.entity.po.SupplierBrand;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 供应商品牌表 服务类
 * </p>
 *
 * @author sunxin
 * @since 2020-03-29
 */
public interface ISupplierBrandService extends IService<SupplierBrand> {

    /**
     * 查询所有
     * @param
     * @return
     */
    List<SupplierBrand> queryAllInfo();

}
