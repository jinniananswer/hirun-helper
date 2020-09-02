package com.microtomato.hirun.modules.college.knowhow.entity.dto;

import lombok.*;

/**
 * @author huanghua
 * @date 2020-08-16
 */
@Data
@NoArgsConstructor
public class QuestionServiceDTO {

    private Long questionId;

    private Long employeeId;

    private Long respondent;

    private Long approvedId;

    private String questionTitle;

    private String questionContent;

    private String questionType;

    private String approvedTag;

    private String replyContent;
}
