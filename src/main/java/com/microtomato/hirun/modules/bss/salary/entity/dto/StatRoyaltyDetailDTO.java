package com.microtomato.hirun.modules.bss.salary.entity.dto;

import com.microtomato.hirun.modules.bss.order.entity.dto.UsualOrderWorkerDTO;
import lombok.Data;

import java.time.LocalDate;

/**
 * @program: hirun-helper
 * @description: 统计提成数据的DTO
 * @author: jinnian
 * @create: 2020-07-15 23:30
 **/
@Data
public class StatRoyaltyDetailDTO {

    private Long orderId;

    private String type;

    private String custNo;

    private String custName;

    private String decorateAddress;

    private String royaltyStatus;

    private LocalDate royaltyStatusDate;

    private LocalDate designPayDate;

    private Double designFee;

    private Double designContractAward;

    private Double designRoyalty;

    private String designRate;

    private Double assistantRoyalty;

    private String assistantRate;

    private Double produceRoyalty;

    private String produceRate;

    private Double contractFee;

    private Double basicFee;

    private Double doorFee;

    private Double furnitureFee;

    private String periods;

    private String payState;

    private LocalDate payDate;

    private Double contractAward;

    private Double budgetRoyalty;

    private Double customerGroupAward;

    private UsualOrderWorkerDTO usualWorker;

    private String reportStatusName;

    private Double counselorRoyalty;

    private String counselorRate;

    private Double areaManagerRoyalty;

    private String areaManagerRate;

    private Double agentRoyalty;

    private String agentRate;

    private Double designerRoyalty;

    private String designerRate;

    private Double projectManagerRoyalty;

    private String projectManagerRate;

    private Double projectChargeRoyalty;

    private String projectChargeRate;

    private Double otherRoyalty;

    private String otherRate;

    private Long shopId;

    private String shopName;

}
