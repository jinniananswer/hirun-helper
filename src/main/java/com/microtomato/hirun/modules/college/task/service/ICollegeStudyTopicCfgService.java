package com.microtomato.hirun.modules.college.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeStudyTopicCfg;

import java.util.List;

/**
 * (CollegeStudyTopicCfg)表服务接口
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-09-16 01:58:14
 */
public interface ICollegeStudyTopicCfgService extends IService<CollegeStudyTopicCfg> {

    List<CollegeStudyTopicCfg> queryEffectiveByStudyAndChaptersId(String studyId, String chaptersId);

    CollegeStudyTopicCfg getEffectiveByStudyId(String studyId);
}