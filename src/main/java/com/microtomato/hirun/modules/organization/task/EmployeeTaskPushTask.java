package com.microtomato.hirun.modules.organization.task;

import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyTaskCfg;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeTaskJobCfg;
import com.microtomato.hirun.modules.college.config.service.ICollegeStudyTaskCfgService;
import com.microtomato.hirun.modules.college.config.service.ICollegeTaskJobCfgService;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTask;
import com.microtomato.hirun.modules.college.task.service.ICollegeEmployeeTaskService;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class EmployeeTaskPushTask {

    @Autowired
    private ICollegeStudyTaskCfgService collegeStudyTaskCfgServiceImpl;

    @Autowired
    private IEmployeeService employeeServiceImpl;

    @Autowired
    private ICollegeTaskJobCfgService collegeTaskJobCfgServiceImpl;

    @Autowired
    private ICollegeEmployeeTaskService collegeEmployeeTaskServiceImpl;


    /**
     * 每天凌晨 00：00 开始执行。
     * 给员工发布任务自动进程
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduled() {
        //统一时间
        LocalDateTime now = LocalDateTime.now();
        log.debug("任务发布时间：" + now);
        List<CollegeStudyTaskCfg> collegeStudyTaskCfgList = collegeStudyTaskCfgServiceImpl.queryEffectiveReleased();
        List<CollegeStudyTaskCfg> allTaskList = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(collegeStudyTaskCfgList)){
            for (CollegeStudyTaskCfg collegeStudyTaskCfg : collegeStudyTaskCfgList){
                String taskType = collegeStudyTaskCfg.getTaskType();
                if (StringUtils.equals("1", taskType)){
                    allTaskList.add(collegeStudyTaskCfg);
                }else if (StringUtils.equals("2", taskType)){
                    //活动任务判断有效期
                    LocalDateTime taskReleaseDate = collegeStudyTaskCfg.getTaskReleaseDate();
                    Integer taskValidityTerm = collegeStudyTaskCfg.getTaskValidityTerm();
                    //如果在有效期内
                    if (TimeUtils.compareTwoTime(now, TimeUtils.addDays(taskReleaseDate, taskValidityTerm)) < 0){
                        allTaskList.add(collegeStudyTaskCfg);
                    }
                }
            }
        }
        //如果是固定任务
        if (ArrayUtils.isNotEmpty(allTaskList)){
            for (int i = 0 ; i < allTaskList.size(); i++){
                CollegeStudyTaskCfg collegeStudyTaskCfg = allTaskList.get(i);
                String jobType = collegeStudyTaskCfg.getJobType();
                Long studyTaskId = collegeStudyTaskCfg.getStudyTaskId();
                //所有员工
                List<Long> employeeIdList = new ArrayList<>();
                if (StringUtils.equals("2", jobType)){
                    List<Employee> employeeList = employeeServiceImpl.queryAllEffectiveEmployee();
                    if (ArrayUtils.isNotEmpty(employeeList)){
                        for (Employee employee : employeeList){
                            Long employeeId = employee.getEmployeeId();
                            employeeIdList.add(employeeId);
                        }
                    }
                }else {
                    List<CollegeTaskJobCfg> collegeTaskJobCfgList = collegeTaskJobCfgServiceImpl.queryEffectiveByTaskId(String.valueOf(studyTaskId));
                    if (ArrayUtils.isNotEmpty(collegeTaskJobCfgList)){
                        List<String> jobRoleList = new ArrayList<>();
                        for (CollegeTaskJobCfg collegeTaskJobCfg : collegeTaskJobCfgList){
                            String jobRole = collegeTaskJobCfg.getJobRole();
                            jobRoleList.add(jobRole);
                        }
                        if (ArrayUtils.isNotEmpty(jobRoleList)){
                            List<Employee> employeeList = new ArrayList<>();
                            if (StringUtils.equals("0", jobType)){
                                //正式员工
                                employeeList = employeeServiceImpl.queryEffectiveByJobRoleList(jobRoleList);
                            }else if (StringUtils.equals("1", jobType)){
                                //新员工
                                employeeList = employeeServiceImpl.queryNewEffectiveByJobRoleList(jobRoleList);
                            }
                            if (ArrayUtils.isNotEmpty(employeeList) && employeeList.size() > 0){
                                for (Employee employee : employeeList){
                                    Long employeeId = employee.getEmployeeId();
                                    employeeIdList.add(employeeId);
                                }
                            }
                        }
                    }
                }
                if (ArrayUtils.isNotEmpty(employeeIdList)){
                    List<CollegeEmployeeTask> collegeEmployeeTaskList = new ArrayList<>();
                    for (Long employeeId : employeeIdList){
                        CollegeEmployeeTask employeeTask = collegeEmployeeTaskServiceImpl.getLastEffectiveByEmployeeIdAndStudyTaskId(String.valueOf(employeeId), String.valueOf(studyTaskId));
                        //如果该员工分配了该项任务，则跳过
                        if (null != employeeTask){
                            continue;
                        }
                        String studyStartType = collegeStudyTaskCfg.getStudyStartType();
                        LocalDateTime startDate = now;
                        LocalDateTime endDate = now;
                        if (StringUtils.equals("3", studyStartType)){
                            String studyModel = collegeStudyTaskCfg.getStudyModel();
                            if (StringUtils.equals("1", studyModel)){
                                //如果是同时学习
                                String togetherStudyTaskId = collegeStudyTaskCfg.getTogetherStudyTaskId();
                                CollegeEmployeeTask togetherCollegeEmployeeTask = collegeEmployeeTaskServiceImpl.getLastEffectiveByEmployeeIdAndStudyTaskId(String.valueOf(employeeId), togetherStudyTaskId);
                                if (null != togetherCollegeEmployeeTask){
                                    startDate = togetherCollegeEmployeeTask.getStudyStartDate();
                                }else {
                                    CollegeEmployeeTask collegeEmployeeTask = getTogetherStudyTaskByStudyTaskIdAndEmployeeId(collegeEmployeeTaskList, String.valueOf(employeeId), togetherStudyTaskId);
                                    if (null != collegeEmployeeTask){
                                        startDate = collegeEmployeeTask.getStudyStartDate();
                                    }
                                }
                            }else if (StringUtils.equals("0", studyModel)){
                                //如果是顺序学习
                                //查询员工最后一次学习任务
                                CollegeEmployeeTask collegeEmployeeTask = collegeEmployeeTaskServiceImpl.getLastEffectiveByEmployeeId(String.valueOf(employeeId));
                                if (null != collegeEmployeeTask){
                                    LocalDateTime togetherTaskEndDate = collegeEmployeeTask.getStudyEndDate();
                                    //如果同时学习开始时间小于当前时间，则立即后一天开始
                                    if (TimeUtils.compareTwoTime(togetherTaskEndDate, now) < 0){
                                        startDate = TimeUtils.getFirstSecondDay(now, 1);
                                    }else {
                                        //否则取最后一次任务结束时间后一天第一秒
                                        startDate = TimeUtils.getFirstSecondDay(togetherTaskEndDate, 1);
                                    }
                                }else {
                                    //如果员工没有任务
                                    startDate = TimeUtils.getFirstSecondDay(now, 1);
                                }
                            }

                        }else {
                            if (StringUtils.equals("1", studyStartType)){
                                //立即开始
                                startDate = TimeUtils.getFirstSecondDay(now, 1);
                            }else if (StringUtils.equals("2", studyStartType)){
                                //指定天数后开始开始
                                startDate = TimeUtils.getFirstSecondDay(now, collegeStudyTaskCfg.getAppointDay());
                            }
                        }
                        endDate = TimeUtils.addSeconds(TimeUtils.getFirstSecondDay(startDate, Integer.valueOf(collegeStudyTaskCfg.getStudyTime())), -1);
                        CollegeEmployeeTask collegeEmployeeTask = new CollegeEmployeeTask();
                        BeanUtils.copyProperties(collegeStudyTaskCfg, collegeEmployeeTask);
                        collegeEmployeeTask.setEmployeeId(String.valueOf(employeeId));
                        collegeEmployeeTask.setStatus("0");
                        collegeEmployeeTask.setStudyTaskId(String.valueOf(studyTaskId));
                        collegeEmployeeTask.setStudyStartDate(startDate);
                        collegeEmployeeTask.setStudyEndDate(endDate);
                        collegeEmployeeTaskList.add(collegeEmployeeTask);
                    }
                    if (ArrayUtils.isNotEmpty(collegeEmployeeTaskList)){
                        collegeEmployeeTaskServiceImpl.saveBatch(collegeEmployeeTaskList);
                    }
                }
            }
        }
    }

    private CollegeEmployeeTask getTogetherStudyTaskByStudyTaskIdAndEmployeeId(List<CollegeEmployeeTask> list, String employeeId, String studyTaskId){
        if (ArrayUtils.isNotEmpty(list)){
            for (CollegeEmployeeTask collegeEmployeeTask : list){
                if (StringUtils.equals(collegeEmployeeTask.getEmployeeId(), employeeId) && StringUtils.equals(collegeEmployeeTask.getStudyTaskId(), studyTaskId)){
                    return collegeEmployeeTask;
                }
            }
        }
        return null;
    }
}
