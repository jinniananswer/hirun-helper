package com.microtomato.hirun.modules.college.task.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTask;
import com.microtomato.hirun.modules.college.task.mapper.CollegeEmployeeTaskMapper;
import com.microtomato.hirun.modules.college.task.service.ICollegeEmployeeTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (CollegeEmployeeTask)表服务实现类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-08-04 00:05:48
 */
@Service("collegeEmployeeTaskService")
@DataSource(DataSourceKey.INS)
public class CollegeEmployeeTaskServiceImpl extends ServiceImpl<CollegeEmployeeTaskMapper, CollegeEmployeeTask> implements ICollegeEmployeeTaskService {

    @Autowired
    private CollegeEmployeeTaskMapper collegeEmployeeTaskMapper;


    @Override
    public List<CollegeEmployeeTask> queryByEmployeeIdAndTaskType(String employeeId, String taskType) {
        return this.list(Wrappers.<CollegeEmployeeTask>lambdaQuery().eq(CollegeEmployeeTask::getEmployeeId, employeeId)
                .eq(CollegeEmployeeTask::getTaskType, taskType));
    }
}