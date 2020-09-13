package com.microtomato.hirun.modules.college.config.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class CollegeFixedExercisesRequestDTO {

    private String examId;

    private String exercisesType;

    private Integer exercisesNumber;

    private List<CollegeFixedExercisesTaskDTO> studyChaptersList;

}
