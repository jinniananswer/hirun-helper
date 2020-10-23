package com.microtomato.hirun.modules.college.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeExamCfg;

/**
 * (CollegeExamCfg)表服务接口
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-18 01:12:19
 */
public interface ICollegeExamCfgService extends IService<CollegeExamCfg> {

    CollegeExamCfg getByStudyTaskId(Long studyTaskId);
}