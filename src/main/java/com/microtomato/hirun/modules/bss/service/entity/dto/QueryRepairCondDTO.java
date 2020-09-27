package com.microtomato.hirun.modules.bss.service.entity.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * @program: hirun-helper
 * @description: 维修信息查询条件传输对象
 * @author: liuhui
 * @create: 2020-04-05 23:54
 **/
@Data
public class QueryRepairCondDTO {

    private String custName;

    private String isFee;

    private String repairWorkerType;

    private String repairWorker;

    private LocalDate startTime;

    private LocalDate endTime;

    private Long houseId;

    private Long customerServiceEmployeeId;

    private String repairTimeType;
}
