package com.microtomato.hirun.modules.bss.supply.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.supply.entity.dto.SupplyMaterialDTO;
import com.microtomato.hirun.modules.bss.supply.entity.po.SupplyMaterial;

import java.util.List;

/**
 * 材料表(SupplyMaterial)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-15 11:26:08
 */
public interface ISupplyMaterialService extends IService<SupplyMaterial> {

    /**
     * 材料信息
     * @param
     * @return
     */
    List<SupplyMaterialDTO> loadMaterial();

}