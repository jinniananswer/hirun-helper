package com.microtomato.hirun.modules.finance.entity.dto;

import com.microtomato.hirun.modules.bss.supply.entity.dto.SupplyMaterialDTO;
import lombok.Data;

import java.util.List;


@Data
public class FinanceVoucherDTO {

    private Long id;

    private Long orderId;

    private Long supplierId;

    private Long voucherType;

    private Double totalMoney;

    private String auditStatus;

    private Long decoratorId;

    private Double money;

    /**
     * 制单明细
     */
    private List<FinanceVoucherItemDTO> financeVoucherItemDTOList;
}
