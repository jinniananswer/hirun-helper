package com.microtomato.hirun.modules.bss.service.entity.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.microtomato.hirun.framework.config.JsonLocalDateTimeDeserializer;
import com.microtomato.hirun.framework.config.JsonLocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDate;

/**
 * @program: hirun-helper
 * @description: 投诉信息查询条件传输对象
 * @author: liuhui
 * @create: 2020-04-05 23:54
 **/
@Data
public class QueryComplainCondDTO {

    private String custName;

    private String complainType;

    private String complainWay;

    private String complainTimeType;

    private LocalDate startTime;

    private LocalDate endTime;

    private Long houseId;

    private Long customerServiceEmployeeId;

    private Long designEmployeeId;

    private String orderStatus;

    private Long orgId;

    private String wxNick;

    private String busiStatus;
}
