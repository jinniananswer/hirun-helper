package com.microtomato.hirun.modules.college.config.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyExamCfg;
import lombok.Data;

import java.util.List;

@Data
public class CollegeStudyExamCfgResponseDTO {
    @TableId(value = "study_exam_id", type = IdType.AUTO)
    private Long studyExamId;

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
     * 考试题目类型
     */
    private String exercisesType;

    /**
     * 考试题目类型名称
     */
    private String exercisesTypeName;

    /**
     * 考试题目数量
     */
    private Integer exercisesNumber;

    /**
     * 状态
     */
    private String status;
}
