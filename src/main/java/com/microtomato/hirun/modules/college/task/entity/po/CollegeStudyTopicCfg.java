package com.microtomato.hirun.modules.college.task.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.microtomato.hirun.framework.data.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * (CollegeStudyTopicCfg)表实体类
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-09-16 01:59:25
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("college_study_topic_cfg")
public class CollegeStudyTopicCfg extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "study_topic_id", type = IdType.AUTO)
    private Long studyTopicId;

    /** 学习内容与题目关系名称 */
    @TableField(value = "study_topic_name")
    private String studyTopicName;

    /** 学习标识 */
    @TableField(value = "study_id")
    private String studyId;

    /** 章节ID */
    @TableField(value = "chapters_id")
    private String chaptersId;

    /** 状态 */
    @TableField(value = "status")
    private String status;

    /** 学习与题目关系描述 */
    @TableField(value = "study_topic_desc")
    private String studyTopicDesc;

}