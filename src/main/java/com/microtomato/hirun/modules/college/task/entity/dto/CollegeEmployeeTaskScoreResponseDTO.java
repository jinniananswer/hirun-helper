package com.microtomato.hirun.modules.college.task.entity.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CollegeEmployeeTaskScoreResponseDTO {

    /**
     * 评分类型名称
     */
    private String name;

    /**
     * 评分分数
     */
    private Integer score;
}
