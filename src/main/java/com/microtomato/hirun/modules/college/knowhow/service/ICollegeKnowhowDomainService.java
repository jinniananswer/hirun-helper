package com.microtomato.hirun.modules.college.knowhow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.college.knowhow.entity.dto.QuestionServiceDTO;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeQuestion;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeQuestionRela;

import java.util.List;

/**
 * @author huanghua
 * @date 2020-08-16
 */
public interface ICollegeKnowhowDomainService {

    void addQuestion(QuestionServiceDTO request);

    IPage<CollegeQuestion> queryByEmployeeIdAndRelaType(Long employeeId, String relationType, Page<CollegeQuestionRela> page);

    IPage<CollegeQuestion> querySelfQuestion(String questionText, String sortType, Long employeeId, String relationType, String optionTag, Page<CollegeQuestionRela> page, String questionType);

    IPage<CollegeQuestion> queryAllQuestion(Page<CollegeQuestion> page);
}
