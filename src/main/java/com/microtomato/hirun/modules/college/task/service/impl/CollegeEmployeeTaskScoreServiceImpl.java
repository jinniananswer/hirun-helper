package com.microtomato.hirun.modules.college.task.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTaskScore;
import com.microtomato.hirun.modules.college.task.mapper.CollegeEmployeeTaskScoreMapper;
import com.microtomato.hirun.modules.college.task.service.ICollegeEmployeeTaskScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (CollegeEmployeeTaskScore)表服务实现类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-18 23:21:01
 */
@Service("collegeEmployeeTaskScoreService")
@DataSource(DataSourceKey.INS)
public class CollegeEmployeeTaskScoreServiceImpl extends ServiceImpl<CollegeEmployeeTaskScoreMapper, CollegeEmployeeTaskScore> implements ICollegeEmployeeTaskScoreService {

    @Autowired
    private CollegeEmployeeTaskScoreMapper collegeEmployeeTaskScoreMapper;


    @Override
    public CollegeEmployeeTaskScore getByTaskId(String taskId) {
        return this.getOne(Wrappers.<CollegeEmployeeTaskScore>lambdaQuery()
                .eq(CollegeEmployeeTaskScore::getTaskId, taskId), false);
    }
}