package com.microtomato.hirun.modules.college.topic.entity.po;

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
 * (ExamTopic)表实体类
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-09-04 11:39:06
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("ins_exam_topic")
public class ExamTopic extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "topic_id", type = IdType.AUTO)
    private Long topicId;


    @TableField(value = "exam_id")
    private Long examId;


    @TableField(value = "name")
    private String name;


    @TableField(value = "type")
    private String type;


    @TableField(value = "correct_answer")
    private String correctAnswer;


    @TableField(value = "score")
    private Integer score;


    @TableField(value = "status")
    private String status;


    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;


    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}