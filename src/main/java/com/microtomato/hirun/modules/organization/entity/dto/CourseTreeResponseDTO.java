package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class CourseTreeResponseDTO {

    private String studyName;

    private Long studyId;

    private boolean courseFlag;

    private List<CourseTreeResponseDTO> children;
}
