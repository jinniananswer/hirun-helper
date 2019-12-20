package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 我的培训档案数据传输对象
 * @author: jinnian
 * @create: 2019-12-17 17:56
 **/
@Data
public class TrainDTO {

    private Long trainId;

    private Long employeeId;

    private String name;

    private List<String> courseName;

    private String type;

    private String typeName;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean pass;

    private List<TrainScoreDTO> scores;

}
