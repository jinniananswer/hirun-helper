package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.modules.bss.order.entity.po.SupplierBrand;
import com.microtomato.hirun.modules.bss.order.mapper.SupplierBrandMapper;
import com.microtomato.hirun.modules.bss.order.service.ISupplierBrandService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 * 供应商品牌表 服务实现类
 * </p>
 *
 * @author sunxin
 * @since 2020-03-29
 */
@Slf4j
@Service
public class SupplierBrandServiceImpl extends ServiceImpl<SupplierBrandMapper, SupplierBrand> implements ISupplierBrandService {

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
