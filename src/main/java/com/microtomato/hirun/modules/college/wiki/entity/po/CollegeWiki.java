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
 * (CollegeWiki)表实体类
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-11-05 13:14:24
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("college_wiki")
public class CollegeWiki extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "wiki_id", type = IdType.AUTO)
    private Long wikiId;


    @TableField(value = "wiki_title")
    private String wikiTitle;


    @TableField(value = "wiki_content")
    private String wikiContent;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(value = "status")
    private String status;


    @TableField(value = "clicks")
    private Long clicks;


    @TableField(value = "wiki_type")
    private String wikiType;


    @TableField(value = "thumbs_up")
    private Long thumbsUp;


    @TableField(value = "author")
    private Long author;

}