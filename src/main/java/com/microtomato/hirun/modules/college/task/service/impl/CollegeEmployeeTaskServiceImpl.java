package com.microtomato.hirun.modules.college.task.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyTaskCfg;
import com.microtomato.hirun.modules.college.config.service.ICollegeCourseChaptersCfgService;
import com.microtomato.hirun.modules.college.config.service.ICollegeStudyTaskCfgService;
import com.microtomato.hirun.modules.college.task.entity.dto.CollegeEmployeeTaskDetailRequestDTO;
import com.microtomato.hirun.modules.college.task.entity.dto.CollegeEmployeeTaskDetailResponseDTO;
import com.microtomato.hirun.modules.college.task.entity.dto.CollegeEmployeeTaskQueryRequestDTO;
import com.microtomato.hirun.modules.college.task.entity.dto.CollegeEmployeeTaskQueryResponseDTO;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTask;
import com.microtomato.hirun.modules.college.task.mapper.CollegeEmployeeTaskMapper;
import com.microtomato.hirun.modules.college.task.service.ICollegeEmployeeTaskService;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.service.ICourseService;
import com.microtomato.hirun.modules.organization.service.impl.EmployeeServiceImpl;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import com.microtomato.hirun.modules.system.service.IUploadFileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;

    @Autowired
    private ICourseService courseServiceImpl;

    @Autowired
    private IUploadFileService uploadFileServiceImpl;

    @Autowired
    private IStaticDataService staticDataServiceImpl;

    @Autowired
    private ICollegeStudyTaskCfgService collegeStudyTaskCfgServiceImpl;

    @Autowired
    private ICollegeCourseChaptersCfgService collegeCourseChaptersCfgServiceImpl;

    @Override
    public List<CollegeEmployeeTask> queryByEmployeeIdAndTaskType(String employeeId, String taskType) {
        return this.list(Wrappers.<CollegeEmployeeTask>lambdaQuery().eq(CollegeEmployeeTask::getEmployeeId, employeeId));
    }

    @Override
    public IPage<CollegeEmployeeTaskQueryResponseDTO> queryEmployeeTask(CollegeEmployeeTaskQueryRequestDTO collegeEmployeeTaskQueryRequestDTO, Page<CollegeEmployeeTaskQueryRequestDTO> page) {
        List<Employee> employeeList = employeeServiceImpl.queryByEmployeeIdAndLikeName(collegeEmployeeTaskQueryRequestDTO.getEmployeeId(), collegeEmployeeTaskQueryRequestDTO.getEmployeeName());
        Map<String, String> employeeMap = new HashMap<>();
        List<String> employeeIdList = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(employeeList)){
            for (Employee employee : employeeList){
                String employeeId = String.valueOf(employee.getEmployeeId());
                String name = employee.getName();
                employeeIdList.add(employeeId);
                employeeMap.put(employeeId, name);
            }
        }
        if (ArrayUtils.isNotEmpty(employeeIdList)){
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.in("employee_id", employeeIdList);
            queryWrapper.eq("status", '0');
            queryWrapper.groupBy("employee_id");
            IPage<CollegeEmployeeTaskQueryResponseDTO> result = this.collegeEmployeeTaskMapper.queryEmployeeTask(page, queryWrapper);
            List<CollegeEmployeeTaskQueryResponseDTO> records = result.getRecords();
            for (CollegeEmployeeTaskQueryResponseDTO collegeEmployeeTaskQueryResponseDTO : records){
                String employeeId = collegeEmployeeTaskQueryResponseDTO.getEmployeeId();
                collegeEmployeeTaskQueryResponseDTO.setEmployeeName(employeeMap.get(employeeId));
                int taskNum = 0;
                int studyNum = 0;
                List<CollegeEmployeeTask> collegeEmployeeTaskList = this.queryEffectiveByEmployeeId(employeeId);
                if (ArrayUtils.isNotEmpty(collegeEmployeeTaskList)){
                    taskNum = collegeEmployeeTaskList.size();
                    Map<String, String> studyMap = new HashMap<>();
                    for (CollegeEmployeeTask collegeEmployeeTask : collegeEmployeeTaskList){
                        studyMap.put(collegeEmployeeTask.getStudyTaskId(), null);
                    }
                    studyNum = studyMap.size();
                }
                collegeEmployeeTaskQueryResponseDTO.setTaskNum(taskNum);
                collegeEmployeeTaskQueryResponseDTO.setStudyNum(studyNum);
            }
            return result;
        }
        return new Page<>();
    }

    @Override
    public List<CollegeEmployeeTask> queryEffectiveByEmployeeId(String employeeId) {
        return this.list(Wrappers.<CollegeEmployeeTask>lambdaQuery().eq(CollegeEmployeeTask::getEmployeeId, employeeId)
                .eq(CollegeEmployeeTask::getStatus, "0")
                .orderByDesc(CollegeEmployeeTask::getStudyEndDate));
    }

    @Override
    public IPage<CollegeEmployeeTaskDetailResponseDTO> queryEmployeeTaskDetailByPage(CollegeEmployeeTaskDetailRequestDTO collegeEmployeeTaskDetailRequestDTO, Page<CollegeEmployeeTaskDetailRequestDTO> page) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("employee_id", collegeEmployeeTaskDetailRequestDTO.getEmployeeId());
        queryWrapper.eq("status", '0');
        IPage<CollegeEmployeeTaskDetailResponseDTO> result = this.collegeEmployeeTaskMapper.queryEmployeeTaskDetailByPage(page, queryWrapper);
        List<CollegeEmployeeTaskDetailResponseDTO> records = result.getRecords();
        for (CollegeEmployeeTaskDetailResponseDTO collegeEmployeeTaskDetailResponseDTO : records){
            String studyTaskId = collegeEmployeeTaskDetailResponseDTO.getStudyTaskId();
            CollegeStudyTaskCfg collegeStudyTaskCfg = collegeStudyTaskCfgServiceImpl.getEffectiveByStudyTaskId(Long.valueOf(studyTaskId));
            if (null != collegeStudyTaskCfg){
                BeanUtils.copyProperties(collegeStudyTaskCfg, collegeEmployeeTaskDetailResponseDTO);
            }
            String studyType = collegeEmployeeTaskDetailResponseDTO.getStudyType();
            String studyTypeName = staticDataServiceImpl.getCodeName("TASK_COURSEWARE_TYPE", studyType);
            if (StringUtils.isEmpty(studyTypeName)){
                studyTypeName = studyType;
            }
            collegeEmployeeTaskDetailResponseDTO.setStudyTypeName(studyTypeName);
            String studyName = "";
            String studyId = collegeEmployeeTaskDetailResponseDTO.getStudyId();
            if (StringUtils.equals("0", studyType)){
                studyName = courseServiceImpl.getCourseNameByCourseId(Long.valueOf(studyId));
            }else if (StringUtils.equals("1", studyType)){
                studyName = uploadFileServiceImpl.getFileNameByFileId(studyId);
            }
            if (StringUtils.isEmpty(studyName)){
                studyName = studyId;
            }
            collegeEmployeeTaskDetailResponseDTO.setStudyName(studyName);
            String chapterId = collegeEmployeeTaskDetailResponseDTO.getChapterId();
            if (StringUtils.isNotEmpty(chapterId)){
                String chapterName = collegeCourseChaptersCfgServiceImpl.getChapterNameByChaptersId(Long.valueOf(chapterId));
                if (StringUtils.isEmpty(chapterName)){
                    chapterName = chapterId;
                }
                collegeEmployeeTaskDetailResponseDTO.setChapterName(chapterName);
            }
            String taskType = collegeEmployeeTaskDetailResponseDTO.getTaskType();
            String taskTypeName = staticDataServiceImpl.getCodeName("STUDY_TASK_TYPE", taskType);
            if (StringUtils.isEmpty(taskTypeName)){
                taskTypeName = taskType;
            }
            collegeEmployeeTaskDetailResponseDTO.setTaskTypeName(taskTypeName);
        }
        return result;
    }

    @Override
    public CollegeEmployeeTask getLastEffectiveByEmployeeId(String employeeId) {
        List<CollegeEmployeeTask> collegeEmployeeTaskList = this.queryEffectiveByEmployeeId(employeeId);
        if (ArrayUtils.isNotEmpty(collegeEmployeeTaskList)){
            return collegeEmployeeTaskList.get(0);
        }
        return null;
    }

    @Override
    public CollegeEmployeeTask getLastEffectiveByEmployeeIdAndStudyTaskId(String employeeId, String studyTaskId) {
        List<CollegeEmployeeTask> collegeEmployeeTaskList = this.list(Wrappers.<CollegeEmployeeTask>lambdaQuery().eq(CollegeEmployeeTask::getEmployeeId, employeeId)
                .eq(CollegeEmployeeTask::getStatus, "0")
                .eq(CollegeEmployeeTask::getStudyTaskId, studyTaskId)
                .orderByDesc(CollegeEmployeeTask::getStudyEndDate));
        if (ArrayUtils.isNotEmpty(collegeEmployeeTaskList)){
            return collegeEmployeeTaskList.get(0);
        }
        return null;
    }

}