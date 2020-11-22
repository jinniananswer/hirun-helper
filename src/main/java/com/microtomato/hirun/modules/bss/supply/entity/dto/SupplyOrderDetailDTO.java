package com.microtomato.hirun.modules.bss.supply.entity.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @program: hirun-helper
 * @description: 供应商下单详情数据对象
 * @author: jinnian
 * @create: 2020-11-20 20:55
 **/
@Getter
@Setter
public class SupplyOrderDetailDTO {

    private Long id;

    private Long orderId;

    private String supplyOrderTypeName;

    private String supplyOrderType;

    private Long supplyId;

    private Long supplierId;

    private Long materialId;

    private String materialCode;

    private String materialName;

    private String supplierName;

    private String materialUnit;

    private String materialType;

    private String materialTypeName;

    private Integer materialNum;

    private Double fee;

    private Double totalMoney;

    private Double costPrice;

    private Double salePrice;

    private String standard;

    private String auditStatus;

    private String auditStatusName;

    private String auditComment;

    private Long auditEmployeeId;

    private Long createEmployeeId;

    private String createEmployeeName;

    private String auditEmployeeName;

    private String remark;
}
