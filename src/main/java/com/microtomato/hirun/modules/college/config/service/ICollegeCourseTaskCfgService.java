package com.microtomato.hirun.modules.college.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeCourseTaskCfg;

import java.util.List;

/**
 * (CollegeCourseTaskCfg)表服务接口
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-08-04 00:19:05
 */
public interface ICollegeCourseTaskCfgService extends IService<CollegeCourseTaskCfg> {

    /**
     * 根据任务类型查询所有配置课程数据 1-固定任务，2-活动任务
     * @param taskType
     * @return
     */
    List<CollegeCourseTaskCfg> queryByTaskType(String taskType);
}