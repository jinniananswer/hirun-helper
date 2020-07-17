package com.microtomato.hirun.modules.bss.supply.controller;



import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.order.entity.dto.NonCollectFeeDTO;
import com.microtomato.hirun.modules.bss.supply.entity.dto.SupplyMaterialDTO;
import com.microtomato.hirun.modules.bss.supply.entity.po.SupplyMaterial;
import com.microtomato.hirun.modules.bss.supply.service.ISupplyMaterialService;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/loadMaterial")
    @RestResult
    public  List<SupplyMaterialDTO> loadMaterial() {
        return supplyMaterialService.loadMaterial();

    }



}