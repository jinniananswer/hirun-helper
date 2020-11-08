package com.microtomato.hirun.modules.college.task.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * (CollegeStudyTaskScore)表实体类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-22 01:03:46
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("college_study_task_score")
public class CollegeStudyTaskScore extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "study_task_score_id", type = IdType.AUTO)
    private Long studyTaskScoreId;
    /**
     * 员工任务标识
     */
    @TableId(value = "task_id", type = IdType.AUTO)
    private String taskId;

    /**
     * 学习任务标识
     */
    @TableField(value = "study_task_id")
    private String studyTaskId;

    /**
     * 任务难度评分
     */
    @TableField(value = "task_difficulty_score")
    private Integer taskDifficultyScore;

    /**
     * 老师评分
     */
    @TableField(value = "tutor_score")
    private Integer tutorScore;

}