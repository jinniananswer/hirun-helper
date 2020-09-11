package com.microtomato.hirun.modules.college.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeStudyExamResponseDTO;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeStudyExercisesResponseDTO;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeStudyTaskRequestDTO;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeStudyTaskResponseDTO;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeCourseChaptersCfg;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyExamCfg;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyExercisesCfg;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyTaskCfg;
import com.microtomato.hirun.modules.college.config.mapper.CollegeStudyTaskCfgMapper;
import com.microtomato.hirun.modules.college.config.service.ICollegeCourseChaptersCfgService;
import com.microtomato.hirun.modules.college.config.service.ICollegeStudyExamCfgService;
import com.microtomato.hirun.modules.college.config.service.ICollegeStudyExercisesCfgService;
import com.microtomato.hirun.modules.college.config.service.ICollegeStudyTaskCfgService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (CollegeStudyTaskCfg)表服务实现类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-08-26 02:04:48
 */
@Service("collegeStudyTaskCfgService")
@DataSource(DataSourceKey.SYS)
public class CollegeStudyTaskCfgServiceImpl extends ServiceImpl<CollegeStudyTaskCfgMapper, CollegeStudyTaskCfg> implements ICollegeStudyTaskCfgService {

    @Autowired
    private CollegeStudyTaskCfgMapper collegeStudyTaskCfgMapper;

    @Autowired
    private ICollegeCourseChaptersCfgService collegeCourseChaptersCfgServiceImpl;

    @Autowired
    private ICollegeStudyExercisesCfgService collegeStudyExercisesCfgServiceImpl;

    @Autowired
    private ICollegeStudyExamCfgService collegeStudyExamCfgServiceImpl;

    @Override
    public List<CollegeStudyTaskCfg> queryByTaskType(String taskType) {
        return this.list(Wrappers.<CollegeStudyTaskCfg>lambdaQuery()
                .eq(CollegeStudyTaskCfg::getTaskType, taskType).eq(CollegeStudyTaskCfg::getStatus, '0')
                .orderByAsc(CollegeStudyTaskCfg::getStudyOrder));
    }

