package com.microtomato.hirun.modules.college.config.entity.dto;

import com.microtomato.hirun.modules.college.config.entity.po.CollegeCourseChaptersCfg;
import lombok.Data;

import java.util.List;

@Data
public class CollegeCourseTaskResponseDTO {

    private Long courseTaskId;

    /**
     * 课程ID
     */
    private String courseId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程类型
     */
    private String courseType;

    /**
     * 课程学习顺序
     */
    private String courseStudyOrder;

    /**
     * 状态
     */
    private String status;

    /**
     * 员工职级
     */
    private String staffRank;

    /**
     * 任务类型:1-固定任务，2-活动任务
     */
    private String taskType;

    /**
     * 学习时间/天
     */
    private String studyTime;

    /**
     * 章节集合
     */
    private List<CollegeCourseChaptersCfg> collegeCourseChaptersList;
}
