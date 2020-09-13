package com.microtomato.hirun.modules.college.config.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeCourseChaptersCfg;
import lombok.Data;

import java.util.List;

@Data
public class CollegeCourseChaptersTaskResponseDTO {

    private Long chaptersId;

    /**
     * 学习标识
     */
    private String studyId;

    /**
     * 章节名称
     */
    private String chaptersName;

    /**
     * 章节学习顺序
     */
    private String chaptersStudyOrder;

    /**
     * 章节类型
     */
    private String chaptersType;

    /**
     * 章节类型名称
     */
    private String chaptersTypeName;

    /**
     * 状态
     */
    private String status;

    /**
     * 学习时间/天
     */
    private String studyTime;

    /**
     * 学习模式：0-分批学习，1-同时学习
     */
    private String studyModel;

    /**
     * 学习模式名称
     */
    private String studyModelName;

    /**
     * 习题次数
     */
    private Integer exercisesNumber;

    /**
     * 考试合格分数
     */
    private Integer passScore;
}
