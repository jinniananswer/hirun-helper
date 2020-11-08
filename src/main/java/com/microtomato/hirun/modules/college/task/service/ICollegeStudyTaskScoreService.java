package com.microtomato.hirun.modules.college.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeStudyTaskScore;

import java.util.List;

/**
 * (CollegeStudyTaskScore)表服务接口
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-21 23:53:59
 */
public interface ICollegeStudyTaskScoreService extends IService<CollegeStudyTaskScore> {
    void taskScore(CollegeStudyTaskScore collegeStudyTaskScore);

    CollegeStudyTaskScore getStudyScoreByTaskId(String taskId);

    List<CollegeStudyTaskScore> queryAllStudyScore();
}