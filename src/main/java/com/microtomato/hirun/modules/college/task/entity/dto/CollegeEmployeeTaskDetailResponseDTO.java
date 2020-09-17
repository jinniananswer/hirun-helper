package com.microtomato.hirun.modules.college.task.entity.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CollegeEmployeeTaskDetailResponseDTO {

    private String employeeId;

    private String studyTaskId;

    private String taskName;

    private String studyId;

    private String studyName;

    private String chapterId;

    private String chapterName;

    private String taskType;

    private String taskTypeName;

    private String studyType;

    private String studyTypeName;

    private LocalDateTime studyStartDate;

    private LocalDateTime studyEndDate;

    private LocalDateTime studyCompleteDate;

    private Integer exercisesCompletedNumber;

    private Integer exercisesNumber;

    private Integer examScore;

    private Integer passScore;

    private Integer score;
}
