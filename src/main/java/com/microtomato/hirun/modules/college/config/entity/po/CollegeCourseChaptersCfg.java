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
 * @date 2020-08-13 00:26:58
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
     * 课程ID
     */
    @TableField(value = "course_id")
    private String courseId;

    /**
     * 章节名称
     */
    @TableField(value = "chapters_name")
    private String chaptersName;

    /**
     * 章节学习顺序
     */
    @TableField(value = "chapters_study_order")
    private String chaptersStudyOrder;

    /**
     * 章节类型
     */
    @TableField(value = "chapters_type")
    private String chaptersType;

    /**
     * 章节学习顺序
     */
    @TableField(value = "status")
    private String status;

    /**
     * 学习时间/天
     */
    @TableField(value = "study_time")
    private String studyTime;

    /**
     * 学习模式：0-分批学习，1-同时学习
     */
    @TableField(value = "study_model")
    private String studyModel;

}