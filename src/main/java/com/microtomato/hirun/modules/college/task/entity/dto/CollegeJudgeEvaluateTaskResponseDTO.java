package com.microtomato.hirun.modules.college.task.entity.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CollegeJudgeEvaluateTaskResponseDTO {

    private boolean isEvaluateTask;

    private String scoreNotReasons;
}
