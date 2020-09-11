package com.microtomato.hirun.modules.college.task.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * (CollegeEmployeeTask)表实体类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-11 02:11:33
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("college_employee_task")
public class CollegeEmployeeTask extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "task_id", type = IdType.AUTO)
    private Long taskId;

    /**
     * 员工ID
     */
    @TableField(value = "employee_id")
    private String employeeId;

    /**
     * 任务类型：1-固定任务，2-活动任务
     */
    @TableField(value = "task_type")
    private String taskType;

    /**
     * 课程类别
     */
    @TableField(value = "course_type")
    private String courseType;

    /**
     * 课程ID
     */
    @TableField(value = "course_id")
    private String courseId;

    /**
     * 章节ID
     */
    @TableField(value = "chapter_id")
    private String chapterId;

    /**
     * 状态
     */
    @TableField(value = "status")
    private String status;

    /**
     * 学习开始时间
     */
    @TableField(value = "study_start_date")
    private LocalDateTime studyStartDate;

    /**
     * 学习结束时间
     */
    @TableField(value = "study_end_date")
    private LocalDateTime studyEndDate;

    /**
     * 学习完成时间
     */
    @TableField(value = "study_complete_date")
    private LocalDateTime studyCompleteDate;

    /**
     * 日完成量
     */
    @TableField(value = "daily_completion")
    private String dailyCompletion;

    /**
     * 评分
     */
    @TableField(value = "score")
    private Integer score;

    /**
     * 习题次数
     */
    @TableField(value = "exercises_number")
    private Integer exercisesNumber;

    /**
     * 习题完成次数
     */
    @TableField(value = "exercises_completed_number")
    private Integer exercisesCompletedNumber;

    /**
     * 考试合格分数
     */
    @TableField(value = "pass_score")
    private Integer passScore;

    /**
     * 考试分数
     */
    @TableField(value = "exam_score")
    private Integer examScore;

}