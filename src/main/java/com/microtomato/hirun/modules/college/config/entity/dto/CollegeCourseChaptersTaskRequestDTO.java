package com.microtomato.hirun.modules.college.config.entity.dto;

import com.microtomato.hirun.modules.college.config.entity.po.CollegeCourseChaptersCfg;
import lombok.Data;

import java.util.List;

@Data
public class CollegeCourseChaptersTaskRequestDTO {

    private String taskType;

    private String studyId;

    private String studyName;

    private String taskName;

    private String studyType;

    private String studyOrder;
    /**
     * 学习模式
     */
    private String studyModel;

    /**
     * 同时学习的任务ID
     */
    private String togetherStudyTaskId;

    /**
     * 任务开始方式
     */
    private String studyStartType;

    /**
     * 指定天数后开始
     */
    private Integer appointDay;

    private String studyTime;

    private Integer exercisesNumber;

    private Integer passScore;

    private String jobType;

    private List<CollegeCourseChaptersCfg> courseChaptersList;

    /**
     * 员工岗位集合
     */
    List<String> jobRoleInfos;
}
