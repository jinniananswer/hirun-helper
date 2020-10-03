package com.microtomato.hirun.modules.college.task.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.framework.util.UserContextUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyTaskCfg;
import com.microtomato.hirun.modules.college.config.service.ICollegeCourseChaptersCfgService;
import com.microtomato.hirun.modules.college.config.service.ICollegeStudyTaskCfgService;
import com.microtomato.hirun.modules.college.task.entity.dto.*;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTask;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTaskScore;
import com.microtomato.hirun.modules.college.task.mapper.CollegeEmployeeTaskMapper;
import com.microtomato.hirun.modules.college.task.service.ICollegeEmployeeTaskScoreService;
import com.microtomato.hirun.modules.college.task.service.ICollegeEmployeeTaskService;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.service.ICourseService;
import com.microtomato.hirun.modules.organization.service.IEmployeeJobRoleService;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import com.microtomato.hirun.modules.organization.service.impl.EmployeeServiceImpl;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import com.microtomato.hirun.modules.system.service.IUploadFileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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

    @Autowired
    private IEmployeeJobRoleService employeeJobRoleServiceImpl;

    @Autowired
    private ICollegeEmployeeTaskScoreService collegeEmployeeTaskScoreServiceImpl;

    @Autowired
    private IOrgService orgServiceImpl;

    @Override
    public List<CollegeEmployeeTask> queryByEmployeeIdAndTaskType(String employeeId, String taskType) {
        return this.list(Wrappers.<CollegeEmployeeTask>lambdaQuery().eq(CollegeEmployeeTask::getEmployeeId, employeeId));
    }

    @Override
    public IPage<CollegeEmployeeTaskQueryResponseDTO> queryEmployeeTask(CollegeEmployeeTaskQueryRequestDTO collegeEmployeeTaskQueryRequestDTO, Page<CollegeEmployeeTaskQueryRequestDTO> page) {
        List<Employee> employeeList = employeeServiceImpl.queryByorgIdAndEmployeeIdAndLikeName(collegeEmployeeTaskQueryRequestDTO.getOrgId(), collegeEmployeeTaskQueryRequestDTO.getEmployeeId(), collegeEmployeeTaskQueryRequestDTO.getEmployeeName());
        Map<String, Employee> employeeMap = new HashMap<>();
        List<String> employeeIdList = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(employeeList)){
            for (Employee employee : employeeList){
                String employeeId = String.valueOf(employee.getEmployeeId());
                employeeIdList.add(employeeId);
                employeeMap.put(employeeId, employee);
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
                Employee employee = employeeMap.get(employeeId);
                if (null != employee){
                    collegeEmployeeTaskQueryResponseDTO.setEmployeeName(employee.getName());
                    EmployeeJobRole employeeJobRole = employeeJobRoleServiceImpl.queryValidMain(Long.valueOf(employeeId));
                    if (null != employeeJobRole){
                        String jobRoleName = this.staticDataServiceImpl.getCodeName("JOB_ROLE", employeeJobRole.getJobRole());
                        collegeEmployeeTaskQueryResponseDTO.setJobRoleName(jobRoleName);
                        Org org = orgServiceImpl.queryByOrgId(employeeJobRole.getOrgId());
                        if (null != org){
                            collegeEmployeeTaskQueryResponseDTO.setOrgName(org.getName());
                        }
                    }
                    String sex = employee.getSex();
                    String sexName = staticDataServiceImpl.getCodeName("SEX", sex);
                    if (StringUtils.isEmpty(sexName)){
                        sexName = sex;
                    }
                    collegeEmployeeTaskQueryResponseDTO.setSex(sexName);
                    collegeEmployeeTaskQueryResponseDTO.setMobileNo(employee.getMobileNo());
                }

                int taskNum = 0;
                int finishNum = 0;
                List<CollegeEmployeeTask> collegeEmployeeTaskList = this.queryEffectiveByEmployeeId(employeeId);
                if (ArrayUtils.isNotEmpty(collegeEmployeeTaskList)){
                    taskNum = collegeEmployeeTaskList.size();
                    for (CollegeEmployeeTask collegeEmployeeTask : collegeEmployeeTaskList){
                        LocalDateTime studyCompleteDate = collegeEmployeeTask.getStudyCompleteDate();
                        if (null != studyCompleteDate){
                            finishNum++;
                        }
                    }
                }
                collegeEmployeeTaskQueryResponseDTO.setTaskNum(taskNum);
                collegeEmployeeTaskQueryResponseDTO.setFinishNum(finishNum);
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
        String studyType = collegeEmployeeTaskDetailRequestDTO.getStudyType();
        List<CollegeStudyTaskCfg> collegeStudyTaskCfgList = collegeStudyTaskCfgServiceImpl.queryByStudyType(studyType);
        Map<String, CollegeStudyTaskCfg> collegeStudyTaskCfgMap = new HashMap<>();
        List<String> studyTaskIdList = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(collegeStudyTaskCfgList)){
            for (CollegeStudyTaskCfg collegeStudyTaskCfg : collegeStudyTaskCfgList){
                String studyTaskId = String.valueOf(collegeStudyTaskCfg.getStudyTaskId());
                studyTaskIdList.add(studyTaskId);
                collegeStudyTaskCfgMap.put(studyTaskId, collegeStudyTaskCfg);
            }
        }
        queryWrapper.in("study_task_id", studyTaskIdList);
        IPage<CollegeEmployeeTaskDetailResponseDTO> result = this.collegeEmployeeTaskMapper.queryEmployeeTaskDetailByPage(page, queryWrapper);
        List<CollegeEmployeeTaskDetailResponseDTO> records = result.getRecords();
        for (CollegeEmployeeTaskDetailResponseDTO collegeEmployeeTaskDetailResponseDTO : records){
            String studyTaskId = collegeEmployeeTaskDetailResponseDTO.getStudyTaskId();
            CollegeStudyTaskCfg collegeStudyTaskCfg = collegeStudyTaskCfgMap.get(studyTaskId);
            if (null != collegeStudyTaskCfg){
                BeanUtils.copyProperties(collegeStudyTaskCfg, collegeEmployeeTaskDetailResponseDTO);
            }
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
            Integer studyDateLength = collegeEmployeeTaskDetailResponseDTO.getStudyDateLength();
            Integer studyLength = collegeStudyTaskCfg.getStudyLength();
            Double taskProgress = 0.0;
            if (null != studyDateLength && null != studyLength){
                if (studyDateLength > studyLength){
                    studyDateLength = studyLength;
                }
                taskProgress = new BigDecimal((float)studyDateLength * 100/studyLength).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            collegeEmployeeTaskDetailResponseDTO.setTaskProgress(taskProgress);
            LocalDateTime now = LocalDateTime.now();
            //学习完成时间
            LocalDateTime studyCompleteDate = collegeEmployeeTaskDetailResponseDTO.getStudyCompleteDate();
            //学习结束时间
            LocalDateTime studyEndDate = collegeEmployeeTaskDetailResponseDTO.getStudyEndDate();
            String taskRemainderTime = "任务";
            //是否延期
            boolean isDelay = false;
            //是否学习完成
            boolean isStudyComplete = false;
            //是否完成习题
            boolean isExercises = false;
            //是否完成考试
            boolean isExam = false;
            if (null != studyCompleteDate){
                isStudyComplete = true;
                if (TimeUtils.compareTwoTime(studyCompleteDate, studyEndDate) > 0){
                    isDelay = true;
                }
                
            }else {
                if (TimeUtils.compareTwoTime(now, studyEndDate) > 0){
                    isDelay = true;
                } 
            }
            
            Integer exercisesCompletedNumber = collegeEmployeeTaskDetailResponseDTO.getExercisesCompletedNumber();
            Integer exercisesNumber = collegeEmployeeTaskDetailResponseDTO.getExercisesNumber();
            if (null != exercisesCompletedNumber && null != exercisesNumber && exercisesCompletedNumber >= exercisesNumber){
                isExercises = true;
            }
            Integer examScore = collegeEmployeeTaskDetailResponseDTO.getExamScore();
            if (null != examScore){
                isExam = true;
            }
            if (StringUtils.equals("2", studyType)){
                //如果是实践任务
                if (isDelay){
                    if (isStudyComplete){
                        taskRemainderTime = "延期完成";
                    }else {
                        taskRemainderTime = "已延期";
                    }
                }else {
                    if (isStudyComplete){
                        taskRemainderTime = "已完成";
                    }else {
                        taskRemainderTime = "进行中";
                    }
                }
            }else {
                if (!isDelay && isStudyComplete && isExercises && isExam){
                    taskRemainderTime += "完成";
                }else {
                    if (isDelay){
                        if (isStudyComplete && isExercises && isExam){
                            taskRemainderTime += "延期完成";
                        }else {
                            taskRemainderTime += "延期未完成";
                        }
                    }else {
                        long twoTimeDiffSecond = studyEndDate.toEpochSecond(ZoneOffset.UTC) - now.toEpochSecond(ZoneOffset.UTC);
                        if (twoTimeDiffSecond > 0){
                            log.debug("twoTimeDiffSecond:" + twoTimeDiffSecond);
                            long day = twoTimeDiffSecond / 86400;
                            long hour = (twoTimeDiffSecond % 86400) / 3600;
                            long minute = (twoTimeDiffSecond % 3600) / 60;
                            long second = twoTimeDiffSecond % 60;
                            taskRemainderTime = day + "天" + hour + "小时" + minute + "分钟" + second + "秒";
                            log.debug("taskRemainderTime:" + taskRemainderTime);
                        }else {
                            taskRemainderTime += "已延期";
                        }
                    }
                }
            }
            collegeEmployeeTaskDetailResponseDTO.setTaskRemainderTime(taskRemainderTime);
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

    @Override
    public List<CollegeEmployeeTask> queryAllEffective() {
        return this.list(Wrappers.<CollegeEmployeeTask>lambdaQuery().eq(CollegeEmployeeTask::getStatus, "0")
                .orderByDesc(CollegeEmployeeTask::getStudyEndDate));
    }

    @Override
    public List<CollegeEmployeeTask> queryEffectiveByEmployeeIdList(List<String> employeeIdList) {
        return this.list(Wrappers.<CollegeEmployeeTask>lambdaQuery().in(CollegeEmployeeTask::getEmployeeId, employeeIdList)
                .eq(CollegeEmployeeTask::getStatus, "0")
                .orderByDesc(CollegeEmployeeTask::getStudyEndDate));
    }

    @Override
    public CollegeLoginTaskInfoResponseDTO queryLoginEmployeeTaskInfo() {
        LocalDateTime nowTime = TimeUtils.getCurrentLocalDateTime();
        CollegeLoginTaskInfoResponseDTO result = new CollegeLoginTaskInfoResponseDTO();
        UserContext userContext = WebContextUtils.getUserContext();
        if (userContext == null) {
            userContext = UserContextUtils.getUserContext();
        }
        Long employeeId = userContext.getEmployeeId();
        //查询已完成的任务，目前任务有评分的为已完成的
        List<CollegeEmployeeTask> collegeEmployeeTaskList = this.queryAllEffective();

        if (ArrayUtils.isNotEmpty(collegeEmployeeTaskList)){
            List<CollegeEmployeeTaskDetailResponseDTO> todayTaskList = new ArrayList<>();

            List<CollegeEmployeeTaskDetailResponseDTO> tomorrowTaskList = new ArrayList<>();

            List<CollegeEmployeeTaskDetailResponseDTO> finishTaskList = new ArrayList<>();
            for (CollegeEmployeeTask collegeEmployeeTask : collegeEmployeeTaskList){
                Long taskId = collegeEmployeeTask.getTaskId();
                CollegeEmployeeTaskScore collegeEmployeeTaskScore = collegeEmployeeTaskScoreServiceImpl.getByTaskId(String.valueOf(taskId));
                if (null != collegeEmployeeTaskScore){
                    CollegeEmployeeTaskDetailResponseDTO collegeEmployeeTaskDetailResponseDTO = new CollegeEmployeeTaskDetailResponseDTO();
                    BeanUtils.copyProperties(collegeEmployeeTask, collegeEmployeeTaskDetailResponseDTO);
                    String studyTaskId = collegeEmployeeTaskDetailResponseDTO.getStudyTaskId();
                    CollegeStudyTaskCfg collegeStudyTaskCfg = collegeStudyTaskCfgServiceImpl.getAllByStudyTaskId(Long.valueOf(studyTaskId));
                    String taskName = "";
                    if (null != collegeStudyTaskCfg){
                        taskName = collegeStudyTaskCfg.getTaskName();
                        collegeEmployeeTaskDetailResponseDTO.setTaskDesc(collegeStudyTaskCfg.getTaskDesc());
                    }
                    if (StringUtils.isEmpty(taskName)){
                        taskName = studyTaskId;
                    }
                    collegeEmployeeTaskDetailResponseDTO.setTaskName(taskName);
                    finishTaskList.add(collegeEmployeeTaskDetailResponseDTO);
                }else {
                    LocalDateTime studyStartDate = collegeEmployeeTask.getStudyStartDate();
                    LocalDateTime studyEndDate = collegeEmployeeTask.getStudyEndDate();
                    LocalDateTime todayFirstTime = TimeUtils.getFirstSecondDay(nowTime, 0);
                    if (TimeUtils.compareTwoTime(studyStartDate, todayFirstTime) <= 0 && TimeUtils.compareTwoTime(studyEndDate, todayFirstTime) >= 0){
                        CollegeEmployeeTaskDetailResponseDTO collegeEmployeeTaskDetailResponseDTO = new CollegeEmployeeTaskDetailResponseDTO();
                        BeanUtils.copyProperties(collegeEmployeeTask, collegeEmployeeTaskDetailResponseDTO);
                        todayTaskList.add(collegeEmployeeTaskDetailResponseDTO);
                        continue;
                    }

                    LocalDateTime tomorrowFirstTime = TimeUtils.getFirstSecondDay(todayFirstTime, 1);
                    if (TimeUtils.compareTwoTime(studyStartDate, tomorrowFirstTime) <= 0 && TimeUtils.compareTwoTime(studyEndDate, tomorrowFirstTime) >= 0){
                        CollegeEmployeeTaskDetailResponseDTO collegeEmployeeTaskDetailResponseDTO = new CollegeEmployeeTaskDetailResponseDTO();
                        BeanUtils.copyProperties(collegeEmployeeTask, collegeEmployeeTaskDetailResponseDTO);
                        tomorrowTaskList.add(collegeEmployeeTaskDetailResponseDTO);
                        continue;
                    }
                }
            }
            result.setFinishTaskList(finishTaskList);
            result.setTodayTaskList(todayTaskList);
            result.setTomorrowTaskList(tomorrowTaskList);
        }
        return result;
    }

}