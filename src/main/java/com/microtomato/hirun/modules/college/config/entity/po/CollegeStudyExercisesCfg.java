package com.microtomato.hirun.modules.college.config.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * (CollegeStudyExercisesCfg)表实体类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-04 01:04:12
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("college_study_exercises_cfg")
public class CollegeStudyExercisesCfg extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "study_exercises_id", type = IdType.AUTO)
    private Long studyExercisesId;

    /**
     * 学习标识
     */
    @TableField(value = "study_id")
    private String studyId;

    /**
     * 章节ID
     */
    @TableField(value = "chapters_id")
    private String chaptersId;

    /**
     * 考试标识
     */
    @TableField(value = "exam_id")
    private String examId;

    /**
     * 习题类型
     */
    @TableField(value = "exercises_type")
    private String exercisesType;

    /**
     * 习题数量
     */
    @TableField(value = "exercises_number")
    private Integer exercisesNumber;

    /**
     * 状态
     */
    @TableField(value = "status")
    private String status;

}