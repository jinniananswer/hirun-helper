package com.microtomato.hirun.modules.bss.supply.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.supply.entity.po.SupplierBrand;
import com.microtomato.hirun.modules.bss.supply.mapper.SupplierBrandMapper;
import com.microtomato.hirun.modules.bss.supply.service.ISupplierBrandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 供应商品牌表(SupplySupplierBrand)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-05 17:46:45
 */
@Service
@Slf4j
public class SupplierBrandServiceImpl extends ServiceImpl<SupplierBrandMapper, SupplierBrand> implements ISupplierBrandService {

    @Autowired
    private SupplierBrandMapper supplierBrandMapper;


    @Override
    public List<SupplierBrand> queryAllInfo() {
        return this.list(new QueryWrapper<SupplierBrand>().lambda()
                .eq(SupplierBrand::getStatus, "0"));
    }

    /**
     * 根据供应商ID查询供应商信息
     * @param supplierId
     * @return
     */
    @Override
    public SupplierBrand getSupplierBrand(Long supplierId) {
        SupplierBrand supplierBrand = this.getById(supplierId);
        return supplierBrand;
    }
}