package com.microtomato.hirun.modules.college.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeStudyTopicRel;

import java.util.List;

/**
 * (CollegeStudyTopicRel)表服务接口
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-20 02:17:16
 */
public interface ICollegeStudyTopicRelService extends IService<CollegeStudyTopicRel> {
    List<CollegeStudyTopicRel> queryEffectiveByStudyAndChaptersId(String studyId, String chaptersId);

    List<CollegeStudyTopicRel> getEffectiveByStudyId(String studyId);
}