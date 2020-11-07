package com.microtomato.hirun.modules.college.knowhow.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author huanghua
 * @date 2020-08-16
 */
@Data
@NoArgsConstructor
public class CollegeQuestionServiceDTO {

    private Long questionId;

    private Long employeeId;

    private Long respondent;

    private Long approvedId;

    private String questionTitle;

    private String questionContent;

    private String questionType;

    private String approvedTag;

    private String cancelTag;

    private String questioner;

    private LocalDateTime createTime;

    private List<ReplyServiceDTO> replyInfos;
}
