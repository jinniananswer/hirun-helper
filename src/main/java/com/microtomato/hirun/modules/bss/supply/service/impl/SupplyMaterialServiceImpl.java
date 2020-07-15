package com.microtomato.hirun.modules.bss.supply.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.supply.entity.po.SupplyMaterial;
import com.microtomato.hirun.modules.bss.supply.mapper.SupplyMaterialMapper;
import com.microtomato.hirun.modules.bss.supply.service.ISupplyMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 材料表(SupplyMaterial)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-15 11:26:08
 */
@Service
@Slf4j
public class SupplyMaterialServiceImpl extends ServiceImpl<SupplyMaterialMapper, SupplyMaterial> implements ISupplyMaterialService {

    @Autowired
    private SupplyMaterialMapper supplyMaterialMapper;
    

}