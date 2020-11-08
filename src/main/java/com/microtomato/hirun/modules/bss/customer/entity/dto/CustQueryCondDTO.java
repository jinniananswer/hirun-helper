package com.microtomato.hirun.modules.bss.customer.entity.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.microtomato.hirun.framework.config.JsonLocalDateTimeDeserializer;
import com.microtomato.hirun.framework.config.JsonLocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 客户信息查询条件传输对象
 * @author: liuhui
 * @create: 2020-02-05 23:54
 **/
@Data
public class CustQueryCondDTO {

    private String custName;

    private Long designEmployeeId;

    private Long counselorEmployeeId;

    private Long reportEmployeeId;

    private String informationSource;

    private String customerStatus;

    private String customerType;

    private String timeType;

    private LocalDate startTime;

    private LocalDate endTime;

    private String houseMode;

    private Integer page;

    private Integer size;

    private Integer total;

    private Long houseId;

    private String orderStatus;

    private String prepareStatus;

    private Long agentEmployeeId;

    private Long shopId;

    private String employeeIds;

    private String orgLine;
}
