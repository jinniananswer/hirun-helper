package com.microtomato.hirun.modules.college.knowhow.entity.dto;

import lombok.Data;

/**
 * @author huanghua
 * @date 2020-08-16
 */
@Data
public class AddQuestionServiceDTO {

    private String employeeId;

    private String respondent;

    private String approvedId;

    private String questionTittle;

    private String questionContent;

    private String questionType;

}
