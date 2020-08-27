package com.microtomato.hirun.modules.college.task.entity.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CollegeEmployeeTaskDTO {

    private String taskType;

    private String studyType;

    private String studyId;

    private String chapterId;

    private String status;

    private LocalDateTime studyStartDate;

    private LocalDateTime studyEndDate;
}
