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
 * @date 2020-10-22 01:31:29
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
     * 学习任务标识
     */
    @TableField(value = "study_task_id")
    private String studyTaskId;

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
     * 习题完成次数
     */
    @TableField(value = "exercises_completed_number")
    private Integer exercisesCompletedNumber;

    /**
     * 考试分数
     */
    @TableField(value = "exam_score")
    private Integer examScore;

    /**
     * 任务学习时长
     */
    @TableField(value = "study_date_length")
    private Integer studyDateLength;

    /**
     * 任务完成时间
     */
    @TableField(value = "task_complete_date")
    private LocalDateTime taskCompleteDate;

}