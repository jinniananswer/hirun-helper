package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * @program: hirun-helper
 * @description: 二期款收取中传输对象
 * @author: anwx
 * @create: 2020-02-05
 **/
@Data
public class SecondInstallmentCollectionDTO {

    private Long orderId;

    private Integer secondInstallmentFee;

    private Integer secondContractFee;

    private Integer woodProductFee;

    private Integer baseDecorationFee;

    private Integer doorFee;

    private Integer taxFee;

    private Integer furnitureFee;

    private Integer otherFee;

    private Long financeEmployeeId;
}
