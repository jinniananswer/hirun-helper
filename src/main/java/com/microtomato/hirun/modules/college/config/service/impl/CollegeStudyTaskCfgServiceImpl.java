package com.microtomato.hirun.modules.college.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeStudyTaskRequestDTO;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeStudyTaskResponseDTO;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeCourseChaptersCfg;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyTaskCfg;
import com.microtomato.hirun.modules.college.config.mapper.CollegeStudyTaskCfgMapper;
import com.microtomato.hirun.modules.college.config.service.ICollegeCourseChaptersCfgService;
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

    @Override
    public List<CollegeStudyTaskCfg> queryByTaskType(String taskType) {
        return this.list(Wrappers.<CollegeStudyTaskCfg>lambdaQuery()
                .eq(CollegeStudyTaskCfg::getTaskType, taskType).eq(CollegeStudyTaskCfg::getStatus, '0')
                .orderByAsc(CollegeStudyTaskCfg::getStudyOrder));
    }

    @Override
    public IPage<CollegeStudyTaskResponseDTO> queryCollegeStufyByPage(CollegeStudyTaskRequestDTO collegeStudyTaskRequestDTO, Page<CollegeStudyTaskRequestDTO> page) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(null != collegeStudyTaskRequestDTO.getTaskType(), "task_type", collegeStudyTaskRequestDTO.getTaskType());
        queryWrapper.like(StringUtils.isNotEmpty(collegeStudyTaskRequestDTO.getStudyName()), "study_name", collegeStudyTaskRequestDTO.getStudyName());
        queryWrapper.eq(null != collegeStudyTaskRequestDTO.getStudyTaskId(), "study_task_id" , collegeStudyTaskRequestDTO.getStudyTaskId());
        queryWrapper.eq(null != collegeStudyTaskRequestDTO.getStudyId(), "study_id", collegeStudyTaskRequestDTO.getStudyId());
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
}