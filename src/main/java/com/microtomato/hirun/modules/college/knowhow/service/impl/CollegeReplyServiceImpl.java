package com.microtomato.hirun.modules.college.knowhow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.college.knowhow.consts.KnowhowConsts;
import com.microtomato.hirun.modules.college.knowhow.mapper.CollegeReplyMapper;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeReply;
import com.microtomato.hirun.modules.college.knowhow.service.ICollegeReplyService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * (CollegeReply)表服务实现类
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-08-16 16:10:18
 */
@Service("collegeReplyService")
public class CollegeReplyServiceImpl extends ServiceImpl<CollegeReplyMapper, CollegeReply> implements ICollegeReplyService {

    @Autowired
    private CollegeReplyMapper collegeReplyMapper;

    @Override
    public CollegeReply queryReplyByQuestionId(Long questionId) {
        CollegeReply reply = this.getOne(new QueryWrapper<CollegeReply>().lambda()
                .eq(CollegeReply::getQuestionId, questionId)
                .eq(CollegeReply::getStatus, KnowhowConsts.NORMAL_STATUS_VALID));
        return reply;
    }

    @Override
    public void insertReply(Long questionId, String replyContent, Long respondent) {
        this.save(CollegeReply.builder()
                .questionId(questionId)
                .replyContent(replyContent)
                .respondent(respondent)
                .status(KnowhowConsts.NORMAL_STATUS_VALID)
                .replyTime(TimeUtils.getCurrentLocalDateTime()).build());
    }
}