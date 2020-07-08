package com.microtomato.hirun.modules.bss.supply.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.supply.entity.dto.SupplierQueryDTO;
import com.microtomato.hirun.modules.bss.supply.entity.po.Supplier;
import com.microtomato.hirun.modules.bss.supply.mapper.SupplierMapper;
import com.microtomato.hirun.modules.bss.supply.service.ISupplierService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 供应商表(SupplySupplier)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-05 17:41:16
 */
@Service
@Slf4j
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements ISupplierService {

    @Autowired
    private SupplierMapper supplierMapper;

    @Override
    public IPage<Supplier> queryByNameAndId(SupplierQueryDTO supplierQueryDTO) {
        QueryWrapper<Supplier> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(null != supplierQueryDTO.getId(), "id", supplierQueryDTO.getId());
        queryWrapper.like(StringUtils.isNotEmpty(supplierQueryDTO.getName()), "name", supplierQueryDTO.getName());
        Page<SupplierQueryDTO> page = new Page<>(supplierQueryDTO.getPage(), supplierQueryDTO.getLimit());
        return this.supplierMapper.queryByNameAndId(page, queryWrapper);
    }
}