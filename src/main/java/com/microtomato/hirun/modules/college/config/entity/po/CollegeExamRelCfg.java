package com.microtomato.hirun.modules.college.config.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * (CollegeExamRelCfg)表实体类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-18 01:12:54
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("college_exam_rel_cfg")
public class CollegeExamRelCfg extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 考题关系标识
     */
    @TableId(value = "exercises_rel_id", type = IdType.AUTO)
    private Long exercisesRelId;

    /**
     * 学习标识
     */
    @TableField(value = "study_exercises_id")
    private String studyExercisesId;

    /**
     * 题目类型
     */
    @TableField(value = "topic_type")
    private String topicType;

    /**
     * 题目数量
     */
    @TableField(value = "topic_num")
    private Integer topicNum;

    /**
     * 状态
     */
    @TableField(value = "status")
    private String status;

}