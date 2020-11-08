package com.microtomato.hirun.modules.college.task.entity.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CollegeTaskCommentDetailResponseDTO {

    private String employeeId;

    private String employeeName;

    private String studyTaskId;

    private String taskName;

    private Long taskId;

    private String taskDesc;
}
