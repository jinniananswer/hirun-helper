package com.microtomato.hirun.modules.college.config.entity.dto;

import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyExercisesCfg;
import lombok.Data;

import java.util.List;

@Data
public class CollegeStudyExercisesResponseDTO {
    private String studyId;

    private String studyName;

    private String studyType;

    private String studyTypeName;

    private String taskType;

    private String taskTypeName;

    private String chaptersId;

    private String chaptersName;

    private Integer exercisesNumber;

    private Integer passScore;

    private List<CollegeStudyExercisesCfgResponseDTO> collegeStudyExercisesList;
}
