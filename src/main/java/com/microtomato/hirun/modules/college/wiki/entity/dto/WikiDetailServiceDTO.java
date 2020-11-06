package com.microtomato.hirun.modules.college.wiki.entity.dto;

import com.microtomato.hirun.modules.college.wiki.entity.po.CollegeWiki;
import com.microtomato.hirun.modules.college.wiki.entity.po.CollegeWikiReply;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author huanghua
 * @date 2020-10-28
 */
@Data
@NoArgsConstructor
public class WikiDetailServiceDTO {

    private CollegeWiki wiki;

    private String wikiTypeName;

    private String authorName;

    private List<WikiReplyServiceDTO> replyInfos;
}
