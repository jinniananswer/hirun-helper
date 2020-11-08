package com.microtomato.hirun.modules.college.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeReleaseTaskExamRequestDTO;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeExamCfg;

/**
 * (CollegeExamCfg)表服务接口
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-24 04:21:53
 */
public interface ICollegeExamCfgService extends IService<CollegeExamCfg> {

    CollegeExamCfg getByStudyTaskId(Long studyTaskId);

    CollegeExamCfg getByStudyTaskIdAndExamType(String studyTaskId, String examType);

    boolean updateByIds(CollegeExamCfg collegeExamCfg);

    void releaseTaskExam(CollegeReleaseTaskExamRequestDTO collegeReleaseTaskExamRequestDTO);
}