package com.microtomato.hirun.modules.college.knowhow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.UserContextUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.college.knowhow.consts.KnowhowConsts;
import com.microtomato.hirun.modules.college.knowhow.entity.dto.CollegeQuestionServiceDTO;
import com.microtomato.hirun.modules.college.knowhow.entity.dto.QuestionInfoDTO;
import com.microtomato.hirun.modules.college.knowhow.entity.dto.ReplyServiceDTO;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeQuestionRela;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeReply;
import com.microtomato.hirun.modules.college.knowhow.mapper.CollegeQuestionMapper;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeQuestion;
import com.microtomato.hirun.modules.college.knowhow.service.ICollegeQuestionRelaService;
import com.microtomato.hirun.modules.college.knowhow.service.ICollegeQuestionService;
import com.microtomato.hirun.modules.college.knowhow.service.ICollegeReplyService;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import io.lettuce.core.dynamic.annotation.Key;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

/**
 * (CollegeQuestion)表服务实现类
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-08-16 16:14:44
 */
@Service("collegeQuestionService")
public class CollegeQuestionServiceImpl extends ServiceImpl<CollegeQuestionMapper, CollegeQuestion> implements ICollegeQuestionService {

    @Autowired
    private CollegeQuestionMapper collegeQuestionMapper;

    @Autowired
    private IStaticDataService staticDataServiceImpl;

    @Autowired
    private ICollegeQuestionRelaService collegeQuestionRelaServiceImpl;

    @Autowired
    private ICollegeReplyService collegeReplyService;

    @Autowired
    private IEmployeeService employeeService;

    @Override
    public CollegeQuestion getValidById(Long questionId) {
        return this.getOne(new QueryWrapper<CollegeQuestion>().lambda()
                .eq(CollegeQuestion::getQuestionId, questionId)
                .ne(CollegeQuestion::getStatus, KnowhowConsts.QUESTION_STATUS_INVALID));
    }

    @Override
    public List<CollegeQuestion> queryByQuestionIds(List<Long> questionIds) {
        return this.list(new QueryWrapper<CollegeQuestion>().lambda()
                .in(CollegeQuestion::getQuestionId, questionIds)
                .ne(CollegeQuestion::getStatus, KnowhowConsts.QUESTION_STATUS_INVALID));
    }

    @Override
    public void updateByIds(List<Long> questionIds) {
        this.update(new UpdateWrapper<CollegeQuestion>().lambda()
                .in(CollegeQuestion::getQuestionId, questionIds)
                .set(CollegeQuestion::getStatus, KnowhowConsts.QUESTION_STATUS_INVALID));
    }

    @Override
    public void updateApprovedTagByIds(List<Long> questionIds, String approvedTag) {
        this.update(new UpdateWrapper<CollegeQuestion>().lambda()
                .in(CollegeQuestion::getQuestionId, questionIds)
                .set(StringUtils.equals(approvedTag, KnowhowConsts.QUESTION_VERIFY_APPROVE_FAILED),
                        CollegeQuestion::getStatus, KnowhowConsts.QUESTION_STATUS_APPROVE_FAILED)
                .set(StringUtils.equals(approvedTag, KnowhowConsts.QUESTION_VERIFY_APPROVED),
                        CollegeQuestion::getStatus, KnowhowConsts.QUESTION_STATUS_UNREPLY));
    }

    @Override
    public void updateReplyQuestion(Long questionId) {
        this.update(new UpdateWrapper<CollegeQuestion>().lambda()
                .eq(CollegeQuestion::getQuestionId, questionId)
                .set(CollegeQuestion::getStatus, KnowhowConsts.QUESTION_STATUS_REPLY));
    }

    @Override
    public void updatePublishQuestionByIds(List<Long> questionIds) {
        this.update(new UpdateWrapper<CollegeQuestion>().lambda()
                .in(CollegeQuestion::getQuestionId, questionIds)
                .set(CollegeQuestion::getStatus, KnowhowConsts.QUESTION_STATUS_DEPLOYED));
    }

    @Override
    public void updateClicksById(String questionId) {
        this.update(new UpdateWrapper<CollegeQuestion>().lambda()
                .eq(CollegeQuestion::getQuestionId, questionId).setSql("clicks = clicks + 1"));
    }

    @Override
    public void thumbsUpById(Long questionId, String cancelTag) {
        this.update(new UpdateWrapper<CollegeQuestion>().lambda()
                .eq(CollegeQuestion::getQuestionId, questionId)
                .setSql(StringUtils.equals("0", cancelTag), "thumbs_up = thumbs_up + 1")
                .setSql(StringUtils.equals("1", cancelTag), "thumbs_up = thumbs_up - 1"));
    }

    @Override
    public List<QuestionInfoDTO> queryQuestionByName(String name) {
        List<QuestionInfoDTO> result = new ArrayList<>();
        List<CollegeQuestion> list = this.list(Wrappers.<CollegeQuestion>lambdaQuery()
                .eq(CollegeQuestion::getStatus, "4"));
        if (ArrayUtils.isEmpty(list)) {
            return result;
        }

        if (StringUtils.isNotBlank(name)) {
            list = list.stream().filter(x ->
                    x.getQuestionTitle().contains(name)
                            || x.getQuestionContent().contains(name))
                    .collect(Collectors.toList());
        }

        Map<String, List<CollegeQuestion>> questionMap = new HashMap<>();
        if (ArrayUtils.isNotEmpty(list)){
            for (CollegeQuestion collegeQuestion : list) {
                String questionType = collegeQuestion.getQuestionType();
                List<CollegeQuestion> collegeQuestionList = new ArrayList<>();
                if (questionMap.containsKey(questionType)){
                    collegeQuestionList = questionMap.get(questionType);
                }
                collegeQuestionList.add(collegeQuestion);
                questionMap.put(questionType, collegeQuestionList);
            }
        }
        if (null != questionMap && questionMap.size() > 0){
            for (String questionType : questionMap.keySet()){
                String questionTypeName = staticDataServiceImpl.getCodeName("QUESTION_TYPE", questionType);
                if (StringUtils.isEmpty(questionTypeName)){
                    questionTypeName = questionType;
                }
                QuestionInfoDTO questionInfoDTO = new QuestionInfoDTO();
                questionInfoDTO.setQuestionType(questionType);
                questionInfoDTO.setQuestionTypeName(questionTypeName);
                questionInfoDTO.setQuestionList(questionMap.get(questionType));
                result.add(questionInfoDTO);
            }
        }
        return result;
    }

