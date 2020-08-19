package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class CourseTreeResponseDTO {

    private String courseName;

    private Long courseId;

    private String courseType;

    private boolean courseFlag;

    private List<CourseTreeResponseDTO> children;
}
