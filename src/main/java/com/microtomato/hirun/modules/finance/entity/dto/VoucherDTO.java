package com.microtomato.hirun.modules.finance.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 财务付款数据对象
 * @author: jinnian
 * @create: 2020-11-11 23:13
 **/
@Getter
@Setter
public class VoucherDTO {

    private String voucherNo;

    private String item;

    private LocalDate voucherDate;

    private Double totalMoney;

    private String remark;

    private List<VoucherItemDTO> voucherItems;
}
