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
 * (CollegeQuestionRela)表实体类
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
@TableName("college_question_rela")
public class CollegeQuestionRela extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "relation_id", type = IdType.AUTO)
    private Long relationId;

    /** 员工ID */
    @TableField(value = "employee_id")
    private Long employeeId;

    /** 问题ID */
    @TableField(value = "question_id")
    private Long questionId;

    /** 关系类型 */
    @TableField(value = "relation_type")
    private String relationType;

    /** 状态 */
    @TableField(value = "status")
    private String status;

}