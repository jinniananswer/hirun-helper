package com.microtomato.hirun.modules.bss.supply.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.modules.bss.order.entity.po.NormalPayNo;
import com.microtomato.hirun.modules.bss.supply.entity.po.Supplier;
import com.microtomato.hirun.modules.bss.supply.mapper.SupplierMapper;
import com.microtomato.hirun.modules.bss.supply.service.ISupplierService;
import lombok.extern.slf4j.Slf4j;
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
        return this.getOne(new QueryWrapper<Supplier>().lambda()
                .eq(Supplier::getId,supplierId)
                .eq(Supplier::getStatus,0));
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



}