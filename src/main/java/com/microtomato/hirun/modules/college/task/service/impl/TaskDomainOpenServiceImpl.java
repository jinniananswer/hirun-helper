package com.microtomato.hirun.modules.college.task.service.impl;

import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeCourseChaptersCfg;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeCourseTaskCfg;
import com.microtomato.hirun.modules.college.config.service.ICollegeCourseChaptersCfgService;
import com.microtomato.hirun.modules.college.config.service.ICollegeCourseTaskCfgService;
import com.microtomato.hirun.modules.college.task.entity.dto.CollegeEmployeeTaskDTO;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTask;
import com.microtomato.hirun.modules.college.task.service.ICollegeEmployeeTaskService;
import com.microtomato.hirun.modules.college.task.service.ITaskDomainOpenService;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskDomainOpenServiceImpl implements ITaskDomainOpenService {

    @Autowired
    private ICollegeCourseTaskCfgService collegeCourseTaskCfgServiceImpl;

    @Autowired
    private ICollegeCourseChaptersCfgService collegeCourseChaptersCfgServiceImpl;

    @Autowired
    private ICollegeEmployeeTaskService collegeEmployeeTaskServiceImpl;

    @Override
    public void fixedTaskReleaseByEmployeeList(List<Long> employeeIdList) {
        if(ArrayUtils.isNotEmpty(employeeIdList)){
            //1.获取需要分配给新员工的课程以及课程章节
            //1.1查询固定任务配置课程
            List<CollegeCourseTaskCfg> collegeCourseTaskCfgList = collegeCourseTaskCfgServiceImpl.queryByTaskType("1");
            List<CollegeEmployeeTaskDTO> collegeEmployeeTaskDTOList = new ArrayList<>();
            if (ArrayUtils.isNotEmpty(collegeCourseTaskCfgList)){
                //根据课程ID查询章节
                //上一次课程学习顺序
                String lastTimeCourseStudyOrder = "-1";
                //上一课程的学习开始时间
                LocalDateTime lastTimeCourseStartDate = TimeUtils.getFirstSecondDay(LocalDateTime.now(), 1);
                //相同学习顺序课程中较大的结束时间，用于下一次课程时间设置判断
                LocalDateTime lastCourseEndDate = TimeUtils.getFirstSecondDay(LocalDateTime.now(), 1);
                //当前课程中同时学习章节最后学习结束时间
                for(int i = 0 ; i < collegeCourseTaskCfgList.size() ; i++){
                    CollegeCourseTaskCfg collegeCourseTaskCfg = collegeCourseTaskCfgList.get(i);
                    String courseId = collegeCourseTaskCfg.getCourseId();
                    List<CollegeCourseChaptersCfg> collegeCourseChaptersCfgList = collegeCourseChaptersCfgServiceImpl.queryByCourseId(courseId);
                    if (ArrayUtils.isNotEmpty(collegeCourseChaptersCfgList)){
                        //如果有章节配置，则需配置章节信息
                        //上一次章节学习顺序
                        String lastTimeChaptersStudyOrder = "-1";
                        //上一章节的学习开始时间
                        LocalDateTime lastTimeChaptersStartDate = lastTimeCourseStartDate;
                        //同时学习的章节中，以学习天数较长的学习结束时间的后一天为下一分批学习章节的学习开始时间
                        //当前课程中同时学习章节最后学习结束时间
                        LocalDateTime lastChaptersEndDate = lastCourseEndDate;
                        for (int j = 0 ; j < collegeCourseChaptersCfgList.size() ; j++){
                            CollegeCourseChaptersCfg collegeCourseChaptersCfg = collegeCourseChaptersCfgList.get(j);
                            CollegeEmployeeTaskDTO collegeEmployeeTaskDTO = new CollegeEmployeeTaskDTO();
                            collegeEmployeeTaskDTO.setCourseId(courseId);
                            collegeEmployeeTaskDTO.setChapterId(String.valueOf(collegeCourseChaptersCfg.getChaptersId()));
                            collegeEmployeeTaskDTO.setCourseType(collegeCourseTaskCfg.getCourseType());
                            collegeEmployeeTaskDTO.setStatus("0");
                            //固定任务
                            collegeEmployeeTaskDTO.setTaskType("1");
                            //学习模式
                            String studyModel = collegeCourseChaptersCfg.getStudyModel();
                            //章节学习顺序
                            String chaptersStudyOrder = collegeCourseChaptersCfg.getChaptersStudyOrder();
                            //学习模式是分批学习并且当前学习顺序与上一章节学习顺序不一致时，学习开始时间应该为同时学习章节最后学习结束时间的后一天
                            LocalDateTime startDate = TimeUtils.getFirstSecondDay(lastChaptersEndDate, 1);
                            //如果学习模式是同时学习或者学习顺序与上一章节的一致时，开始时间为上一章节的开始时间
                            if (StringUtils.equals("1", studyModel) || StringUtils.equals(lastTimeChaptersStudyOrder, chaptersStudyOrder)){
                                startDate = lastTimeChaptersStartDate;
                            }
                            collegeEmployeeTaskDTO.setStudyStartDate(startDate);
                            //将本章节开始时间设置到上一章节的开始时间，用以下一章节判断使用
                            lastTimeChaptersStartDate = startDate;

                            LocalDateTime studyEndDate = TimeUtils.getFirstSecondDay(startDate, Integer.valueOf(collegeCourseChaptersCfg.getStudyTime()));
                            collegeEmployeeTaskDTO.setStudyEndDate(studyEndDate);
                            //取当前章节较大的结束时间
                            if (TimeUtils.compareTwoTime(lastChaptersEndDate, studyEndDate) < 0){
                                lastChaptersEndDate = studyEndDate;
                            }
                            lastTimeChaptersStudyOrder = chaptersStudyOrder;
                            collegeEmployeeTaskDTOList.add(collegeEmployeeTaskDTO);
                        }
                    }else {
                        //没有章节配置,以课程配置未准
                        CollegeEmployeeTaskDTO collegeEmployeeTaskDTO = new CollegeEmployeeTaskDTO();
                        collegeEmployeeTaskDTO.setCourseId(courseId);
                        collegeEmployeeTaskDTO.setCourseType(collegeCourseTaskCfg.getCourseType());
                        collegeEmployeeTaskDTO.setStatus("0");
                        //固定任务
                        collegeEmployeeTaskDTO.setTaskType("1");
                        //当前课程学习顺序
                        String courseStudyOrder = collegeCourseTaskCfg.getCourseStudyOrder();
                        //如果当前课程习顺序与上一课程的不一致时，学习开始时间取前面课程较大的结束时间
                        LocalDateTime startDate = TimeUtils.getFirstSecondDay(lastCourseEndDate, 1);
                        if (StringUtils.equals(lastTimeCourseStudyOrder, courseStudyOrder)){
                            //如果学习顺序一致，则开始时间与上一课程一致
                            startDate = lastTimeCourseStartDate;
                        }
                        collegeEmployeeTaskDTO.setStudyStartDate(startDate);
                        //将本课程开始时间设置到上一课程的开始时间，用以下一课程判断使用
                        lastTimeCourseStartDate = startDate;

                        LocalDateTime studyEndDate = TimeUtils.getFirstSecondDay(startDate, Integer.valueOf(collegeCourseTaskCfg.getStudyTime()));
                        collegeEmployeeTaskDTO.setStudyEndDate(studyEndDate);
                        //取前面课程较大的结束时间
                        if (TimeUtils.compareTwoTime(lastCourseEndDate, studyEndDate) < 0){
                            lastCourseEndDate = studyEndDate;
                        }
                        lastTimeCourseStudyOrder = courseStudyOrder;
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
        }
    }
}
