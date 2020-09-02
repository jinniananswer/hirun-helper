package com.microtomato.hirun.modules.college.knowhow.entity.po;

import java.time.LocalDateTime;

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
 * (CollegeQuestion)表实体类
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-08-16 16:14:44
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("college_question")
public class CollegeQuestion extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "question_id", type = IdType.INPUT)
    private Long questionId;

    /**
     * 问题标题
     */
    @TableField(value = "question_title")
    private String questionTitle;

    /**
     * 问题内容
     */
    @TableField(value = "question_content")
    private String questionContent;

    /**
     * 问题类型
     */
    @TableField(value = "question_type")
    private String questionType;

    /**
     * 点击量
     */
    @TableField(value = "clicks")
    private Long clicks;

    /**
     * 状态
     * 0:已失效（审批未通过）
     * 1:未审批
     * 2:已审批未回答
     * 3:已回答
     */
    @TableField(value = "status")
    private String status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}