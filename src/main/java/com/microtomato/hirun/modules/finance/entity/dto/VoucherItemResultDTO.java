package com.microtomato.hirun.modules.finance.entity.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * @program: hirun-helper
 * @description: 领款单详情
 * @author: jinnian
 * @create: 2020-11-22 20:39
 **/
@Data
public class VoucherItemResultDTO {

    private String voucherNo;

    private String type;

    private String typeName;

    private String item;

    private String itemName;

    private String projectName;

    private String financeItemId;

    private String financeItemName;

    private Double fee;

    private String trafficType;

    private LocalDate trafficDate;

    private String trafficBegin;

    private String trafficEnd;

    private Double trafficFee;

    private Double allowance;

    private Double hotelFee;

    private String remark;
}
