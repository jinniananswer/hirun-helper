package com.microtomato.hirun.modules.college.task.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.college.task.entity.dto.CollegeEmployTaskInfoResponseDTO;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTask;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeStudyTaskScore;
import com.microtomato.hirun.modules.college.task.mapper.CollegeStudyTaskScoreMapper;
import com.microtomato.hirun.modules.college.task.service.ICollegeEmployeeTaskService;
import com.microtomato.hirun.modules.college.task.service.ICollegeStudyTaskScoreService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (CollegeStudyTaskScore)表服务实现类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-21 23:53:59
 */
@Service("collegeStudyTaskScoreService")
@DataSource(DataSourceKey.INS)
public class CollegeStudyTaskScoreServiceImpl extends ServiceImpl<CollegeStudyTaskScoreMapper, CollegeStudyTaskScore> implements ICollegeStudyTaskScoreService {

    @Autowired
    private CollegeStudyTaskScoreMapper collegeStudyTaskScoreMapper;

    @Autowired
    private ICollegeEmployeeTaskService collegeEmployeeTaskServiceImpl;


    @Override
    public void taskScore(CollegeStudyTaskScore collegeStudyTaskScore) {
        if (null != collegeStudyTaskScore){
            String taskId = collegeStudyTaskScore.getTaskId();
            if (StringUtils.isNotEmpty(taskId)){
                CollegeStudyTaskScore studyScoreByTaskId = this.getStudyScoreByTaskId(taskId);
                if (null != studyScoreByTaskId){
                    studyScoreByTaskId.setTaskDifficultyScore(collegeStudyTaskScore.getTaskDifficultyScore());
                    studyScoreByTaskId.setTutorScore(collegeStudyTaskScore.getTutorScore());
                    this.updateById(studyScoreByTaskId);
                }else {
                    CollegeEmployeeTask collegeEmployeeTask = collegeEmployeeTaskServiceImpl.getById(Long.valueOf(taskId));
                    if (null != collegeEmployeeTask){
                        String studyTaskId = collegeEmployeeTask.getStudyTaskId();
                        collegeStudyTaskScore.setStudyTaskId(studyTaskId);
                        this.save(collegeStudyTaskScore);
                    }
                }
            }
        }
    }

    @Override
    public CollegeStudyTaskScore getStudyScoreByTaskId(String taskId) {
        return this.getOne(Wrappers.<CollegeStudyTaskScore>lambdaQuery().eq(CollegeStudyTaskScore::getTaskId, taskId));
    }

    @Override
    public List<CollegeStudyTaskScore> queryAllStudyScore() {
        return this.list(Wrappers.<CollegeStudyTaskScore>lambdaQuery());
    }
}