    @Override
    public IPage<CollegeStudyTaskResponseDTO> queryCollegeStudyByPage(CollegeStudyTaskRequestDTO collegeStudyTaskRequestDTO, Page<CollegeStudyTaskRequestDTO> page) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(null != collegeStudyTaskRequestDTO.getTaskType(), "task_type", collegeStudyTaskRequestDTO.getTaskType());
        queryWrapper.like(StringUtils.isNotEmpty(collegeStudyTaskRequestDTO.getStudyName()), "study_name", collegeStudyTaskRequestDTO.getStudyName());
        queryWrapper.eq(null != collegeStudyTaskRequestDTO.getStudyTaskId(), "study_task_id" , collegeStudyTaskRequestDTO.getStudyTaskId());
        queryWrapper.eq(null != collegeStudyTaskRequestDTO.getStudyId(), "study_id", collegeStudyTaskRequestDTO.getStudyId());
        queryWrapper.eq("status", '0');
        IPage<CollegeStudyTaskResponseDTO> collegeStudyTaskResponseDTOIPage = this.collegeStudyTaskCfgMapper.queryCollegeStudyByPage(page, queryWrapper);
        List<CollegeStudyTaskResponseDTO> records = collegeStudyTaskResponseDTOIPage.getRecords();
        for (CollegeStudyTaskResponseDTO collegeStudyTaskResponseDTO : records){
            String studyId = collegeStudyTaskResponseDTO.getStudyId();
            List<CollegeCourseChaptersCfg> collegeCourseChaptersCfgList = collegeCourseChaptersCfgServiceImpl.queryByStudyId(studyId);
            collegeStudyTaskResponseDTO.setCollegeCourseChaptersList(collegeCourseChaptersCfgList);
        }
        return collegeStudyTaskResponseDTOIPage;
    }

    @Override
    public List<CollegeStudyTaskCfg> queryByStudyTaskIdList(List<Long> studyTaskIdList) {
        return this.list(Wrappers.<CollegeStudyTaskCfg>lambdaQuery().eq(CollegeStudyTaskCfg::getStatus, '0')
                .in(CollegeStudyTaskCfg::getStudyTaskId, studyTaskIdList));
    }

    @Override
    public CollegeStudyTaskCfg getByStudyTaskId(Long studyTaskId) {
        return this.getOne(Wrappers.<CollegeStudyTaskCfg>lambdaQuery().eq(CollegeStudyTaskCfg::getStudyTaskId, studyTaskId)
                .eq(CollegeStudyTaskCfg::getStatus, "0"));
    }

    @Override
    public CollegeStudyTaskCfg getEffectiveByStudyId(String studyId) {
        return this.getOne(Wrappers.<CollegeStudyTaskCfg>lambdaQuery().eq(CollegeStudyTaskCfg::getStudyId, studyId)
                .eq(CollegeStudyTaskCfg::getStatus, "0"), false);
    }

    @Override
    public IPage<CollegeStudyExercisesResponseDTO> queryCollegeStudyExercisesByPage(CollegeStudyTaskRequestDTO collegeStudyTaskRequestDTO, Page<CollegeStudyTaskRequestDTO> page) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(null != collegeStudyTaskRequestDTO.getTaskType(), "task_type", collegeStudyTaskRequestDTO.getTaskType());
        queryWrapper.like(StringUtils.isNotEmpty(collegeStudyTaskRequestDTO.getStudyName()), "study_name", collegeStudyTaskRequestDTO.getStudyName());
        queryWrapper.eq(null != collegeStudyTaskRequestDTO.getStudyTaskId(), "study_task_id" , collegeStudyTaskRequestDTO.getStudyTaskId());
        queryWrapper.eq(null != collegeStudyTaskRequestDTO.getStudyId(), "study_id", collegeStudyTaskRequestDTO.getStudyId());
        queryWrapper.eq("status", '0');
        IPage<CollegeStudyExercisesResponseDTO> result = this.collegeStudyTaskCfgMapper.queryCollegeStudyExercisesByPage(page, queryWrapper);
        List<CollegeStudyExercisesResponseDTO> records = result.getRecords();
        for (CollegeStudyExercisesResponseDTO collegeStudyExercisesResponseDTO : records){
            String studyId = collegeStudyExercisesResponseDTO.getStudyId();
            List<CollegeStudyExercisesCfg> collegeStudyExercisesCfgList = collegeStudyExercisesCfgServiceImpl.queryEffectiveByStudyIdAndChaptersId(studyId, collegeStudyExercisesResponseDTO.getChaptersId());
            if (ArrayUtils.isNotEmpty(collegeStudyExercisesCfgList)){
                collegeStudyExercisesResponseDTO.setCollegeStudyExercisesList(collegeStudyExercisesCfgList);
            }
        }
        return result;
    }

    @Override
    public IPage<CollegeStudyExamResponseDTO> queryCollegeStudyExamByPage(CollegeStudyTaskRequestDTO collegeStudyTaskRequestDTO, Page<CollegeStudyTaskRequestDTO> page) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(null != collegeStudyTaskRequestDTO.getTaskType(), "task_type", collegeStudyTaskRequestDTO.getTaskType());
        queryWrapper.like(StringUtils.isNotEmpty(collegeStudyTaskRequestDTO.getStudyName()), "study_name", collegeStudyTaskRequestDTO.getStudyName());
        queryWrapper.eq(null != collegeStudyTaskRequestDTO.getStudyTaskId(), "study_task_id" , collegeStudyTaskRequestDTO.getStudyTaskId());
        queryWrapper.eq(null != collegeStudyTaskRequestDTO.getStudyId(), "study_id", collegeStudyTaskRequestDTO.getStudyId());
        queryWrapper.eq("status", '0');
        IPage<CollegeStudyExamResponseDTO> result = this.collegeStudyTaskCfgMapper.queryCollegeStudyExamByPage(page, queryWrapper);
        List<CollegeStudyExamResponseDTO> records = result.getRecords();
        for (CollegeStudyExamResponseDTO collegeStudyExamResponseDTO : records){
            String studyId = collegeStudyExamResponseDTO.getStudyId();
            List<CollegeStudyExamCfg> collegeStudyExercisesCfgList = collegeStudyExamCfgServiceImpl.queryEffectiveByStudyIdAndChaptersId(studyId, collegeStudyExamResponseDTO.getChaptersId());
            if (ArrayUtils.isNotEmpty(collegeStudyExercisesCfgList)){
                collegeStudyExamResponseDTO.setCollegeStudyExamList(collegeStudyExercisesCfgList);
            }
        }
        return result;
    }
}