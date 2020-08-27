package com.microtomato.hirun.modules.college.config.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeStudyTaskRequestDTO;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeStudyTaskResponseDTO;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyTaskCfg;

import java.util.List;

/**
 * (CollegeStudyTaskCfg)表服务接口
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-08-26 02:04:48
 */
public interface ICollegeStudyTaskCfgService extends IService<CollegeStudyTaskCfg> {

    /**
     * 根据任务类型查询所有配置课程数据 1-固定任务，2-活动任务
     * @param taskType
     * @return
     */
    List<CollegeStudyTaskCfg> queryByTaskType(String taskType);

    IPage<CollegeStudyTaskResponseDTO> queryCollegeStufyByPage(CollegeStudyTaskRequestDTO collegeStudyTaskRequestDTO, Page<CollegeStudyTaskRequestDTO> page);

    List<CollegeStudyTaskCfg> queryByStudyTaskIdList(List<Long> studyTaskIdList);
}