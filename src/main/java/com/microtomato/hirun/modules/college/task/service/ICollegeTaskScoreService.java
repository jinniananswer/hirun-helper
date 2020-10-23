package com.microtomato.hirun.modules.college.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeTaskScore;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * (CollegeTaskScore)表服务接口
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-10-22 04:16:25
 */
public interface ICollegeTaskScoreService extends IService<CollegeTaskScore> {

    int getExamScoreNumByTaskId(Long taskId, String examType);
}