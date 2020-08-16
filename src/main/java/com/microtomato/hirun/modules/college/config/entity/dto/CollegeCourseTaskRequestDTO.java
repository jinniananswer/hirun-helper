package com.microtomato.hirun.modules.college.config.entity.dto;

import lombok.Data;

@Data
public class CollegeCourseTaskRequestDTO {

    private Long taskType;

    private Long courseId;

    private String courseName;

    private Long courseTaskId;

    private Integer page;

    private Integer limit;

    private Integer count;
}
