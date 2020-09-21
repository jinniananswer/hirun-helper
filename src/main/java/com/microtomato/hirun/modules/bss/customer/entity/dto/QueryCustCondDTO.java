package com.microtomato.hirun.modules.bss.customer.entity.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.microtomato.hirun.framework.config.JsonLocalDateTimeDeserializer;
import com.microtomato.hirun.framework.config.JsonLocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDate;

/**
 * @program: hirun-helper
 * @description: 客户信息查询条件传输对象
 * @author: liuhui
 * @create: 2020-04-05 23:54
 **/
@Data
public class QueryCustCondDTO {

    private String custName;

    private Long counselorEmployeeId;

    private Long customerServiceEmployeeId;

    private String timeType;

    @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
    @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
    private LocalDate startTime;

    @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
    @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
    private LocalDate endTime;

    private String houseMode;

    private Integer page;

    private Integer size;

    private Integer total;

    private Long houseId;

    private String orderStatus;

    private Long orgId;

    private String wxNick;

    private String busiStatus;
}
