package com.microtomato.hirun.modules.college.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeExamRelCfg;
import com.microtomato.hirun.modules.college.config.mapper.CollegeExamRelCfgMapper;
import com.microtomato.hirun.modules.college.config.service.ICollegeExamRelCfgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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


    @Override
    public List<CollegeExamRelCfg> queryExamRelInfo(Long examTopicId) {
        return this.list(new QueryWrapper<CollegeExamRelCfg>().lambda()
                .eq(CollegeExamRelCfg::getExamTopicId, examTopicId)
                .eq(CollegeExamRelCfg::getStatus, "0"));
    }
}