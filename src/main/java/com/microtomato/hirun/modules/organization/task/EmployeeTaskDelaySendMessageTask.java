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
import com.microtomato.hirun.modules.system.service.INotifyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class EmployeeTaskDelaySendMessageTask {

    @Autowired
    private ICollegeStudyTaskCfgService collegeStudyTaskCfgServiceImpl;

    @Autowired
    private IEmployeeService employeeServiceImpl;

    @Autowired
    private ICollegeEmployeeTaskService collegeEmployeeTaskServiceImpl;

    @Autowired
    private INotifyService notifyService;


    /**
     * 每天凌晨 00：00 开始执行。
     * 给员工发布任务自动进程
     */
    @Scheduled(cron = "0 5 0 * * ?")
    public void scheduled() {
        //统一时间
        LocalDateTime now = LocalDateTime.now();
        List<CollegeEmployeeTask> collegeEmployeeTaskList = collegeEmployeeTaskServiceImpl.queryAllTaskByDelay();
        if (ArrayUtils.isNotEmpty(collegeEmployeeTaskList)){
            for (CollegeEmployeeTask collegeEmployeeTask : collegeEmployeeTaskList) {
                String employeeId = collegeEmployeeTask.getEmployeeId();
                String studyTaskId = collegeEmployeeTask.getStudyTaskId();
                LocalDateTime studyEndDate = collegeEmployeeTask.getStudyEndDate();
                CollegeStudyTaskCfg collegeStudyTaskCfg = collegeStudyTaskCfgServiceImpl.getEffectiveByStudyTaskId(Long.valueOf(studyTaskId));
                if (null != collegeStudyTaskCfg){
                    String taskName = collegeStudyTaskCfg.getTaskName();
                    if (StringUtils.equals("3", collegeStudyTaskCfg.getStudyType())){
                        if (StringUtils.equals("1", collegeStudyTaskCfg.getAnswerTaskType())){
                            LocalDateTime studyStartDate = collegeEmployeeTask.getStudyStartDate();
                            String dateStr = TimeUtils.formatLocalDateTimeToString(studyStartDate, TimeUtils.DATE_FMT_4);
                            taskName = dateStr + "答题任务";
                        }else if (StringUtils.equals("2", collegeStudyTaskCfg.getAnswerTaskType())){

                            LocalDateTime studyStartDate = collegeEmployeeTask.getStudyStartDate();
                            int year = studyStartDate.getYear();
                            int monthValue = studyStartDate.getMonthValue();
                            WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY,1);
                            int weeks = studyStartDate.get(weekFields.weekOfMonth());
                            taskName = year + "年" + monthValue + "月第" + weeks + "周答题任务";
                        }
                    }
                    //发送消息给对应的员工
                    String hrContent = employeeServiceImpl.getEmployeeNameEmployeeId(Long.valueOf(employeeId)) + " 您好！您的【" +
                            taskName + "】已到期，请尽快完成任务。";
                    notifyService.sendMessage(Long.valueOf(employeeId), hrContent, 1L);
                }
            }
        }
    }
}
