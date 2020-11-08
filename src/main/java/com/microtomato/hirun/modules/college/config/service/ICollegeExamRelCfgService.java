package com.microtomato.hirun.modules.college.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeExamRelCfg;

import java.util.List;

/**
 * (CollegeExamRelCfg)表服务接口
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-24 04:19:05
 */
public interface ICollegeExamRelCfgService extends IService<CollegeExamRelCfg> {

    List<CollegeExamRelCfg> queryExamRelInfo(Long examTopicId);

    List<CollegeExamRelCfg> queryByExamTopicId(Long examTopicId);

    CollegeExamRelCfg getEffectiveByExamTopicIdAndTopicType(Long examTopicId, String topicType);
}