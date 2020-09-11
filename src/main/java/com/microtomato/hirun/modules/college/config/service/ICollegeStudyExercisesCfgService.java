package com.microtomato.hirun.modules.college.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyExercisesCfg;

import java.util.List;

/**
 * (CollegeStudyExercisesCfg)表服务接口
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-04 00:41:33
 */
public interface ICollegeStudyExercisesCfgService extends IService<CollegeStudyExercisesCfg> {

    List<CollegeStudyExercisesCfg> queryEffectiveByStudyIdAndChaptersId(String studyId, String chaptersId);
}