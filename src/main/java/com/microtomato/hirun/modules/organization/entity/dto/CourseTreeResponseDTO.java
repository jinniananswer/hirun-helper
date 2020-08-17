package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class CourseTreeResponseDTO {

    private String name;

    private Long courseId;

    private String type;

    private boolean courseFlag;

    private List<CourseTreeResponseDTO> children;
}
