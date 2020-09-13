package com.microtomato.hirun.modules.college.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyExamCfg;

import java.util.List;

/**
 * (CollegeStudyExamCfg)表服务接口
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-11 00:30:30
 */
public interface ICollegeStudyExamCfgService extends IService<CollegeStudyExamCfg> {

    List<CollegeStudyExamCfg> queryEffectiveByStudyIdAndChaptersId(String studyId, String chaptersId);
}