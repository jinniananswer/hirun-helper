package com.microtomato.hirun.modules.college.task.entity.dto;


import lombok.Data;

@Data
public class CollegeEmployeeTaskScoreRequestDTO {

    private String taskId;

    private Integer taskScore;

    private Integer studyScore;

    private Integer exercisesScore;

    private Integer examScore;
}
