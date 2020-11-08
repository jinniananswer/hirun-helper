package com.microtomato.hirun.modules.college.wiki.entity.po;

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
 * (CollegeWikiReply)表实体类
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-11-05 13:13:52
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("college_wiki_reply")
public class CollegeWikiReply extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "reply_id", type = IdType.AUTO)
    private Long replyId;


    @TableField(value = "wiki_id")
    private Long wikiId;


    @TableField(value = "reply_content")
    private String replyContent;


    @TableField(value = "respondent")
    private Long respondent;


    @TableField(value = "reply_time")
    private LocalDateTime replyTime;


    @TableField(value = "status")
    private String status;


    @TableField(value = "thumbs_up")
    private Long thumbsUp;

}