package com.microtomato.hirun.modules.college.task.entity.dto;


import com.baomidou.mybatisplus.annotation.TableField;
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

    /**
     * 习题次数
     */
    private Integer exercisesNumber;

    /**
     * 考试合格分数
     */
    private Integer passScore;
}
