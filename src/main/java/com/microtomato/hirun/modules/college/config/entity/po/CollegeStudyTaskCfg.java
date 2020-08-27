package com.microtomato.hirun.modules.college.config.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * (CollegeStudyTaskCfg)表实体类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-08-28 01:14:50
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
     * 学习内容标识
     */
    @TableField(value = "study_id")
    private String studyId;

    /**
     * 学习内容名称
     */
    @TableField(value = "study_name")
    private String studyName;

    /**
     * 学习内容类型 0-课程，1-课件
     */
    @TableField(value = "study_type")
    private String studyType;

    /**
     * 学习顺序
     */
    @TableField(value = "study_order")
    private String studyOrder;

    /**
     * 状态
     */
    @TableField(value = "status")
    private String status;

    /**
     * 员工职级
     */
    @TableField(value = "staff_rank")
    private String staffRank;

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
     * 习题次数
     */
    @TableField(value = "exercises_number")
    private Integer exercisesNumber;

    /**
     * 考试合格分数
     */
    @TableField(value = "pass_score")
    private Integer passScore;

}