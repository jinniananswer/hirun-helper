package com.microtomato.hirun.modules.bss.order.entity.dto.fee;

import com.microtomato.hirun.modules.bss.order.entity.dto.UsualOrderWorkerDTO;
import lombok.Data;

import java.time.LocalDate;

/**
 * @program: hirun-helper
 * @description: 查询工程费用数据返回对象
 * @author: jinnian
 * @create: 2020-07-12 02:07
 **/
@Data
public class ProjectFeeDTO {

    private Long orderId;

    private Long custId;

    private String custNo;

    private String custName;

    private String decorateAddress;

    private String type;

    private String status;

    private String orderStatusName;

    private String houseLayout;

    private String houseLayoutName;

    private String indoorArea;

    private UsualOrderWorkerDTO usualWorker;

    private String reportStatusName;

    private Double totalContractFee;

    private Double backDesignFee;

    private Double cacheDiscount;

    private Double contractFee;

    private LocalDate contractStartDate;

    private LocalDate contractEndDate;

    private LocalDate firstPayDate;

    private Double firstPay;

    private String firstPayFull;

    private LocalDate secondPayDate;

    private Double secondPay;

    private String secondPayFull;

    private LocalDate checkDate;

    private LocalDate settlementDate;

    private Double settlementPay;

    private String settlementPayFull;

    private Double settlementEarn;

    private Double budgetBasic;

    private Double budgetDoor;

    private Double budgetFurniture;

    private Double settlementBasic;

    private Double settlementDoor;

    private Double settlementFurniture;

    private Double settlementCabinet;

    private Double settlementMaterial;

    private String discountItem;

    private String saleActive;

    private String financeRemark;

    private LocalDate submitFileDate;

    private LocalDate submitReportDate;

    private LocalDate fetchRoyaltyDate;

    private String businessLevel;

}
