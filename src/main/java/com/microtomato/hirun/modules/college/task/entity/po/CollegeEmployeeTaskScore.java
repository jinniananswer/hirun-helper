package com.microtomato.hirun.modules.college.task.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * (CollegeEmployeeTaskScore)表实体类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-30 01:44:47
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("college_employee_task_score")
public class CollegeEmployeeTaskScore extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "task_score_id", type = IdType.AUTO)
    private Long taskScoreId;

    /**
     * 员工任务标识
     */
    @TableField(value = "task_id")
    private String taskId;

    /**
     * 任务学习评分
     */
    @TableField(value = "study_score")
    private Integer studyScore;

    /**
     * 任务练习评分
     */
    @TableField(value = "exercises_score")
    private Integer exercisesScore;

    /**
     * 任务考试评分
     */
    @TableField(value = "exam_score")
    private Integer examScore;

    /**
     * 心得评分
     */
    @TableField(value = "experience_score")
    private Integer experienceScore;

    /**
     * 照片评分
     */
    @TableField(value = "img_score")
    private Integer imgScore;

}