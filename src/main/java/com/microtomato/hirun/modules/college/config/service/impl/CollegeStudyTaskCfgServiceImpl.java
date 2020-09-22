package com.microtomato.hirun.modules.college.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.college.config.entity.dto.*;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeCourseChaptersCfg;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyTaskCfg;
import com.microtomato.hirun.modules.college.config.mapper.CollegeStudyTaskCfgMapper;
import com.microtomato.hirun.modules.college.config.service.ICollegeCourseChaptersCfgService;
import com.microtomato.hirun.modules.college.config.service.ICollegeStudyTaskCfgService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private IStaticDataService staticDataServiceImpl;

    @Override
    public List<CollegeStudyTaskCfg> queryByTaskType(String taskType) {
        return this.list(Wrappers.<CollegeStudyTaskCfg>lambdaQuery()
                .eq(CollegeStudyTaskCfg::getTaskType, taskType).eq(CollegeStudyTaskCfg::getStatus, '0')
                .orderByAsc(CollegeStudyTaskCfg::getStudyTaskId));
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
            String studyType = collegeStudyTaskResponseDTO.getStudyType();
            String studyTypeName = studyType;
            if (StringUtils.isNotEmpty(studyType)){
                studyTypeName = staticDataServiceImpl.getCodeName("TASK_COURSEWARE_TYPE", studyType);
            }
            collegeStudyTaskResponseDTO.setStudyTypeName(studyTypeName);
            String taskType = collegeStudyTaskResponseDTO.getTaskType();
            String taskTypeName = studyType;
            if (StringUtils.isNotEmpty(taskType)){
                taskTypeName = staticDataServiceImpl.getCodeName("STUDY_TASK_TYPE", taskType);
            }
            collegeStudyTaskResponseDTO.setTaskTypeName(taskTypeName);
            String jobType = collegeStudyTaskResponseDTO.getJobType();
            String jobTypeName = jobType;
            if (StringUtils.isNotEmpty(jobType)){
                jobTypeName = staticDataServiceImpl.getCodeName("JOB_TYPE", jobType);
            }
            collegeStudyTaskResponseDTO.setJobTypeName(jobTypeName);
            String studyModel = collegeStudyTaskResponseDTO.getStudyModel();
            String studyModelName = studyModel;
            if (StringUtils.isNotEmpty(studyModel)){
                studyModelName = staticDataServiceImpl.getCodeName("STUDY_MODEL", studyModel);
            }
            collegeStudyTaskResponseDTO.setStudyModelName(studyModelName);
            String togetherStudyTaskId = collegeStudyTaskResponseDTO.getTogetherStudyTaskId();
            if (StringUtils.isNotEmpty(togetherStudyTaskId)){
                CollegeStudyTaskCfg togetherCollegeStudyTaskCfg = this.getEffectiveByStudyTaskId(Long.valueOf(togetherStudyTaskId));
                if (null != togetherCollegeStudyTaskCfg){
                    collegeStudyTaskResponseDTO.setTogetherStudyTaskName(togetherCollegeStudyTaskCfg.getTaskName());
                }
            }

            String studyStartType = collegeStudyTaskResponseDTO.getStudyStartType();
            String studyStartTypeName = "";
            if (StringUtils.isNotEmpty(studyStartType)){
                studyStartTypeName = staticDataServiceImpl.getCodeName("STUDY_START_TYPE", studyStartType);
            }
            if (StringUtils.isEmpty(studyStartTypeName)){
                studyStartTypeName = studyStartType;
            }
            collegeStudyTaskResponseDTO.setStudyStartTypeName(studyStartTypeName);
            String releaseStatus = collegeStudyTaskResponseDTO.getReleaseStatus();
            String releaseStatusName = staticDataServiceImpl.getCodeName("RELEASE_STATUS", releaseStatus);
            if (StringUtils.isEmpty(releaseStatusName)){
                releaseStatusName = releaseStatus;
            }
            collegeStudyTaskResponseDTO.setReleaseStatusName(releaseStatusName);
            List<CollegeCourseChaptersCfg> collegeCourseChaptersCfgList = collegeCourseChaptersCfgServiceImpl.queryByStudyId(studyId);
            List<CollegeCourseChaptersTaskResponseDTO> collegeCourseChaptersTaskResponseDTOList = new ArrayList<>();
            if (ArrayUtils.isNotEmpty(collegeCourseChaptersCfgList)){
                for (CollegeCourseChaptersCfg collegeCourseChaptersCfg : collegeCourseChaptersCfgList){
                    CollegeCourseChaptersTaskResponseDTO collegeCourseChaptersTaskResponseDTO = new CollegeCourseChaptersTaskResponseDTO();
                    BeanUtils.copyProperties(collegeCourseChaptersCfg, collegeCourseChaptersTaskResponseDTO);
                    String chaptersType = collegeCourseChaptersTaskResponseDTO.getChaptersType();
                    String chaptersTypeName = chaptersType;
                    if (StringUtils.isNotEmpty(chaptersType)){
                        chaptersTypeName = staticDataServiceImpl.getCodeName("CHAPTERS_TYPE", chaptersType);
                    }
                    collegeCourseChaptersTaskResponseDTO.setChaptersTypeName(chaptersTypeName);
                    String chapterStudyModel = collegeCourseChaptersTaskResponseDTO.getStudyModel();
                    String chapterStudyModelName = chapterStudyModel;
                    if (StringUtils.isNotEmpty(chapterStudyModel)){
                        chapterStudyModelName = staticDataServiceImpl.getCodeName("STUDY_MODEL", chapterStudyModel);
                    }
                    collegeCourseChaptersTaskResponseDTO.setStudyModelName(chapterStudyModelName);
                    String togetherChaptersId = collegeCourseChaptersTaskResponseDTO.getTogetherChaptersId();
                    String togetherChaptersName = "";
                    if(StringUtils.isNotEmpty(togetherChaptersId)){
                        togetherChaptersName = collegeCourseChaptersCfgServiceImpl.getChapterNameByChaptersId(Long.valueOf(togetherChaptersId));
                    }
                    collegeCourseChaptersTaskResponseDTO.setTogetherChaptersName(togetherChaptersName);
                    collegeCourseChaptersTaskResponseDTOList.add(collegeCourseChaptersTaskResponseDTO);
                }
            }
            collegeStudyTaskResponseDTO.setCollegeCourseChaptersList(collegeCourseChaptersTaskResponseDTOList);
        }
        return collegeStudyTaskResponseDTOIPage;
    }

    @Override
    public List<CollegeStudyTaskCfg> queryByStudyTaskIdList(List<Long> studyTaskIdList) {
        return this.list(Wrappers.<CollegeStudyTaskCfg>lambdaQuery().eq(CollegeStudyTaskCfg::getStatus, '0')
                .in(CollegeStudyTaskCfg::getStudyTaskId, studyTaskIdList));
    }

    @Override
    public CollegeStudyTaskCfg getEffectiveByStudyTaskId(Long studyTaskId) {
        return this.getOne(Wrappers.<CollegeStudyTaskCfg>lambdaQuery().eq(CollegeStudyTaskCfg::getStudyTaskId, studyTaskId)
                .eq(CollegeStudyTaskCfg::getStatus, "0"));
    }

    @Override
    public CollegeStudyTaskCfg getEffectiveByStudyId(String studyId) {
        return this.getOne(Wrappers.<CollegeStudyTaskCfg>lambdaQuery().eq(CollegeStudyTaskCfg::getStudyId, studyId)
                .eq(CollegeStudyTaskCfg::getStatus, "0"), false);
    }

    @Override
    public CollegeStudyTaskResponseDTO getCollegeStudyTaskByStudyTaskId(String studyTaskId) {
        CollegeStudyTaskResponseDTO result = new CollegeStudyTaskResponseDTO();
        if (StringUtils.isNotEmpty(studyTaskId)){
            CollegeStudyTaskCfg collegeStudyTaskCfg = this.getEffectiveByStudyTaskId(Long.valueOf(studyTaskId));
            if (null != collegeStudyTaskCfg){
                BeanUtils.copyProperties(collegeStudyTaskCfg, result);
                String jobType = result.getJobType();
                String jobTypeName = "";
                if (StringUtils.isNotEmpty(jobType)){
                    jobTypeName = staticDataServiceImpl.getCodeName("JOB_TYPE", jobType);
                }
                if (StringUtils.isEmpty(jobTypeName)){
                    jobTypeName = jobType;
                }
                result.setJobTypeName(jobTypeName);

                String studyType = result.getStudyType();
                String studyTypeName = "";
                if (StringUtils.isNotEmpty(studyType)){
                    studyTypeName = staticDataServiceImpl.getCodeName("TASK_COURSEWARE_TYPE", studyType);
                }
                if (StringUtils.isEmpty(studyTypeName)){
                    studyTypeName = studyType;
                }
                result.setStudyTypeName(studyTypeName);

                String taskType = result.getTaskType();
                String taskTypeName = "";
                if (StringUtils.isNotEmpty(taskType)){
                    taskTypeName = staticDataServiceImpl.getCodeName("STUDY_TASK_TYPE", taskType);
                }
                if (StringUtils.isEmpty(taskTypeName)){
                    taskTypeName = taskType;
                }
                result.setTaskTypeName(taskTypeName);

                /*//设置章节信息
                List<CollegeCourseChaptersCfg> collegeCourseChaptersCfgList = collegeCourseChaptersCfgServiceImpl.queryByStudyId(result.getStudyId());
                List<CollegeCourseChaptersTaskResponseDTO> collegeCourseChaptersTaskResponseDTOList = new ArrayList<>();
                if (ArrayUtils.isNotEmpty(collegeCourseChaptersCfgList)){
                    for (CollegeCourseChaptersCfg collegeCourseChaptersCfg : collegeCourseChaptersCfgList){
                        CollegeCourseChaptersTaskResponseDTO collegeCourseChaptersTaskResponseDTO = new CollegeCourseChaptersTaskResponseDTO();
                        BeanUtils.copyProperties(collegeCourseChaptersCfg, collegeCourseChaptersTaskResponseDTO);
                        collegeCourseChaptersTaskResponseDTO.setStudyName(result.getStudyName());
                        String chaptersType = collegeCourseChaptersTaskResponseDTO.getChaptersType();
                        String chaptersTypeName = chaptersType;
                        if (StringUtils.isNotEmpty(chaptersType)){
                            chaptersTypeName = staticDataServiceImpl.getCodeName("CHAPTERS_TYPE", chaptersType);
                        }
                        collegeCourseChaptersTaskResponseDTO.setChaptersTypeName(chaptersTypeName);
                        String studyModel = collegeCourseChaptersTaskResponseDTO.getStudyModel();
                        String studyModelName = studyModel;
                        if (StringUtils.isNotEmpty(studyModel)){
                            studyModelName = staticDataServiceImpl.getCodeName("STUDY_MODEL", studyModel);
                        }
                        collegeCourseChaptersTaskResponseDTO.setStudyModelName(studyModelName);
                        String togetherChaptersId = collegeCourseChaptersTaskResponseDTO.getTogetherChaptersId();
                        String togetherChaptersName = "";
                        if(StringUtils.isNotEmpty(togetherChaptersId)){
                            togetherChaptersName = collegeCourseChaptersCfgServiceImpl.getChapterNameByChaptersId(Long.valueOf(togetherChaptersId));
                        }
                        collegeCourseChaptersTaskResponseDTO.setTogetherChaptersName(togetherChaptersName);
                        collegeCourseChaptersTaskResponseDTOList.add(collegeCourseChaptersTaskResponseDTO);
                    }
                }
                result.setCollegeCourseChaptersList(collegeCourseChaptersTaskResponseDTOList);*/
            }
        }
        return result;
    }

    @Override
    public List<CollegeTogetherStudyTaskResponseDTO> queryEffectiveTogetherStudyTaskList() {
        List<CollegeTogetherStudyTaskResponseDTO> result = new ArrayList<>();
        List<CollegeStudyTaskCfg> collegeStudyTaskCfgList = this.list(Wrappers.<CollegeStudyTaskCfg>lambdaQuery().eq(CollegeStudyTaskCfg::getStatus, "0")
                .orderByAsc(CollegeStudyTaskCfg::getStudyTaskId));
        if (ArrayUtils.isNotEmpty(collegeStudyTaskCfgList)){
            for (CollegeStudyTaskCfg collegeStudyTaskCfg : collegeStudyTaskCfgList){
                CollegeTogetherStudyTaskResponseDTO collegeTogetherStudyTaskResponseDTO = new CollegeTogetherStudyTaskResponseDTO();
                BeanUtils.copyProperties(collegeStudyTaskCfg, collegeTogetherStudyTaskResponseDTO);
                collegeTogetherStudyTaskResponseDTO.setStudyTaskId(String.valueOf(collegeStudyTaskCfg.getStudyTaskId()));
                result.add(collegeTogetherStudyTaskResponseDTO);
            }
        }
        return result;
    }

    @Override
    public CollegeStudyTaskCfg getAllByStudyTaskId(Long studyTaskId) {
        return this.getOne(Wrappers.<CollegeStudyTaskCfg>lambdaQuery().eq(CollegeStudyTaskCfg::getStudyTaskId, studyTaskId));
    }

    @Override
    public List<CollegeStudyTaskCfg> queryEffectiveReleased() {
        return this.list(Wrappers.<CollegeStudyTaskCfg>lambdaQuery().eq(CollegeStudyTaskCfg::getStatus, '0')
                .in(CollegeStudyTaskCfg::getReleaseStatus, '1')
                .orderByAsc(CollegeStudyTaskCfg::getStudyTaskId));
    }
}