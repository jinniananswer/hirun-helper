package com.microtomato.hirun.modules.college.task.entity.dto;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CollegeEmployeeTaskDetailResponseDTO {

    private String employeeId;

    private String studyTaskId;

    private String taskName;

    private Long taskId;

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

    private Integer studyDateLength;

    private String taskDesc;

    private String taskRemainderTime;

    private Double taskProgress;

    /**
     * 答题任务类型 详情请见STATIC表ANSWER_TASK_TYPE
     */
    private String answerTaskType;
}
