package com.microtomato.hirun.modules.college.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeTaskEmployeeCfg;

import java.util.List;

/**
 * (CollegeTaskEmployeeCfg)表服务接口
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-11-07 22:36:23
 */
public interface ICollegeTaskEmployeeCfgService extends IService<CollegeTaskEmployeeCfg> {

    List<CollegeTaskEmployeeCfg> queryEffectiveByTaskId(String taskId);
}