package com.microtomato.hirun.modules.college.knowhow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.college.knowhow.mapper.CollegeQuestionMapper;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeQuestion;
import com.microtomato.hirun.modules.college.knowhow.service.ICollegeQuestionService;
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
    

}