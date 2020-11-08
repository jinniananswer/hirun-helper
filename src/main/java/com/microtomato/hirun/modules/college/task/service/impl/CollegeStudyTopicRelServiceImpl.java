package com.microtomato.hirun.modules.college.task.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeStudyTopicRel;
import com.microtomato.hirun.modules.college.task.mapper.CollegeStudyTopicRelMapper;
import com.microtomato.hirun.modules.college.task.service.ICollegeStudyTopicRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (CollegeStudyTopicRel)表服务实现类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-20 02:17:16
 */
@Service("collegeStudyTopicRelService")
@DataSource(DataSourceKey.INS)
public class CollegeStudyTopicRelServiceImpl extends ServiceImpl<CollegeStudyTopicRelMapper, CollegeStudyTopicRel> implements ICollegeStudyTopicRelService {

    @Autowired
    private CollegeStudyTopicRelMapper collegeStudyTopicRelMapper;

    @Override
    public List<CollegeStudyTopicRel> queryEffectiveByStudyAndChaptersId(String studyId, String chaptersId) {
        return this.list(Wrappers.<CollegeStudyTopicRel>lambdaQuery().eq(CollegeStudyTopicRel::getStudyId, studyId)
                .eq(CollegeStudyTopicRel::getChaptersId, chaptersId)
                .eq(CollegeStudyTopicRel::getStatus, "0"));
    }

    @Override
    public List<CollegeStudyTopicRel> getEffectiveByStudyId(String studyId) {
        return this.list(Wrappers.<CollegeStudyTopicRel>lambdaQuery().eq(CollegeStudyTopicRel::getStudyId, studyId)
                .eq(CollegeStudyTopicRel::getStatus, "0"));
    }

}