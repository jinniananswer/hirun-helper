package com.microtomato.hirun.modules.college.config.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeCourseChaptersCfg;
import lombok.Data;

import java.util.List;

@Data
public class CollegeStudyTaskResponseDTO {

    private Long studyTaskId;

    /**
     * 课程ID
     */
    private String studyId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 课程类型
     */
    private String studyType;

    /**
     * 课程类型名称
     */
    private String studyTypeName;

    /**
     * 学习模式
     */
    private String studyModel;

    /**
     * 学习模式名称
     */
    private String studyModelName;

    /**
     * 同时学习的任务ID
     */
    private String togetherStudyTaskId;

    /**
     * 同时学习的任务名称
     */
    private String togetherStudyTaskName;

    /**
     * 任务开始方式
     */
    private String studyStartType;

    /**
     * 任务开始方式名称
     */
    private String studyStartTypeName;

    /**
     * 状态
     */
    private String status;

    /**
     * 员工工作类型标识
     */
    private String jobType;

    /**
     * 员工工作类型名称
     */
    private String jobTypeName;

    /**
     * 任务类型:1-固定任务，2-活动任务
     */
    private String taskType;

    /**
     * 任务类型名称
     */
    private String taskTypeName;

    /**
     * 学习时间/天
     */
    private String studyTime;

    /**
     * 内容学习时长
     */
    private Integer studyLength;

    /**
     * 习题次数
     */
    private Integer exercisesNumber;

    /**
     * 考试合格分数
     */
    private Integer passScore;

    /**
     * 章节集合
     */
    private List<CollegeCourseChaptersTaskResponseDTO> collegeCourseChaptersList;

    /**
     * 员工岗位集合
     */
    List<String> jobRoleInfos;

    /**
     * 任务有效期
     */
    private Integer taskValidityTerm;

    /**
     * 任务描述
     */
    private String taskDesc;

    /**
     * 任务发布状态
     */
    private String releaseStatus;

    /**
     * 任务发布状态名称
     */
    private String releaseStatusName;

    /**
     * 任务分配总数量
     */
    private Integer taskAllNum;

    /**
     * 任务分配有效数量
     */
    private Integer taskEffectiveNum;

    /**
     * 任务分配完成数量
     */
    private Integer taskFinishNum;

    /**
     * 任务分配延期数量
     */
    private Integer taskDelayNum;

    /**
     * 任务分配涉及员工数量
     */
    private Integer taskEmployeeNum;

    /**
     * 任务难度平均分
     */
    private Double argTaskDifficultyScore;

    /**
     * 老师平均分
     */
    private Double argTutorScore;

    /**
     * 任务分配有效百分比
     */
    private Integer taskEffective;

    /**
     * 任务分配完成百分比
     */
    private Integer taskFinish;

    /**
     * 任务分配延期百分比
     */
    private Integer taskDelay;

    /**
     * 任务难度排名
     */
    private Integer taskDifficultyScoreRanking;

    /**
     * 老师评分排名
     */
    private Integer tutorScoreRanking;

    /**
     * 任务难度百分比
     */
    private String taskDifficultyScoreCxceedPercentage;

    /**
     * 老师评分百分比
     */
    private String tutorScoreCxceedPercentage;
}
