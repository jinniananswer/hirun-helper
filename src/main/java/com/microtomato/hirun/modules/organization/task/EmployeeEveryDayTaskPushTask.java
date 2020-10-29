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
public class EmployeeEveryDayTaskPushTask {

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
    @Scheduled(cron = "0 27 15 * * ?")
    public void scheduled() {
        //统一时间
        LocalDateTime now = LocalDateTime.now();
        log.debug("任务发布时间：" + now);
        List<CollegeStudyTaskCfg> collegeStudyTaskCfgList = collegeStudyTaskCfgServiceImpl.queryEffectiveReleased();
        List<CollegeStudyTaskCfg> allTaskList = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(collegeStudyTaskCfgList)){
            for (CollegeStudyTaskCfg collegeStudyTaskCfg : collegeStudyTaskCfgList){
                //答题任务单独设置
                if (StringUtils.equals("3", collegeStudyTaskCfg.getStudyType())){
                    if (StringUtils.equals("1", collegeStudyTaskCfg.getAnswerTaskType())){
                        allTaskList.add(collegeStudyTaskCfg);
                    }
                }
            }
        }

       /* //清空所有答题任务
        if (ArrayUtils.isNotEmpty(allTaskList)){
            List<Long> taskIdList = new ArrayList<>();
            for (CollegeStudyTaskCfg collegeStudyTaskCfg : allTaskList) {
                List<CollegeEmployeeTask> collegeEmployeeTaskList = collegeEmployeeTaskServiceImpl.queryByStudyTaskId(String.valueOf(collegeStudyTaskCfg.getStudyTaskId()));
                if (ArrayUtils.isNotEmpty(collegeEmployeeTaskList)){
                    for (CollegeEmployeeTask collegeEmployeeTask : collegeEmployeeTaskList) {
                        taskIdList.add(collegeEmployeeTask.getTaskId());
                    }
                }
            }
            if (ArrayUtils.isNotEmpty(taskIdList)){
                collegeEmployeeTaskServiceImpl.clearTaskInfoByTaskIdList(taskIdList, "1");
            }
        }*/

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

                        LocalDateTime firstSecondDay = TimeUtils.getFirstSecondDay(now, 0);
                        LocalDateTime lastSecondDay = TimeUtils.addSeconds(TimeUtils.addDays(firstSecondDay, 1), -1);
                        //如果该员工分配了该项任务，则跳过
                        if (null != employeeTask && TimeUtils.compareTwoTime(firstSecondDay, employeeTask.getStudyStartDate()) == 0){
                            continue;
                        }
                        CollegeEmployeeTask collegeEmployeeTask = new CollegeEmployeeTask();
                        BeanUtils.copyProperties(collegeStudyTaskCfg, collegeEmployeeTask);
                        collegeEmployeeTask.setEmployeeId(String.valueOf(employeeId));
                        collegeEmployeeTask.setStatus("0");
                        collegeEmployeeTask.setStudyTaskId(String.valueOf(studyTaskId));
                        collegeEmployeeTask.setStudyStartDate(firstSecondDay);
                        collegeEmployeeTask.setStudyEndDate(lastSecondDay);
                        collegeEmployeeTaskList.add(collegeEmployeeTask);
                    }
                    if (ArrayUtils.isNotEmpty(collegeEmployeeTaskList)){
                        collegeEmployeeTaskServiceImpl.saveBatch(collegeEmployeeTaskList);
                    }
                }
            }
        }
    }
}
