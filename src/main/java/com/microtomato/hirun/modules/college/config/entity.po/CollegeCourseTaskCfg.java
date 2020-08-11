package com.microtomato.hirun.modules.college.config.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * (CollegeCourseTaskCfg)表实体类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-08-09 12:41:24
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("college_course_task_cfg")
public class CollegeCourseTaskCfg extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "course_task_id", type = IdType.AUTO)
    private Long courseTaskId;

    /**
     * 课程ID
     */
    @TableField(value = "course_id")
    private String courseId;

    /**
     * 课程名称
     */
    @TableField(value = "course_name")
    private String courseName;

    /**
     * 课程类型
     */
    @TableField(value = "course_type")
    private String courseType;

    /**
     * 课程学习顺序
     */
    @TableField(value = "course_study_order")
    private String courseStudyOrder;

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

}