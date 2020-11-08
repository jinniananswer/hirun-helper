package com.microtomato.hirun.modules.college.knowhow.entity.po;

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
 * (CollegeQuestionReplyerCfg)表实体类
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-11-07 13:29:05
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("college_question_replyer_cfg")
public class CollegeQuestionReplyerCfg extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "rel_id", type = IdType.AUTO)
    private Long relId;


    @TableField(value = "question_type")
    private String questionType;


    @TableField(value = "teacher_id")
    private Long teacherId;


    @TableField(value = "status")
    private String status;

}