    @Override
    public List<QuestionInfoDTO> queryLoginQuestion(String name) {
        List<QuestionInfoDTO> result = new ArrayList<>();
        UserContext userContext = WebContextUtils.getUserContext();
        if (userContext == null) {
            userContext = UserContextUtils.getUserContext();
        }
        Long employeeId = userContext.getEmployeeId();
        List<CollegeQuestionRela> collegeQuestionRelas = this.collegeQuestionRelaServiceImpl.queryByEmployeeIdAndRelaType(employeeId, "1");
        if (ArrayUtils.isNotEmpty(collegeQuestionRelas)){
            List<Long> questionIdList = new ArrayList<>();
            for (CollegeQuestionRela collegeQuestionRela : collegeQuestionRelas) {
                Long questionId = collegeQuestionRela.getQuestionId();
                questionIdList.add(questionId);
            }
            if (ArrayUtils.isNotEmpty(questionIdList)){
                List<CollegeQuestion> list = this.list(Wrappers.<CollegeQuestion>lambdaQuery().in(CollegeQuestion::getQuestionId, questionIdList));
                if (StringUtils.isNotBlank(name)) {
                    list = list.stream().filter(x -> x.getQuestionTitle().contains(name)
                            || x.getQuestionContent().contains(name)).collect(Collectors.toList());
                }

                Map<String, List<CollegeQuestion>> questionMap = new HashMap<>();
                if (ArrayUtils.isNotEmpty(list)){
                    for (CollegeQuestion collegeQuestion : list) {
                        String questionType = collegeQuestion.getQuestionType();
                        List<CollegeQuestion> collegeQuestionList = new ArrayList<>();
                        if (questionMap.containsKey(questionType)){
                            collegeQuestionList = questionMap.get(questionType);
                        }
                        collegeQuestionList.add(collegeQuestion);
                        questionMap.put(questionType, collegeQuestionList);
                    }
                }
                if (null != questionMap && questionMap.size() > 0){
                    for (String questionType : questionMap.keySet()){
                        String questionTypeName = staticDataServiceImpl.getCodeName("QUESTION_TYPE", questionType);
                        if (StringUtils.isEmpty(questionTypeName)){
                            questionTypeName = questionType;
                        }
                        QuestionInfoDTO questionInfoDTO = new QuestionInfoDTO();
                        questionInfoDTO.setQuestionType(questionType);
                        questionInfoDTO.setQuestionTypeName(questionTypeName);
                        questionInfoDTO.setQuestionList(questionMap.get(questionType));
                        result.add(questionInfoDTO);
                    }
                }
            }
        }

        return result;
    }

    @Override
    public List<QuestionInfoDTO> queryQuestionByQuestionType(String questionType) {
        List<QuestionInfoDTO> result = new ArrayList<>();
        List<CollegeQuestion> list = this.list(Wrappers.<CollegeQuestion>lambdaQuery()
                .eq(CollegeQuestion::getQuestionType, questionType));
        String questionTypeName = staticDataServiceImpl.getCodeName("QUESTION_TYPE", questionType);
        if (StringUtils.isEmpty(questionTypeName)){
            questionTypeName = questionType;
        }
        QuestionInfoDTO questionInfoDTO = new QuestionInfoDTO();
        questionInfoDTO.setQuestionType(questionType);
        questionInfoDTO.setQuestionTypeName(questionTypeName);
        questionInfoDTO.setQuestionList(list);
        result.add(questionInfoDTO);
        return result;
    }

    @Override
    public CollegeQuestionServiceDTO getQuestionById(Long questionId) {
        CollegeQuestionServiceDTO questionService = new CollegeQuestionServiceDTO();
        CollegeQuestion question = this.getValidById(questionId);
        if (null == question || null == question.getQuestionId()) {
            return questionService;
        }
        BeanUtils.copyProperties(question, questionService);
        List<ReplyServiceDTO> replyInfos = new ArrayList<>();
        List<CollegeReply> replys = collegeReplyService.queryReplysByQuestionId(questionId);
        if (ArrayUtils.isNotEmpty(replys)) {
            replys.stream().forEach(x -> {
                ReplyServiceDTO reply = new ReplyServiceDTO();
                BeanUtils.copyProperties(x, reply);
                reply.setReplyer(employeeService.getEmployeeNameEmployeeId(x.getRespondent()));
                replyInfos.add(reply);
            });
        }

        questionService.setReplyInfos(replyInfos);
        Long questionerId = collegeQuestionRelaServiceImpl.getEmployeeByQuestionId(questionId);
        if (Objects.nonNull(questionerId)) {
            questionService.setQuestioner(employeeService.getEmployeeNameEmployeeId(questionerId));
        }

        return questionService;
    }

}