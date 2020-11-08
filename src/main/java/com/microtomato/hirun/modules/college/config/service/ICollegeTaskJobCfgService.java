package com.microtomato.hirun.modules.college.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeTaskJobCfg;

import java.util.List;

/**
 * (CollegeTaskJobCfg)表服务接口
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-16 03:19:45
 */
public interface ICollegeTaskJobCfgService extends IService<CollegeTaskJobCfg> {

    List<CollegeTaskJobCfg> queryEffectiveByTaskId(String taskId);
}