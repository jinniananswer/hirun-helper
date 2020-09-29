package com.microtomato.hirun.modules.college.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.task.entity.dto.CollegeTaskExperienceScoreResponseDTO;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeTaskExperience;

/**
 * (CollegeTaskExperience)表服务接口
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-27 03:36:42
 */
public interface ICollegeTaskExperienceService extends IService<CollegeTaskExperience> {

    public CollegeTaskExperienceScoreResponseDTO queryByTaskId(String taskId);
}