package com.microtomato.hirun.modules.college.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeCourseTaskRequestDTO;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeCourseTaskResponseDTO;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeCourseChaptersCfg;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeCourseTaskCfg;
import com.microtomato.hirun.modules.college.config.mapper.CollegeCourseTaskCfgMapper;
import com.microtomato.hirun.modules.college.config.service.ICollegeCourseChaptersCfgService;
import com.microtomato.hirun.modules.college.config.service.ICollegeCourseTaskCfgService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (CollegeCourseTaskCfg)表服务实现类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-08-04 00:19:05
 */
@Service("collegeCourseTaskCfgService")
public class CollegeCourseTaskCfgServiceImpl extends ServiceImpl<CollegeCourseTaskCfgMapper, CollegeCourseTaskCfg> implements ICollegeCourseTaskCfgService {

    @Autowired
    private CollegeCourseTaskCfgMapper collegeCourseTaskCfgMapper;

    @Autowired
    private ICollegeCourseChaptersCfgService collegeCourseChaptersCfgServiceImpl;


    @Override
    public List<CollegeCourseTaskCfg> queryByTaskType(String taskType) {
        return this.list(Wrappers.<CollegeCourseTaskCfg>lambdaQuery()
                .eq(CollegeCourseTaskCfg::getTaskType, taskType).eq(CollegeCourseTaskCfg::getStatus, '0')
                .orderByAsc(CollegeCourseTaskCfg::getCourseStudyOrder));
    }

    @Override
    public IPage<CollegeCourseTaskResponseDTO> queryCollegeCourseByPage(CollegeCourseTaskRequestDTO collegeCourseTaskRequestDTO, Page<CollegeCourseTaskRequestDTO> page) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(null != collegeCourseTaskRequestDTO.getTaskType(), "task_type", collegeCourseTaskRequestDTO.getTaskType());
        queryWrapper.like(StringUtils.isNotEmpty(collegeCourseTaskRequestDTO.getCourseName()), "course_name", collegeCourseTaskRequestDTO.getCourseName());
        queryWrapper.eq(null != collegeCourseTaskRequestDTO.getCourseTaskId(), "course_task_id" , collegeCourseTaskRequestDTO.getCourseTaskId());
        queryWrapper.eq(null != collegeCourseTaskRequestDTO.getCourseId(), "course_id", collegeCourseTaskRequestDTO.getCourseId());
        IPage<CollegeCourseTaskResponseDTO> collegeCourseTaskResponseDTOIPage = this.collegeCourseTaskCfgMapper.queryCollegeCourseByPage(page, queryWrapper);
        List<CollegeCourseTaskResponseDTO> records = collegeCourseTaskResponseDTOIPage.getRecords();
        for (CollegeCourseTaskResponseDTO collegeCourseTaskResponseDTO : records){
            String courseId = collegeCourseTaskResponseDTO.getCourseId();
            List<CollegeCourseChaptersCfg> collegeCourseChaptersCfgList = collegeCourseChaptersCfgServiceImpl.queryByCourseId(courseId);
            collegeCourseTaskResponseDTO.setCollegeCourseChaptersList(collegeCourseChaptersCfgList);
        }
        return collegeCourseTaskResponseDTOIPage;
    }
}