package com.microtomato.hirun.modules.bss.service.entity.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.microtomato.hirun.framework.config.JsonLocalDateTimeDeserializer;
import com.microtomato.hirun.framework.config.JsonLocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 员工维修传输对象
 * @author: liuhui
 * @create: 2020-07-21 14:24
 **/
@Data
public class RepairOrderInfoDTO {


    List<RepairOrderDTO> insertRecords;

    List<RepairOrderDTO> removeRecords;

    List<RepairOrderDTO> updateRecords;

    Long customerId;

    Long orderId;

    String repairNo;
}
