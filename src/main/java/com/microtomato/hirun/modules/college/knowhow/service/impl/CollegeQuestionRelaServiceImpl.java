package com.microtomato.hirun.modules.college.knowhow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.college.knowhow.mapper.CollegeQuestionRelaMapper;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeQuestionRela;
import com.microtomato.hirun.modules.college.knowhow.service.ICollegeQuestionRelaService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * (CollegeQuestionRela)表服务实现类
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-08-16 16:14:44
 */
@Service("collegeQuestionRelaService")
public class CollegeQuestionRelaServiceImpl extends ServiceImpl<CollegeQuestionRelaMapper, CollegeQuestionRela> implements ICollegeQuestionRelaService {

    @Autowired
    private CollegeQuestionRelaMapper collegeQuestionRelaMapper;
    

}