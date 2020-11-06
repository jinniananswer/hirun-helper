package com.microtomato.hirun.modules.college.wiki.entity.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.microtomato.hirun.modules.college.wiki.entity.po.CollegeWiki;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author huanghua
 * @date 2020-10-28
 */
@Data
@NoArgsConstructor
public class WikiServiceDTO {

    private String wikiType;

    private String wikiTypeName;

    private List<CollegeWiki> wikiList;
}
