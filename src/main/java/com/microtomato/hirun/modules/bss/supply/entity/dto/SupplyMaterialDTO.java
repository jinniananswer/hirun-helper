package com.microtomato.hirun.modules.bss.supply.entity.dto;

import lombok.Data;



@Data
public class SupplyMaterialDTO {

    private Long id;

    private String name;

    private String supplierName;

    private String materialUnit;

    private String materialType;

    private String materialTypeName;

    private Long supplierId;

    private String num;

    private Integer costPrice;

    private Integer salePrice;

    private String standard;
}
