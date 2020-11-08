package com.microtomato.hirun.modules.college.config.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * (CollegeCourseChaptersCfg)表实体类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-16 23:05:27
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("college_course_chapters_cfg")
public class CollegeCourseChaptersCfg extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "chapters_id", type = IdType.AUTO)
    private Long chaptersId;

    /**
     * 学习标识
     */
    @TableField(value = "study_id")
    private String studyId;

    /**
     * 章节名称
     */
    @TableField(value = "chapters_name")
    private String chaptersName;

    /**
     * 章节类型
     */
    @TableField(value = "chapters_type")
    private String chaptersType;

    /**
     * 状态
     */
    @TableField(value = "status")
    private String status;

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