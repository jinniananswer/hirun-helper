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

    private Double contractFee;

    private Double doorFee;

    private Double furnitureFee;

    private Integer chargedDesignFee;

    private Double returnDesignFee;

    private Double taxFee;

    private String remark;

    private Double firstContractFee;

    private Long financeEmployeeId;

    private Long projectEmployeeId;

    private Double chargedWoodFee;
}
