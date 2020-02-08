package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 订单费用数据传输对象
 * @author: sunxin
 * @create: 2020-02-05
 **/
@Data
public class OrderFeeDTO {

    private Long orderId;

    private Long feeItemId;

    private Integer period;

    private Integer collectedFee;

    private Integer actFee;

    private Long feeEmployeeId;

    private Long contractId;

    private String cash;

    private String industrialBankCard;

    private String pudongDevelopmentBankCard;

    private String constructionBankBasic;

    private String constructionBank3797;

    private String ICBC3301;

    private String ICBCInstallment;

    private String ABCInstallment;


}
