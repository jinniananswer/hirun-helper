package com.microtomato.hirun.modules.college.task.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.task.entity.dto.CollegeEmployeeTaskDetailRequestDTO;
import com.microtomato.hirun.modules.college.task.entity.dto.CollegeEmployeeTaskDetailResponseDTO;
import com.microtomato.hirun.modules.college.task.entity.dto.CollegeEmployeeTaskQueryRequestDTO;
import com.microtomato.hirun.modules.college.task.entity.dto.CollegeEmployeeTaskQueryResponseDTO;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTask;

import java.util.List;

/**
 * (CollegeEmployeeTask)表服务接口
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-08-04 00:05:48
 */
public interface ICollegeEmployeeTaskService extends IService<CollegeEmployeeTask> {

    /**
     * 根据员工ID、任务类型查询非过期的任务
     * @param employeeId
     * @param taskType
     * @return
     */
    List<CollegeEmployeeTask> queryByEmployeeIdAndTaskType(String employeeId, String taskType);

    IPage<CollegeEmployeeTaskQueryResponseDTO> queryEmployeeTask(CollegeEmployeeTaskQueryRequestDTO collegeEmployeeTaskQueryRequestDTO, Page<CollegeEmployeeTaskQueryRequestDTO> page);

    List<CollegeEmployeeTask> queryEffectiveByEmployeeId(String employeeId);

    IPage<CollegeEmployeeTaskDetailResponseDTO> queryEmployeeTaskDetailByPage(CollegeEmployeeTaskDetailRequestDTO collegeEmployeeTaskDetailRequestDTO, Page<CollegeEmployeeTaskDetailRequestDTO> page);
}