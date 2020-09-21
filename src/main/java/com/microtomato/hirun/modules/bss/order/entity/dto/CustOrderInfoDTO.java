package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 客户订单信息
 * @author: jinnian
 * @create: 2020-02-15 17:02
 **/
@Data
public class CustOrderInfoDTO {

    private Long custId;

    private String custName;

    private String sex;

    private String sexName;

    private String mobileNo;

    private Long orderId;

    private String type;

    private String typeName;

    private Long housesId;

    private String housesName;

    private String decorateAddress;

    private String status;

    private String statusName;

    private String stage;

    private String stageName;

    private String houseLayout;

    private String houseLayoutName;

    private String floorage;

    private String indoorArea;

    private UsualFeeDTO usualFee;

    private Double designPay;

    private Double contractPay;

    private Double secondContractPay;

    private Double settlementPay;
}
