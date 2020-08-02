package com.microtomato.hirun.modules.bss.supply.controller;



import com.microtomato.hirun.modules.bss.supply.service.ISupplierBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 供应商品牌表(SupplySupplierBrand)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-05 17:46:45
 */
@RestController
@RequestMapping("/api/bss.supply/supplier-brand")
public class SupplierBrandController {

    /**
     * 服务对象
     */
    @Autowired
    private ISupplierBrandService supplySupplierBrandService;

}