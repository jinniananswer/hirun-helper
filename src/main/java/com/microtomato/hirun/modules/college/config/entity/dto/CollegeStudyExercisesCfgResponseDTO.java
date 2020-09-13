package com.microtomato.hirun.modules.college.config.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class CollegeStudyExercisesCfgResponseDTO {

    private Long studyExercisesId;

    /**
     * 学习标识
     */
    private String studyId;

    /**
     * 章节ID
     */
    private String chaptersId;

    /**
     * 考试标识
     */
    private String examId;

    /**
     * 考试范围名称
     */
    private String examName;

    /**
     * 习题类型
     */
    private String exercisesType;

    /**
     * 习题名称
     */
    private String exercisesTypeName;

    /**
     * 习题数量
     */
    private Integer exercisesNumber;

    /**
     * 状态
     */
    private String status;
}
