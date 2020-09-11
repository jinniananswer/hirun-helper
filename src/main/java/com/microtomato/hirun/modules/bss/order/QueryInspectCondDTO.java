package com.microtomato.hirun.modules.bss.order;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.microtomato.hirun.framework.config.JsonLocalDateTimeDeserializer;
import com.microtomato.hirun.framework.config.JsonLocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDate;

/**
 * @program: hirun-helper
 * @description: 检测查询信息查询条件传输对象
 * @author: liuhui
 * @create: 2020-02-05 23:54
 **/
@Data
public class QueryInspectCondDTO {

    private String custName;

    private Long designEmployeeId;

    private String checkStatus;

    private String receiveStatus;

    private String settleStatus;

    private String timeType;

    @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
    @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
    private LocalDate startTime;

    @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
    @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
    private LocalDate endTime;

    private String institution;

    private Integer page;

    private Integer size;

    private Integer total;

    private Long houseId;

    private Long agentEmployeeId;

}
