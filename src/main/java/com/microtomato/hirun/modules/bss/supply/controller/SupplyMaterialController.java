package com.microtomato.hirun.modules.bss.supply.controller;



import com.microtomato.hirun.modules.bss.supply.service.ISupplyMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 材料表(SupplyMaterial)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-15 11:26:08
 */
@RestController
@RequestMapping("/api/bss.supply/SupplyMaterial")
public class SupplyMaterialController {

    /**
     * 服务对象
     */
    @Autowired
    private ISupplyMaterialService supplyMaterialService;

}