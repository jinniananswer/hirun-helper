package com.microtomato.hirun.modules.college.task.service.impl;

import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyTaskCfg;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeTaskJobCfg;
import com.microtomato.hirun.modules.college.config.service.ICollegeCourseChaptersCfgService;
import com.microtomato.hirun.modules.college.config.service.ICollegeStudyTaskCfgService;
import com.microtomato.hirun.modules.college.config.service.ICollegeTaskJobCfgService;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTask;
import com.microtomato.hirun.modules.college.task.service.ICollegeEmployeeTaskService;
import com.microtomato.hirun.modules.college.task.service.ITaskDomainOpenService;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@DataSource(DataSourceKey.INS)
public class TaskDomainOpenServiceImpl implements ITaskDomainOpenService {

    @Autowired
    private ICollegeStudyTaskCfgService collegeStudyTaskCfgServiceImpl;

    @Autowired
    private ICollegeCourseChaptersCfgService collegeCourseChaptersCfgServiceImpl;

    @Autowired
    private ICollegeEmployeeTaskService collegeEmployeeTaskServiceImpl;

    @Autowired
    private IEmployeeService employeeServiceImpl;

    @Autowired
    private ICollegeTaskJobCfgService collegeTaskJobCfgServiceImpl;

    @Override
    public void fixedTaskReleaseByEmployeeList(List<Long> employeeIdList) {
        /*if(ArrayUtils.isNotEmpty(employeeIdList)){
            //1.获取需要分配给新员工的课程以及课程章节
            //1.1查询固定任务配置课程
            List<CollegeStudyTaskCfg> collegeCourseTaskCfgList = collegeStudyTaskCfgServiceImpl.queryByTaskType("1");
            List<CollegeEmployeeTaskDTO> collegeEmployeeTaskDTOList = new ArrayList<>();
            if (ArrayUtils.isNotEmpty(collegeCourseTaskCfgList)){
                //根据课程ID查询章节
                //上一次课程学习顺序
                String lastTimeCourseStudyOrder = "-1";
                //上一课程的学习开始时间
                LocalDateTime lastTimeCourseStartDate = LocalDateTime.now();
                //相同学习顺序课程中较大的结束时间，用于下一次课程时间设置判断
                LocalDateTime lastCourseEndDate = LocalDateTime.now();
                for(int i = 0 ; i < collegeCourseTaskCfgList.size() ; i++){
                    CollegeStudyTaskCfg collegeStudyTaskCfg = collegeCourseTaskCfgList.get(i);
                    String studyId = collegeStudyTaskCfg.getStudyId();
                    List<CollegeCourseChaptersCfg> collegeCourseChaptersCfgList = collegeCourseChaptersCfgServiceImpl.queryByStudyId(studyId);
                    if (ArrayUtils.isNotEmpty(collegeCourseChaptersCfgList)){
                        //如果有章节配置，则需配置章节信息
                        //当前课程学习顺序
                        String studyOrder = collegeStudyTaskCfg.getStudyOrder();
                        //如果当前课程习顺序与上一课程的不一致时，学习开始时间取前面课程较大的结束时间
                        LocalDateTime lastTimeChaptersStartDate = TimeUtils.getFirstSecondDay(lastCourseEndDate, 1);;
                        if (StringUtils.equals(lastTimeCourseStudyOrder, studyOrder)){
                            //如果学习顺序一致，则开始时间与上一课程一致
                            lastTimeChaptersStartDate = lastTimeCourseStartDate;
                        }
                        lastTimeCourseStudyOrder = studyOrder;
                        //上一次章节学习顺序
                        String lastTimeChaptersStudyOrder = "-1";
                        //同时学习的章节中，以学习天数较长的学习结束时间的后一天为下一分批学习章节的学习开始时间
                        //当前课程中同时学习章节最后学习结束时间
                        LocalDateTime lastChaptersEndDate = lastCourseEndDate;
                        for (int j = 0 ; j < collegeCourseChaptersCfgList.size() ; j++){
                            CollegeCourseChaptersCfg collegeCourseChaptersCfg = collegeCourseChaptersCfgList.get(j);
                            CollegeEmployeeTaskDTO collegeEmployeeTaskDTO = new CollegeEmployeeTaskDTO();
                            BeanUtils.copyProperties(collegeCourseChaptersCfg, collegeEmployeeTaskDTO);
                            collegeEmployeeTaskDTO.setStudyId(studyId);
                            collegeEmployeeTaskDTO.setChapterId(String.valueOf(collegeCourseChaptersCfg.getChaptersId()));
                            collegeEmployeeTaskDTO.setStudyType(collegeStudyTaskCfg.getStudyType());
                            collegeEmployeeTaskDTO.setStatus("0");
                            //固定任务
                            collegeEmployeeTaskDTO.setTaskType("1");
                            //学习模式
                            String studyModel = collegeCourseChaptersCfg.getStudyModel();
                            //章节学习顺序
                            String chaptersStudyOrder = collegeCourseChaptersCfg.getChaptersStudyOrder();
                            //学习模式是分批学习并且当前学习顺序与上一章节学习顺序不一致时，学习开始时间应该为同时学习章节最后学习结束时间的后一天
                            LocalDateTime startDate = TimeUtils.getFirstSecondDay(lastChaptersEndDate, 1);
                            //第一章节的开始时间总是取上一课程的结束时间的后一天
                            //如果学习模式是同时学习或者学习顺序与上一章节的一致时，开始时间为上一章节的开始时间
                            if (StringUtils.equals("1", studyModel) || StringUtils.equals(lastTimeChaptersStudyOrder, chaptersStudyOrder)){
                                startDate = lastTimeChaptersStartDate;
                            }
                            if (j == 0){
                                lastTimeCourseStartDate = startDate;
                            }
                            collegeEmployeeTaskDTO.setStudyStartDate(startDate);
                            //将本章节开始时间设置到上一章节的开始时间，用以下一章节判断使用
                            lastTimeChaptersStartDate = startDate;

                            LocalDateTime studyEndDate = TimeUtils.addSeconds(TimeUtils.getFirstSecondDay(startDate, Integer.valueOf(collegeCourseChaptersCfg.getStudyTime())), -1);
                            collegeEmployeeTaskDTO.setStudyEndDate(studyEndDate);

                            //将本章节结束时间设置到上一章节的结束时间，用以下一章节判断使用
                            lastChaptersEndDate = studyEndDate;
                            //取当前章节较大的结束时间
                            if (TimeUtils.compareTwoTime(lastCourseEndDate, studyEndDate) < 0){
                                lastCourseEndDate = studyEndDate;
                            }
                            lastTimeChaptersStudyOrder = chaptersStudyOrder;
                            collegeEmployeeTaskDTOList.add(collegeEmployeeTaskDTO);
                        }
                    }else {
                        //没有章节配置,以课程配置为准
                        CollegeEmployeeTaskDTO collegeEmployeeTaskDTO = new CollegeEmployeeTaskDTO();
                        BeanUtils.copyProperties(collegeStudyTaskCfg, collegeEmployeeTaskDTO);
                        collegeEmployeeTaskDTO.setStatus("0");
                        //当前课程学习顺序
                        String studyOrder = collegeStudyTaskCfg.getStudyOrder();
                        //如果当前课程习顺序与上一课程的不一致时，学习开始时间取前面课程较大的结束时间
                        LocalDateTime startDate = TimeUtils.getFirstSecondDay(lastCourseEndDate, 1);
                        if (StringUtils.equals(lastTimeCourseStudyOrder, studyOrder)){
                            //如果学习顺序一致，则开始时间与上一课程一致
                            startDate = lastTimeCourseStartDate;
                        }
                        collegeEmployeeTaskDTO.setStudyStartDate(startDate);
                        //将本课程开始时间设置到上一课程的开始时间，用以下一课程判断使用
                        lastTimeCourseStartDate = startDate;

                        LocalDateTime studyEndDate = TimeUtils.addSeconds(TimeUtils.getFirstSecondDay(startDate, Integer.valueOf(collegeStudyTaskCfg.getStudyTime())), -1);
                        collegeEmployeeTaskDTO.setStudyEndDate(studyEndDate);
                        //取前面课程较大的结束时间
                        if (TimeUtils.compareTwoTime(lastCourseEndDate, studyEndDate) < 0){
                            lastCourseEndDate = studyEndDate;
                        }
                        lastTimeCourseStudyOrder = studyOrder;
                        collegeEmployeeTaskDTOList.add(collegeEmployeeTaskDTO);
                    }
                }
            }

            List<CollegeEmployeeTask> fixedCollegeEmployeeTaskList = new ArrayList<>();
            if (ArrayUtils.isNotEmpty(collegeEmployeeTaskDTOList)){
                for (Long employeeId : employeeIdList){
                    //获取员工已分配的固定任务
                    List<CollegeEmployeeTask> collegeEmployeeTaskList = collegeEmployeeTaskServiceImpl.queryByEmployeeIdAndTaskType(String.valueOf(employeeId), "1");
                    //如果该员工已经分配了固定任务，在此不能统一再次分配，需要去员工任务管理界面添加
                    if (ArrayUtils.isEmpty(collegeEmployeeTaskList)){
                        for (CollegeEmployeeTaskDTO collegeEmployeeTaskDTO : collegeEmployeeTaskDTOList){
                            CollegeEmployeeTask collegeEmployeeTask = new CollegeEmployeeTask();
                            BeanUtils.copyProperties(collegeEmployeeTaskDTO, collegeEmployeeTask);
                            collegeEmployeeTask.setEmployeeId(String.valueOf(employeeId));
                            fixedCollegeEmployeeTaskList.add(collegeEmployeeTask);
                        }
                    }
                }
            }
            if (ArrayUtils.isNotEmpty(fixedCollegeEmployeeTaskList)){
                collegeEmployeeTaskServiceImpl.saveBatch(fixedCollegeEmployeeTaskList);
            }
        }*/
    }

