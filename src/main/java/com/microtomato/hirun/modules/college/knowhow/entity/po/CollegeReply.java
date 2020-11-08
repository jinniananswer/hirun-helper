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
 * (CollegeReply)表实体类
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-08-16 16:10:04
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("college_reply")
public class CollegeReply extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "reply_id", type = IdType.AUTO)
    private Long replyId;

    /** 问题ID */
    @TableField(value = "question_id")
    private Long questionId;

    /** 回复内容 */
    @TableField(value = "reply_content")
    private String replyContent;

    /** 解答人标识 */
    @TableField(value = "respondent")
    private Long respondent;

    /** 回复时间 */
    @TableField(value = "reply_time")
    private LocalDateTime replyTime;

    /** 状态 */
    @TableField(value = "status")
    private String status;

    /**
     * 点赞数
     */
    @TableField(value = "thumbs_up")
    private Long thumbsUp;
}