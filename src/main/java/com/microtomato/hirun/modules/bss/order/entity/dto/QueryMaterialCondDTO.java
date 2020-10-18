package com.microtomato.hirun.modules.bss.order.entity.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.microtomato.hirun.framework.config.JsonLocalDateTimeDeserializer;
import com.microtomato.hirun.framework.config.JsonLocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDate;

/**
 * @program: hirun-helper
 * @description: 主材查询条件传输对象
 * @author: liuhui
 * @create: 2020-02-05 23:54
 **/
@Data
public class QueryMaterialCondDTO {

    private String custName;

    private Long designEmployeeId;

    private Long projectEmployeeId;

    private Long mainMaterialEmployeeId;

    private String brandCode;

    private String timeType;

    @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
    @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
    private LocalDate startTime;

    @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
    @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
    private LocalDate endTime;

    private Integer page;

    private Integer size;

    private Integer total;

    private Long houseId;

    private Long agentEmployeeId;
    /**
     * 区分主材与橱柜类型
     */
    private String type;
}