    @Override
    public void fixedTaskReleaseByTaskList(List<Long> studyTaskIdList) {
        if (ArrayUtils.isNotEmpty(studyTaskIdList)){
            //统一时间
            LocalDateTime now = LocalDateTime.now();
            //先排序
            Collections.sort(studyTaskIdList);
            for (int i = 0 ; i < studyTaskIdList.size() ; i++){
                Long studyTaskId = studyTaskIdList.get(i);
                CollegeStudyTaskCfg collegeStudyTaskCfg = collegeStudyTaskCfgServiceImpl.getEffectiveByStudyTaskId(studyTaskId);
                String jobType = collegeStudyTaskCfg.getJobType();
                //未转正员工
                List<Long> employeeIdList = new ArrayList<>();
                if (StringUtils.equals("0", jobType)){
                    //正式员工
                    //查询任务配置的工作岗位
                    List<CollegeTaskJobCfg> collegeTaskJobCfgList = collegeTaskJobCfgServiceImpl.queryEffectiveByTaskId(String.valueOf(studyTaskId));
                    if (ArrayUtils.isNotEmpty(collegeTaskJobCfgList)){
                        List<String> jobRoleList = new ArrayList<>();
                        for (CollegeTaskJobCfg collegeTaskJobCfg : collegeTaskJobCfgList){
                            String jobRole = collegeTaskJobCfg.getJobRole();
                            jobRoleList.add(jobRole);
                        }
                        if (ArrayUtils.isNotEmpty(jobRoleList)){
                            List<Employee> employeeList = employeeServiceImpl.queryEffectiveByJobRoleList(jobRoleList);
                            if (ArrayUtils.isNotEmpty(employeeList)){
                                for (Employee employee : employeeList){
                                    Long employeeId = employee.getEmployeeId();
                                    employeeIdList.add(employeeId);
                                }
                            }
                        }
                    }
                }else if (StringUtils.equals("1", jobType)){
                    //未转正员工
                    List<Employee> employeeList = employeeServiceImpl.queryNewEffectiveEmployee();
                    if (ArrayUtils.isNotEmpty(employeeList)){
                        for (Employee employee : employeeList){
                            Long employeeId = employee.getEmployeeId();
                            employeeIdList.add(employeeId);
                        }
                    }
                }else if (StringUtils.equals("2", jobType)){
                    //所有员工
                    List<Employee> employeeList = employeeServiceImpl.queryAllEffectiveEmployee();
                    if (ArrayUtils.isNotEmpty(employeeList)){
                        for (Employee employee : employeeList){
                            Long employeeId = employee.getEmployeeId();
                            employeeIdList.add(employeeId);
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
                            //如果接上个任务开始
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

    @Override
    public void taskReleaseByTaskList(List<Long> studyTaskIdList) {
        LocalDateTime now = LocalDateTime.now();
        if (ArrayUtils.isNotEmpty(studyTaskIdList)){
            List<CollegeStudyTaskCfg> collegeStudyTaskCfgList = collegeStudyTaskCfgServiceImpl.queryByStudyTaskIdList(studyTaskIdList);
            if (ArrayUtils.isNotEmpty(collegeStudyTaskCfgList)){
                for (CollegeStudyTaskCfg collegeStudyTaskCfg : collegeStudyTaskCfgList){
                    collegeStudyTaskCfg.setReleaseStatus("1");
                    collegeStudyTaskCfg.setTaskReleaseDate(now);
                }
                collegeStudyTaskCfgServiceImpl.updateBatchById(collegeStudyTaskCfgList);
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
