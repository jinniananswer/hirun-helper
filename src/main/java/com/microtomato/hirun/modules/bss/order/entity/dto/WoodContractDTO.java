package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * @program: hirun-helper
 * @description: 木制品合同数据传输对象
 * @author: liuhui
 * @create: 2020-03-08
 **/
@Data
public class WoodContractDTO {

    private Long orderId;

    private Long id;

    private LocalDate signDate;

    private LocalDate startDate;

    private LocalDate endDate;

    private String environmentalTestingAgency;

    private Double contractFee=0.0;

    private Double doorFee=0.0;

    private Double furnitureFee=0.0;

    private Double chargedDesignFee;

    private Double returnDesignFee=0.0;

    private Double taxFee=0.0;

    private String remark;

    private Double firstContractFee=0.0;

    private Long financeEmployeeId;

    private String financeEmployeeName;

    private Long projectEmployeeId;

    private String projectEmployeeName;

    private Double chargedWoodFee=0.0;

    private Double cupboardFee=0.0;
}
