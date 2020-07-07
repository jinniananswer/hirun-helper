package com.microtomato.hirun.modules.bss.order.entity.dto.fee;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 设计费用查询结果数据对象
 * @author: jinnian
 * @create: 2020-07-04 15:11
 **/
@Data
public class DesignFeeDTO {

    private Long orderId;

    private Long custId;

    private String custNo;

    private String custName;

    private String decorateAddress;

    private String orderStateName;

    private String houseLayoutName;

    private String indoorArea;

    private Long designStandard;

    private String designStyle;

    private Long feeNo;

    private Long depositFee;

    private LocalDateTime feeTime;

    private String remark;

    private LocalDateTime signTime;

    private LocalDateTime firstPayTime;

    private String shopName;

    private String depositFinanceName;

    private String designFeeFinanceName;

    private String agentName;

    private String designerName;

    private String counselorName;

    private String cabinetName;

    private String materialName;

    private String reportName;

    private String contractTime;
}
