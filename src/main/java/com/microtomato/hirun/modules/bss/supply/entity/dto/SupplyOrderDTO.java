package com.microtomato.hirun.modules.bss.supply.entity.dto;

import lombok.Data;

import java.util.List;


@Data
public class SupplyOrderDTO {


    /**
     * 订单ID
     */
    private Long orderId;

    private Long supplierId;

    private String supplierName;

    private Long createUserId;

    private String createUserName;
    /**
     * 供应ID
     */
    private Long id;

    /** 1-主营材料下单 2-主营材料退单 3-材料入库 4-材料出库 */
    private String supplyOrderType;

    /** 1-主营材料下单 2-主营材料退单 3-材料入库 4-材料出库 */
    private String supplyOrderName;

    /**
     * 下单明细
     */
    private List<SupplyMaterialDTO> supplyMaterial;
}
