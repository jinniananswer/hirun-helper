package com.microtomato.hirun.modules.college.knowhow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.knowhow.entity.dto.CollegeQuestionServiceDTO;
import com.microtomato.hirun.modules.college.knowhow.entity.dto.QuestionInfoDTO;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeQuestion;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * (CollegeQuestion)表服务接口
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-08-16 16:14:44
 */
public interface ICollegeQuestionService extends IService<CollegeQuestion> {

    CollegeQuestion getValidById(Long questionId);

    List<CollegeQuestion> queryByQuestionIds(List<Long> questionIds);

    void updateByIds(List<Long> questionIds);

    void updateApprovedTagByIds(List<Long> questionIds, String approvedTag);

    void updateReplyQuestion(Long questionId);

    void updatePublishQuestionByIds(List<Long> questionIds);

    void updateClicksById(String questionId);

    void thumbsUpById(Long valueOf, String cancelTag);

    List<QuestionInfoDTO> queryQuestionByName(String name);

    List<QuestionInfoDTO> queryLoginQuestion();

    List<QuestionInfoDTO> queryQuestionByQuestionType(String questionType);

    CollegeQuestionServiceDTO getQuestionById(Long questionId);
}