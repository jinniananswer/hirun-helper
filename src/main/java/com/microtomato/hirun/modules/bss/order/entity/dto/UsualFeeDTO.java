package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * @program: hirun-helper
 * @description: 常用的费用信息查询数据对象
 * @author: jinnian
 * @create: 2020-07-12 15:39
 **/
@Data
public class UsualFeeDTO {

    /**
     * 首期合同金额+返回设计费金额
     */
    private Double totalContractFee;

    /**
     * 首期合同金额、净合同金额
     */
    private Double contractFee;

    /**
     * 首期应付款
     */
    private Double needPay;

    /**
     * 首期已付款
     */
    private Double payed;

    /**
     * 付首期款时间
     */
    private LocalDate payDate;

    /**
     * 基础装修金额
     */
    private Double basicFee;

    /**
     * 门金额
     */
    private Double doorFee;

    /**
     * 家具金额
     */
    private Double furnitureFee;

    /**
     * 二期合同金额
     */
    private Double secondContractFee;

    /**
     * 二期基础装修金额
     */
    private Double secondBasicFee;

    /**
     * 二期门金额
     */
    private Double secondDoorFee;

    /**
     * 二期家具金额
     */
    private Double secondFurnitureFee;

    /**
     * 二期应付款
     */
    private Double secondNeedPay;

    /**
     * 二期已付款
     */
    private Double secondPayed;

    /**
     * 付二期款时间
     */
    private LocalDate secondPayDate;

    /**
     * 结算合同金额
     */
    private Double settlementContractFee;

    /**
     * 结算基础金额
     */
    private Double settlementBasicFee;

    /**
     * 结算门金额
     */
    private Double settlementDoorFee;

    /**
     * 结算家具金额
     */
    private Double settlementFurnitureFee;

    /**
     * 结算应付金额
     */
    private Double settlementNeedPay;

    /**
     * 结算已付金额
     */
    private Double settlementPayed;

    /**
     * 付结算款时间
     */
    private LocalDate settlementPayDate;

    /**
     * 橱柜款
     */
    private Double cabinetFee;

    /**
     * 主材款
     */
    private Double materialFee;

    /**
     * 返还设计费查询
     */
    private Double backDesignFee;

    /**
     * 设计费用
     */
    private Double designFee;

    /**
     * 设计费应付
     */
    private Double designNeedPay;

    /**
     * 设计费已付
     */
    private Double designPayed;

    /**
     * 付设计费时间
     */
    private LocalDate payDesignDate;
}
