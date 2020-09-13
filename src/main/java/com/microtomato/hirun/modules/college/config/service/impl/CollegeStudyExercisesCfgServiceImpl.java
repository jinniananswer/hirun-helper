package com.microtomato.hirun.modules.college.config.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyExercisesCfg;
import com.microtomato.hirun.modules.college.config.mapper.CollegeStudyExercisesCfgMapper;
import com.microtomato.hirun.modules.college.config.service.ICollegeStudyExercisesCfgService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (CollegeStudyExercisesCfg)表服务实现类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-04 00:41:33
 */
@Service(value = "collegeStudyExercisesCfgService")
@DataSource(DataSourceKey.SYS)
public class CollegeStudyExercisesCfgServiceImpl extends ServiceImpl<CollegeStudyExercisesCfgMapper, CollegeStudyExercisesCfg> implements ICollegeStudyExercisesCfgService {

    @Autowired
    private CollegeStudyExercisesCfgMapper collegeStudyExercisesCfgMapper;


    @Override
    public List<CollegeStudyExercisesCfg> queryEffectiveByStudyIdAndChaptersId(String studyId, String chaptersId) {
        return this.list(Wrappers.<CollegeStudyExercisesCfg>lambdaQuery().eq(CollegeStudyExercisesCfg::getStudyId, studyId)
                .eq(StringUtils.isNotEmpty(chaptersId), CollegeStudyExercisesCfg::getChaptersId, chaptersId)
                .eq(CollegeStudyExercisesCfg::getStatus, '0'));
    }
}