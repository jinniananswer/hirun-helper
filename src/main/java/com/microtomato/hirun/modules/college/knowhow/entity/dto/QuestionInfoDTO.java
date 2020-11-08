package com.microtomato.hirun.modules.college.knowhow.entity.dto;

import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeQuestion;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author huanghua
 * @date 2020-08-16
 */
@Data
@NoArgsConstructor
public class QuestionInfoDTO {

    private String questionType;

    private String questionTypeName;

    private List<CollegeQuestion> questionList;
}
