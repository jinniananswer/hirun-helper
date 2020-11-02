package com.microtomato.hirun.modules.college.task.entity.dto;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CollegeTaskCommentInfoResponseDTO {

    private String studyTaskId;

    private String studyId;

    private String taskName;

    private String employeeId;

    private String employeeName;

    private Long taskId;

    private Boolean isDelayFlag;

    private String taskDesc;

    private LocalDateTime studyStartDate;

    private LocalDateTime studyEndDate;

    private LocalDateTime taskCompleteDate;

    private Integer experienceScore;

    private Integer imgScore;

    private String experience;

    private List<String> fileList;
}
