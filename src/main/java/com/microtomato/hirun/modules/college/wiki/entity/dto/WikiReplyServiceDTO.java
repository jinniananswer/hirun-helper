package com.microtomato.hirun.modules.college.wiki.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author huanghua
 * @date 2020-10-28
 */
@Data
@NoArgsConstructor
public class WikiReplyServiceDTO {

    private Long replyId;

    private Long wikiId;

    private String replyContent;

    private Long respondent;

    private LocalDateTime replyTime;

    private String status;

    private Long thumbsUp;

    private String replyer;
}
