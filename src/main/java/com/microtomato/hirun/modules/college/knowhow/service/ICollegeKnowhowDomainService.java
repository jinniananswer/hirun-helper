package com.microtomato.hirun.modules.college.knowhow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.college.knowhow.entity.dto.AddQuestionServiceDTO;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeQuestion;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeQuestionRela;

import java.util.List;

/**
 * @author huanghua
 * @date 2020-08-16
 */
public interface ICollegeKnowhowDomainService {

    void addQuestion(AddQuestionServiceDTO request);

    IPage<CollegeQuestion> queryByEmployeeIdAndRelaType(String employeeId, String relationType, Page<CollegeQuestionRela> page);
}
