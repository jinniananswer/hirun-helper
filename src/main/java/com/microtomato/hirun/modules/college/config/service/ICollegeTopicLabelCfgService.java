package com.microtomato.hirun.modules.college.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeTopicLabelCfg;

import java.util.List;

/**
 * (CollegeTopicLabelCfg)表服务接口
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-10-20 01:28:11
 */
public interface ICollegeTopicLabelCfgService extends IService<CollegeTopicLabelCfg> {

    List<CollegeTopicLabelCfg> queryByLabelId(Long labelId);

    List<CollegeTopicLabelCfg> queryByLabelIdList(List<Long> labelIdList);

    List<CollegeTopicLabelCfg> queryEffectiveLabel();
}