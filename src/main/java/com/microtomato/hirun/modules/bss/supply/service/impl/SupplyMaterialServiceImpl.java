package com.microtomato.hirun.modules.bss.supply.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.config.entity.po.FeeItemCfg;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFeeInfoDTO;
import com.microtomato.hirun.modules.bss.supply.entity.dto.SupplyMaterialDTO;
import com.microtomato.hirun.modules.bss.supply.entity.po.SupplyMaterial;
import com.microtomato.hirun.modules.bss.supply.mapper.SupplyMaterialMapper;
import com.microtomato.hirun.modules.bss.supply.service.ISupplierService;
import com.microtomato.hirun.modules.bss.supply.service.ISupplyMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private ISupplierService SupplierServiceImpl;

    /**
     * 查询所有材料信息
     *
     * @param
     * @return
     */
    @Override
    public  List<SupplyMaterialDTO> loadMaterial() {
        List<SupplyMaterial> materials= this.list(new QueryWrapper<SupplyMaterial>().lambda()
                .eq(SupplyMaterial::getStatus,0));
        List<SupplyMaterialDTO> supplyMaterials = new ArrayList<SupplyMaterialDTO>();
        for (SupplyMaterial material : materials) {
            Long supplierId = material.getSupplierId();
            SupplyMaterialDTO supplyMaterial =   new SupplyMaterialDTO();
            supplyMaterial.setId(material.getId());
          // String  supplierName = this.SupplierServiceImpl.querySupplierById(supplierId).getName();
            supplyMaterial.setCostPrice(material.getCostPrice());
           // supplyMaterial.setMaterialType(material.getMaterialType());
            supplyMaterial.setMaterialUnit(material.getMaterialUnit());
            supplyMaterial.setSalePrice(material.getSalePrice());
            supplyMaterial.setName(material.getName());
          //  supplyMaterial.setSupplierName(supplierName);
            supplyMaterial.setStandard(material.getStandard());
            supplyMaterials.add(supplyMaterial);

        }
        log.debug("supplyMaterials========"+supplyMaterials);
        return supplyMaterials ;
    }
    

}