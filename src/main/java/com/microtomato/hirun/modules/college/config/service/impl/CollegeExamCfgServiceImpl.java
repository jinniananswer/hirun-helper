package com.microtomato.hirun.modules.college.config.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeExamCfg;
import com.microtomato.hirun.modules.college.config.mapper.CollegeExamCfgMapper;
import com.microtomato.hirun.modules.college.config.service.ICollegeExamCfgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (CollegeExamCfg)表服务实现类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-18 01:12:19
 */
@Service("collegeExamCfgService")
public class CollegeExamCfgServiceImpl extends ServiceImpl<CollegeExamCfgMapper, CollegeExamCfg> implements ICollegeExamCfgService {

    @Autowired
    private CollegeExamCfgMapper collegeExamCfgMapper;


}