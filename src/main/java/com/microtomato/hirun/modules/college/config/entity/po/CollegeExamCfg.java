package com.microtomato.hirun.modules.college.config.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * (CollegeExamCfg)表实体类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-22 04:19:12
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("college_exam_cfg")
public class CollegeExamCfg extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 题目练习标识
     */
    @TableId(value = "exam_topic_id", type = IdType.AUTO)
    private Long examTopicId;

    /**
     * 学习任务标识
     */
    @TableField(value = "study_task_id")
    private String studyTaskId;

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
     * 考试最大次数
     */
    @TableField(value = "exam_max_num")
    private Integer examMaxNum;

    /**
     * 考试合格分数
     */
    @TableField(value = "pass_score")
    private Integer passScore;

    /**
     * 考试类型：0-练习，1-考试
     */
    @TableField(value = "exam_type")
    private String examType;

    /**
     * 状态
     */
    @TableField(value = "status")
    private String status;

    /**
     * 考试时间
     */
    @TableField(value = "exam_time")
    private Integer examTime;

}