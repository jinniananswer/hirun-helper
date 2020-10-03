package com.microtomato.hirun.modules.bss.service.entity.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.microtomato.hirun.framework.config.JsonLocalDateTimeDeserializer;
import com.microtomato.hirun.framework.config.JsonLocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 员工维修传输对象
 * @author: liuhui
 * @create: 2020-07-12 14:24
 **/
@Data
public class RepairOrderDTO {


    private Long id;

    private Long orderId;

    private String repairNo;

    private Long customerId;

    private String repairWorkerType;

    private String repairWorkerTypeName;

    private String isFee;

    private String isFeeName;

    private String repairItem;

    private String repairReason;

    private String dutyShare;

    private String repairMaterial;

    private String buildHeadman;

    private String repairWorkerCount;

    private String repairWorker;

    @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
    @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
    private LocalDateTime acceptTime;


    private LocalDate offerTime;


    private LocalDate receiptTime;


    private LocalDate planRepairDate;

    private LocalDate actualRepairStartDate;

    private LocalDate actualRepairEndDate;

    private String repairVisitContent;

    private LocalDate repairVisitTime;

    private String repairSatisfaction;

    private Integer repairFee;

    private Integer repairAwardFee;

    private Integer repairMaterialFee;

    private Integer repairWorkerSalary;

    private LocalDate actualWorkerSalaryDate;

    private String remark;

    private String status;

    private String statusName;

    private String custName;

    private String mobileNo;

    private Long houseId;

    private String houseName;

    private String custNo;

    private String agentName;

}
