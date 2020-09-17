package com.microtomato.hirun.modules.college.task.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyTaskCfg;
import com.microtomato.hirun.modules.college.config.service.ICollegeStudyTaskCfgService;
import com.microtomato.hirun.modules.college.task.entity.dto.*;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTask;
import com.microtomato.hirun.modules.college.task.service.ICollegeEmployeeTaskService;
import com.microtomato.hirun.modules.college.task.service.ITaskDomainOpenService;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * (CollegeEmployeeTask)表控制层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-08-04 00:05:49
 */
@RestController
@RequestMapping("/api/CollegeEmployeeTask")
public class CollegeEmployeeTaskController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeEmployeeTaskService collegeEmployeeTaskService;

    @Autowired
    private ITaskDomainOpenService taskDomainOpenServiceImpl;

    @Autowired
    private IEmployeeService employeeServiceImpl;

    @Autowired
    private ICollegeStudyTaskCfgService collegeStudyTaskCfgServiceImpl;

    /**
     * 分页查询所有数据
     *
     * @param page                分页对象
     * @param collegeEmployeeTask 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<CollegeEmployeeTask> selectByPage(Page<CollegeEmployeeTask> page, CollegeEmployeeTask collegeEmployeeTask) {
        return this.collegeEmployeeTaskService.page(page, new QueryWrapper<>(collegeEmployeeTask));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public CollegeEmployeeTask selectById(@PathVariable Serializable id) {
        return this.collegeEmployeeTaskService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param collegeEmployeeTask 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody CollegeEmployeeTask collegeEmployeeTask) {
        return this.collegeEmployeeTaskService.save(collegeEmployeeTask);
    }

    /**
     * 修改数据
     *
     * @param collegeEmployeeTask 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody CollegeEmployeeTask collegeEmployeeTask) {
        return this.collegeEmployeeTaskService.updateById(collegeEmployeeTask);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.collegeEmployeeTaskService.removeByIds(idList);
    }

    @PostMapping("fixedTaskReleaseByEmployeeList")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void fixedTaskReleaseByEmployeeList(@RequestBody List<Long> employeeIdList) {
        this.taskDomainOpenServiceImpl.fixedTaskReleaseByEmployeeList(employeeIdList);
    }

    @GetMapping("queryEmployeeTask")
    @RestResult
    IPage<CollegeEmployeeTaskQueryResponseDTO> queryEmployeeTask(CollegeEmployeeTaskQueryRequestDTO collegeEmployeeTaskQueryRequestDTO){
        Page<CollegeEmployeeTaskQueryRequestDTO> page = new Page<>(collegeEmployeeTaskQueryRequestDTO.getPage(), collegeEmployeeTaskQueryRequestDTO.getLimit());
        return this.collegeEmployeeTaskService.queryEmployeeTask(collegeEmployeeTaskQueryRequestDTO, page);
    }

    @GetMapping("getEmployeeByEmployeeId")
    @RestResult
    public Employee getEmployeeByEmployeeId(@RequestParam("employeeId") String employeeId){
        if (StringUtils.isNotEmpty(employeeId)){
            return employeeServiceImpl.getEEmployeeByEmployeeId(Long.valueOf(employeeId));
        }
        return new Employee();
    }

    @GetMapping("getEmployeeTaskDetailByEmployeeId")
    @RestResult
    public CollegeTaskStatisticsResponseDTO getEmployeeTaskDetailByEmployeeId(@RequestParam("employeeId") String employeeId){
        List<CollegeEmployeeTask> collegeEmployeeTaskList = this.collegeEmployeeTaskService.queryEffectiveByEmployeeId(employeeId);
        CollegeTaskStatisticsResponseDTO result = new CollegeTaskStatisticsResponseDTO();
        int taskFinishNum = 0;
        int exercisesFinishNum = 0;
        int allTaskNum = 0;
        int examPassNum = 0;
        if (ArrayUtils.isNotEmpty(collegeEmployeeTaskList)){
            allTaskNum = collegeEmployeeTaskList.size();
            for (CollegeEmployeeTask collegeEmployeeTask : collegeEmployeeTaskList){
                String studyTaskId = collegeEmployeeTask.getStudyTaskId();
                CollegeStudyTaskCfg collegeStudyTask = collegeStudyTaskCfgServiceImpl.getAllByStudyTaskId(Long.valueOf(studyTaskId));
                LocalDateTime studyCompleteDate = collegeEmployeeTask.getStudyCompleteDate();
                if (null != studyCompleteDate){
                    taskFinishNum++;
                }
                Integer exercisesCompletedNumber = collegeEmployeeTask.getExercisesCompletedNumber();
                if (null != exercisesCompletedNumber){
                    Integer exercisesNumber = collegeStudyTask.getExercisesNumber();
                    if (exercisesCompletedNumber >= exercisesNumber){
                        exercisesFinishNum++;
                    }
                }
                Integer examScore = collegeEmployeeTask.getExamScore();
                if (null != examScore){
                    Integer passScore = collegeStudyTask.getPassScore();
                    if (null != examScore && examScore > passScore){
                        examPassNum++;
                    }
                }
            }

        }
        int taskFinish = 0;
        int exercisesFinish = 0;
        int examPass = 0;
        if (allTaskNum > 0){
            taskFinish = (taskFinishNum * 100) / allTaskNum;
            exercisesFinish = (exercisesFinishNum * 100 ) / allTaskNum;
            examPass = (examPassNum * 100) / allTaskNum;
        }
        result.setExercisesFinish(exercisesFinish);
        result.setExercisesFinishNum(exercisesFinishNum);
        result.setTaskFinish(taskFinish);
        result.setTaskFinishNum(taskFinishNum);
        result.setAllTaskNum(allTaskNum);
        result.setExamPass(examPass);
        result.setExamPassNum(examPassNum);
        return result;
    }

    @GetMapping("queryEmployeeTaskDetailByPage")
    @RestResult
    IPage<CollegeEmployeeTaskDetailResponseDTO> queryEmployeeTaskDetailByPage(CollegeEmployeeTaskDetailRequestDTO collegeEmployeeTaskDetailRequestDTO){
        Page<CollegeEmployeeTaskDetailRequestDTO> page = new Page<>(collegeEmployeeTaskDetailRequestDTO.getPage(), collegeEmployeeTaskDetailRequestDTO.getLimit());
        return this.collegeEmployeeTaskService.queryEmployeeTaskDetailByPage(collegeEmployeeTaskDetailRequestDTO, page);
    }

    @PostMapping("fixedTaskReleaseByTaskList")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void fixedTaskReleaseByTaskList(@RequestBody List<Long> studyTaskIdList) {
        this.taskDomainOpenServiceImpl.fixedTaskReleaseByTaskList(studyTaskIdList);
    }
}