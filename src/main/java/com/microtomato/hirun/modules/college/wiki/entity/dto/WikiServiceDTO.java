package com.microtomato.hirun.modules.college.wiki.entity.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author huanghua
 * @date 2020-10-28
 */
@Data
@NoArgsConstructor
public class WikiServiceDTO {
    private Long wikiId;

    private String wikiTitle;

    private String wikiContent;

    private LocalDateTime createTime;

    private String status;

    private Long clicks;

    private Long thumbsUp;

    private String wikiType;

}
