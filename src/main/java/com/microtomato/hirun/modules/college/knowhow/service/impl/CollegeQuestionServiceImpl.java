package com.microtomato.hirun.modules.college.knowhow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.college.knowhow.consts.KnowhowConsts;
import com.microtomato.hirun.modules.college.knowhow.mapper.CollegeQuestionMapper;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeQuestion;
import com.microtomato.hirun.modules.college.knowhow.service.ICollegeQuestionService;
import io.lettuce.core.dynamic.annotation.Key;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

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
    public CollegeQuestion getValidById(String questionId) {
        return this.getOne(new QueryWrapper<CollegeQuestion>().lambda()
                .eq(CollegeQuestion::getQuestionId, questionId)
                .eq(CollegeQuestion::getStatus, KnowhowConsts.NORMAL_STATUS_VALID));
    }
}