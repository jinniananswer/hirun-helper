package com.microtomato.hirun.modules.college.topic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.topic.entity.po.CollegeTopicLabelRel;

import java.util.List;

/**
 * (CollegeTopicLabelRel)表服务接口
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-10-20 01:22:12
 */
public interface ICollegeTopicLabelRelService extends IService<CollegeTopicLabelRel> {

    List<CollegeTopicLabelRel> queryEffectiveByLabelId(Long labelId);

    List<CollegeTopicLabelRel> queryEffectiveByLabelIdList(List<Long> labelIdList);
}