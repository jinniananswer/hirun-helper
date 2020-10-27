package com.microtomato.hirun.modules.college.task.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTaskTutor;
import com.microtomato.hirun.modules.college.task.mapper.CollegeEmployeeTaskTutorMapper;
import com.microtomato.hirun.modules.college.task.service.ICollegeEmployeeTaskTutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.List;

/**
 * (CollegeEmployeeTaskTutor)表服务实现类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-04 22:53:05
 */
@Service("collegeEmployeeTaskTutorService")
@DataSource(DataSourceKey.INS)
public class CollegeEmployeeTaskTutorServiceImpl extends ServiceImpl<CollegeEmployeeTaskTutorMapper, CollegeEmployeeTaskTutor> implements ICollegeEmployeeTaskTutorService {

    @Autowired
    private CollegeEmployeeTaskTutorMapper collegeEmployeeTaskTutorMapper;


    @Override
    public List<CollegeEmployeeTaskTutor> queryEffectiveByTaskId(String taskId) {
        return this.list(Wrappers.<CollegeEmployeeTaskTutor>lambdaQuery().eq(CollegeEmployeeTaskTutor::getTaskId, taskId)
                .eq(CollegeEmployeeTaskTutor::getStatus, "0"));
    }

    @Override
    public void addByTaskIdAndSelectTutor(String taskId, String selectTutor) {
        if (StringUtils.isNotBlank(selectTutor)){
            String employeeId = selectTutor.substring(selectTutor.indexOf("[") + 1, selectTutor.indexOf("]"));
            CollegeEmployeeTaskTutor collegeEmployeeTaskTutor = new CollegeEmployeeTaskTutor();
            collegeEmployeeTaskTutor.setStatus("0");
            collegeEmployeeTaskTutor.setTaskId(taskId);
            collegeEmployeeTaskTutor.setTutorId(employeeId);
            this.save(collegeEmployeeTaskTutor);
        }
    }
}