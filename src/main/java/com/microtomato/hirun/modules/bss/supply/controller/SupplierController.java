package com.microtomato.hirun.modules.bss.supply.controller;



import com.microtomato.hirun.modules.bss.supply.service.ISupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 供应商表(SupplySupplier)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-05 17:41:16
 */
@RestController
@RequestMapping("/api/bss.supply/supplier")
public class SupplierController {

    /**
     * 服务对象
     */
    @Autowired
    private ISupplierService supplySupplierService;
}