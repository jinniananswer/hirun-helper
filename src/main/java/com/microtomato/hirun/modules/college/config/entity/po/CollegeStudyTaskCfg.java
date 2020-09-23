package com.microtomato.hirun.modules.college.config.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * (CollegeStudyTaskCfg)表实体类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-23 01:00:57
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("college_study_task_cfg")
public class CollegeStudyTaskCfg extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "study_task_id", type = IdType.AUTO)
    private Long studyTaskId;

    /**
     * 任务名称
     */
    @TableField(value = "task_name")
    private String taskName;

    /**
     * 学习内容标识
     */
    @TableField(value = "study_id")
    private String studyId;

    /**
     * 学习内容类型 0-课程，1-课件
     */
    @TableField(value = "study_type")
    private String studyType;

    /**
     * 任务开始方式
     */
    @TableField(value = "study_start_type")
    private String studyStartType;

    /**
     * 指定天数后开始
     */
    @TableField(value = "appoint_day")
    private Integer appointDay;

    /**
     * 学习模式
     */
    @TableField(value = "study_model")
    private String studyModel;

    /**
     * 同时学习的任务ID
     */
    @TableField(value = "together_study_task_id")
    private String togetherStudyTaskId;

    /**
     * 状态
     */
    @TableField(value = "status")
    private String status;

    /**
     * 员工工作类型
     */
    @TableField(value = "job_type")
    private String jobType;

    /**
     * 任务类型:1-固定任务，2-活动任务
     */
    @TableField(value = "task_type")
    private String taskType;

    /**
     * 学习时间/天
     */
    @TableField(value = "study_time")
    private String studyTime;

    /**
     * 内容学习时长/小时
     */
    @TableField(value = "study_length")
    private Integer studyLength;

    /**
     * 习题次数
     */
    @TableField(value = "exercises_number")
    private Integer exercisesNumber;

    /**
     * 考试合格分数
     */
    @TableField(value = "pass_score")
    private Integer passScore;

    /**
     * 任务发布时间
     */
    @TableField(value = "task_release_date")
    private LocalDateTime taskReleaseDate;

    /**
     * 发布状态 0-未发布 1-已发布
     */
    @TableField(value = "release_status")
    private String releaseStatus;

    /**
     * 任务有效期/天
     */
    @TableField(value = "task_validity_term")
    private Integer taskValidityTerm;

    /**
     * 实践任务描述
     */
    @TableField(value = "task_desc")
    private String taskDesc;

}