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
 * @description: 客户投诉传输对象
 * @author: liuhui
 * @create: 2020-07-12 14:24
 **/
@Data
public class ComplainOrderDTO {


    private Long id;

    private Long orderId;

    private String complainNo;

    private Long customerId;

    private String complainType;

    private String complainTypeName;

    private String complainWay;

    private String complainWayName;

    private String complainContent;

    private String dealResult;

    private String followTrace;

    private String acceptEmployeeName;

    private Long acceptEmployeeId;

    @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
    @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
    private LocalDateTime complainTime;

    private LocalDate dealDate;


    private String remark;

    private String status;

    private String statusName;

    private String custName;

    private String custNo;

    private String mobileNo;

    private Long houseId;

    private String houseName;

    private String agentName;

    private String designName;

}
