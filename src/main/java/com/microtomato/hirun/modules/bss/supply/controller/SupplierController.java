package com.microtomato.hirun.modules.bss.supply.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.supply.entity.dto.SupplierQueryDTO;
import com.microtomato.hirun.modules.bss.supply.entity.po.Supplier;
import com.microtomato.hirun.modules.bss.supply.service.ISupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 供应商表(SupplySupplier)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-05 17:41:16
 */
@RestController
@RequestMapping("/api/bss.supply/supplier")
@Slf4j
public class SupplierController {

    /**
     * 服务对象
     */
    @Autowired
    private ISupplierService supplySupplierService;

    @GetMapping("queryByNameAndId")
    @RestResult
    public IPage<Supplier> queryByNameAndId(SupplierQueryDTO supplierQueryDTO){
        return this.supplySupplierService.queryByNameAndId(supplierQueryDTO);
    }

    @PostMapping("updateSupplierById")
    @RestResult
    public boolean updateSupplierById(@RequestBody Supplier supplier){
        System.out.println(supplier);
        return this.supplySupplierService.updateById(supplier);
    }

    @PostMapping("deleteSupplierByIds")
    @RestResult
    public boolean deleteSupplierByIds(@RequestBody List<Supplier> supplierList){
        System.out.println(supplierList);
        return true;
    }
}