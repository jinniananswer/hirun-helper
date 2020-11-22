package com.microtomato.hirun.modules.bss.supply.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 材料制单DTO数据
 * @author: jinnian
 * @create: 2020-11-22 14:39
 **/
@Getter
@Setter
public class MaterialVoucherDTO {

    private LocalDate voucherDate;

    private String financeItemId;

    private String remark;

    private List<SupplyOrderDetailDTO> details;
}
