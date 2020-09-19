package com.microtomato.hirun.modules.college.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTaskScore;

import java.util.List;

/**
 * (CollegeEmployeeTaskScore)表服务接口
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-18 23:21:00
 */
public interface ICollegeEmployeeTaskScoreService extends IService<CollegeEmployeeTaskScore> {

    CollegeEmployeeTaskScore getByTaskId(String taskId);
}