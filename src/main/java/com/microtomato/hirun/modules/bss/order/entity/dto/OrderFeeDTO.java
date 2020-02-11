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

    private Long parentFeeItemId;

    private Long feeItemId;

    private String parentFeeItemName;

    private String feeItemName;

    private String payee;

    private String collectionDate;

    private Long paymentType;

    private Integer period;

    private Integer collectedFee;

    private Integer oldCollectedFee;

    private Integer actFee;

    private Long feeEmployeeId;

    private Long contractId;

    private String cash;

    private String oldCash;

    private String industrialBankCard;

    private String oldIndustrialBankCard;

    private String pudongDevelopmentBankCard;

    private String oldPudongDevelopmentBankCard;

    private String constructionBankBasic;

    private String oldConstructionBankBasic;

    private String constructionBank3797;

    private String oldConstructionBank3797;

    private String ICBC3301;

    private String oldICBC3301;

    private String ICBCInstallment;

    private String oldICBCInstallment;

    private String ABCInstallment;

    private String oldABCInstallment;

    private String summary;

    private String oldSummary;


}
