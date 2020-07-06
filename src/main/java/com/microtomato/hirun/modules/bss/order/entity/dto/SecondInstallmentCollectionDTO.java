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

    private String financeEmployeeName;

    private Double hydropowerSalary=0.0;

    private String hydropowerRemark;

    private Double woodworkerSalary=0.0;

    private String woodworkerRemark;

    private Double tilerSalary=0.0;

    private String tilerRemark;

    private Double painterSalary=0.0;

    private String painterRemark;

    private Double wallworkerSalary=0.0;

    private String wallworkerRemark;

    private Long chargedDecorateFee;
}
