package com.microtomato.hirun.modules.college.task.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * (CollegeStudyTopicRel)表实体类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-20 02:30:42
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("college_study_topic_rel")
public class CollegeStudyTopicRel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "study_topic_rel_id", type = IdType.AUTO)
    private Long studyTopicRelId;

    /**
     * 学习内容与题目关系名称
     */
    @TableField(value = "study_topic_name")
    private String studyTopicName;

    /**
     * 标签标识
     */
    @TableField(value = "label_id")
    private Long labelId;

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
     * 状态
     */
    @TableField(value = "status")
    private String status;

    /**
     * 学习与题目关系描述
     */
    @TableField(value = "study_topic_desc")
    private String studyTopicDesc;

}