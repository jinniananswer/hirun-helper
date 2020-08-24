package com.microtomato.hirun.modules.bss.supply.entity.dto;

import lombok.Data;



@Data
public class SupplyMaterialDTO {

    private Long id;

    private Long materialId;

    private String name;

    private String supplierName;

    private String materialUnit;

    private String materialType;

    private String materialTypeName;

    private Long supplierId;

    private Integer materialNum;

    private Double fee;

    private Double costPrice;

    private Double salePrice;

    private String standard;

    private String remark;
}
