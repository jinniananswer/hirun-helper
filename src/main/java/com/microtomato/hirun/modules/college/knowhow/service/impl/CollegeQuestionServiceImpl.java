package com.microtomato.hirun.modules.college.knowhow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.college.knowhow.consts.KnowhowConsts;
import com.microtomato.hirun.modules.college.knowhow.mapper.CollegeQuestionMapper;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeQuestion;
import com.microtomato.hirun.modules.college.knowhow.service.ICollegeQuestionService;
import io.lettuce.core.dynamic.annotation.Key;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

}