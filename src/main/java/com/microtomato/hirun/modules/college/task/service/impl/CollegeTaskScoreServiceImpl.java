package com.microtomato.hirun.modules.college.task.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.college.task.mapper.CollegeTaskScoreMapper;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeTaskScore;
import com.microtomato.hirun.modules.college.task.service.ICollegeTaskScoreService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Wrapper;

/**
 * (CollegeTaskScore)表服务实现类
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-10-22 04:16:25
 */
@Service("collegeTaskScoreService")
public class CollegeTaskScoreServiceImpl extends ServiceImpl<CollegeTaskScoreMapper, CollegeTaskScore> implements ICollegeTaskScoreService {

    @Autowired
    private CollegeTaskScoreMapper collegeTaskScoreMapper;


    @Override
    public int getExamScoreNumByTaskId(Long taskId, String examType) {
        return this.count(Wrappers.<CollegeTaskScore>lambdaQuery().eq(CollegeTaskScore::getTaskId, taskId).eq(CollegeTaskScore::getScoreType, examType));
    }
}