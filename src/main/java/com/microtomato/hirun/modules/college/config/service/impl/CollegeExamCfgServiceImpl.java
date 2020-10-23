package com.microtomato.hirun.modules.college.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
 * @date 2020-10-24 04:21:53
 */
@Service("collegeExamCfgService")
public class CollegeExamCfgServiceImpl extends ServiceImpl<CollegeExamCfgMapper, CollegeExamCfg> implements ICollegeExamCfgService {

    @Autowired
    private CollegeExamCfgMapper collegeExamCfgMapper;

    @Override
    public CollegeExamCfg getByStudyTaskId(Long studyTaskId) {
        return this.getOne(new QueryWrapper<CollegeExamCfg>().lambda()
                .eq(CollegeExamCfg::getStudyTaskId, studyTaskId)
                .eq(CollegeExamCfg::getStatus, "0"));
    }
    @Override
    public CollegeExamCfg getByStudyTaskIdAndExamType(String studyTaskId, String examTyp) {
        return this.getOne(Wrappers.<CollegeExamCfg>lambdaQuery().eq(CollegeExamCfg::getStudyTaskId, studyTaskId).eq(CollegeExamCfg::getExamType, examTyp).eq(CollegeExamCfg::getStatus, "0"));
    }

    @Override
    public boolean updateByIds(CollegeExamCfg collegeExamCfg) {
        return this.update(collegeExamCfg, Wrappers.<CollegeExamCfg>lambdaUpdate()
                .eq(CollegeExamCfg::getStudyTaskId, collegeExamCfg.getStudyTaskId())
                .eq(CollegeExamCfg::getExamTopicId, collegeExamCfg.getExamTopicId())
                .eq(CollegeExamCfg::getExamType, collegeExamCfg.getExamType()));
    }
}