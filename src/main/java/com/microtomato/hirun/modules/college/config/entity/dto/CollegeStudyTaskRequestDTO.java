package com.microtomato.hirun.modules.college.config.entity.dto;

import lombok.Data;

@Data
public class CollegeStudyTaskRequestDTO {

    private Long taskType;

    private Long studyId;

    private String studyName;

    private Long studyTaskId;

    private Integer page;

    private Integer limit;

    private Integer count;
}
