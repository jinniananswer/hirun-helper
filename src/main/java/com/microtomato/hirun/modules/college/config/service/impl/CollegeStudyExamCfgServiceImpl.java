package com.microtomato.hirun.modules.college.config.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyExamCfg;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyExercisesCfg;
import com.microtomato.hirun.modules.college.config.mapper.CollegeStudyExamCfgMapper;
import com.microtomato.hirun.modules.college.config.service.ICollegeStudyExamCfgService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (CollegeStudyExamCfg)表服务实现类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-11 00:30:30
 */
@Service("collegeStudyExamCfgService")
@DataSource(DataSourceKey.SYS)
public class CollegeStudyExamCfgServiceImpl extends ServiceImpl<CollegeStudyExamCfgMapper, CollegeStudyExamCfg> implements ICollegeStudyExamCfgService {

    @Autowired
    private CollegeStudyExamCfgMapper collegeStudyExamCfgMapper;


    @Override
    public List<CollegeStudyExamCfg> queryEffectiveByStudyIdAndChaptersId(String studyId, String chaptersId) {
        return this.list(Wrappers.<CollegeStudyExamCfg>lambdaQuery().eq(CollegeStudyExamCfg::getStudyId, studyId)
                .eq(StringUtils.isNotEmpty(chaptersId), CollegeStudyExamCfg::getChaptersId, chaptersId)
                .eq(CollegeStudyExamCfg::getStatus, '0'));
    }
}