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
 * @description: 员工保修传输对象
 * @author: liuhui
 * @create: 2020-07-12 14:24
 **/
@Data
public class GuaranteeDTO {


    private Long id;

    private Long orderId;

    private String customerEmployeeName;

    private String designEmployeeName;

    private String projectEmployeeName;

    private String checkTime;

    private String hydropowerName;

    private String inlayName;

    private String paintName;

    @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
    @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    private LocalDate guaranteeStartDate;

    private LocalDate guaranteeEndDate;

}
