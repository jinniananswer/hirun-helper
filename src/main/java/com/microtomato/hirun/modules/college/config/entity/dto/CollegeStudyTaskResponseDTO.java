package com.microtomato.hirun.modules.college.config.entity.dto;

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
     * 课程名称
     */
    private String studyName;

    /**
     * 课程类型
     */
    private String studyType;

    /**
     * 课程类型名称
     */
    private String studyTypeName;

    /**
     * 课程学习顺序
     */
    private String studyOrder;

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
}
