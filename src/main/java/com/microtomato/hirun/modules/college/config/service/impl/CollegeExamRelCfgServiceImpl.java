package com.microtomato.hirun.modules.college.config.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeExamRelCfg;
import com.microtomato.hirun.modules.college.config.mapper.CollegeExamRelCfgMapper;
import com.microtomato.hirun.modules.college.config.service.ICollegeExamRelCfgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (CollegeExamRelCfg)表服务实现类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-18 01:12:54
 */
@Service("collegeExamRelCfgService")
public class CollegeExamRelCfgServiceImpl extends ServiceImpl<CollegeExamRelCfgMapper, CollegeExamRelCfg> implements ICollegeExamRelCfgService {

    @Autowired
    private CollegeExamRelCfgMapper collegeExamRelCfgMapper;


}