package com.microtomato.hirun.modules.bss.supply.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.bss.supply.entity.dto.SupplierQueryDTO;
import com.microtomato.hirun.modules.bss.supply.entity.po.Supplier;
import com.microtomato.hirun.modules.bss.supply.mapper.SupplierMapper;
import com.microtomato.hirun.modules.bss.supply.service.ISupplierService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 供应商表(SupplySupplier)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-05 17:41:16
 */
@Service
@Slf4j
@DataSource(DataSourceKey.INS)
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements ISupplierService {

    @Autowired
    private SupplierMapper supplierMapper;

    /**
     * 根据供应商id查询供应商信息
     *
     * @param supplierId
     * @return
     */
    @Override
    public Supplier querySupplierById(Long supplierId){
//        return this.getOne(new QueryWrapper<Supplier>().lambda()
//                .eq(Supplier::getId,supplierId)
//                .eq(Supplier::getStatus,0));
        return supplierMapper.selectById(supplierId);
    }

    /**
     * 查询供应商信息
     *
     * @param
     * @return
     */
    @Override
    public List<Supplier> loadSupplierInfos(){
        return this.list(new QueryWrapper<Supplier>().lambda()
                .eq(Supplier::getStatus,0));
    }

    @Override
    public IPage<Supplier> queryByNameAndId(SupplierQueryDTO supplierQueryDTO) {
        QueryWrapper<Supplier> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(null != supplierQueryDTO.getId(), "id", supplierQueryDTO.getId());
        queryWrapper.like(StringUtils.isNotEmpty(supplierQueryDTO.getName()), "name", supplierQueryDTO.getName());
        queryWrapper.eq("status", '0');
        Page<SupplierQueryDTO> page = new Page<>(supplierQueryDTO.getPage(), supplierQueryDTO.getLimit());
        return this.supplierMapper.queryByNameAndId(page, queryWrapper);
    }

    @Override
    public boolean deleteSupplierByIds(List<Supplier> supplierList){
        return super.updateBatchById(supplierList);
    }
}