package com.microtomato.hirun.modules.college.task.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.task.entity.dto.*;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTask;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTaskTutor;
import com.microtomato.hirun.modules.organization.entity.dto.SimpleEmployeeDTO;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import org.springframework.web.bind.annotation.RequestBody;

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

    CollegeEmployeeTask getLastEffectiveByEmployeeId(String employeeId);

    CollegeEmployeeTask getLastEffectiveByEmployeeIdAndStudyTaskId(String employeeId, String studyTaskId);

    List<CollegeEmployeeTask> queryAllEffective();

    List<CollegeEmployeeTask> queryEffectiveByEmployeeIdList(List<String> employeeIdList);

    CollegeLoginTaskInfoResponseDTO queryLoginEmployeeTaskInfo();

    CollegeEmployTaskInfoResponseDTO queryEmployTaskInfoByTaskId(Long taskId);

    List<CollegeEmployeeTaskTutorOptionsDTO> queryLoginEmployeeSelectTutor();

    List<CollegeEmployeeTask> queryByStudyTaskId(String studyTaskId);

    void clearTaskInfoByTaskIdList(List<Long> taskIdList, String answerTaskType);

    List<CollegeEmployeeTaskTutor> queryLoginEmployeeCommentTaskInfo();

    List<CollegeEmployeeTask> queryAllTaskByDelay();
}