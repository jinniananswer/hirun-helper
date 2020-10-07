package com.microtomato.hirun.modules.college.task.entity.dto;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CollegeEmployTaskInfoResponseDTO {

    private String studyTaskId;

    private String studyId;

    private String taskName;

    private Boolean isDelayFlag;

    private String taskDesc;

    private String studyType;

    private LocalDateTime studyStartDate;

    private LocalDateTime studyEndDate;

    private Boolean isExerciseFlag;

    private Boolean isExamFlag;

    private Double taskProgress;

    private Double taskTimeProgress;

    private LocalDateTime sysdate;

    private Boolean isSelectTutorFlag;

    private String selectTutor;

    private List<CollegeTaskStudyContentResponseDTO> taskStudyContentList;
}
