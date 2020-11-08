package com.microtomato.hirun.modules.college.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.college.config.entity.dto.*;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeCourseChaptersCfg;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeExamCfg;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeExamRelCfg;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyTaskCfg;
import com.microtomato.hirun.modules.college.config.mapper.CollegeStudyTaskCfgMapper;
import com.microtomato.hirun.modules.college.config.service.ICollegeCourseChaptersCfgService;
import com.microtomato.hirun.modules.college.config.service.ICollegeExamCfgService;
import com.microtomato.hirun.modules.college.config.service.ICollegeExamRelCfgService;
import com.microtomato.hirun.modules.college.config.service.ICollegeStudyTaskCfgService;
import com.microtomato.hirun.modules.college.task.entity.dto.ExamTopicResponseDTO;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTask;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeStudyTaskScore;
import com.microtomato.hirun.modules.college.task.service.ICollegeEmployeeTaskScoreService;
import com.microtomato.hirun.modules.college.task.service.ICollegeEmployeeTaskService;
import com.microtomato.hirun.modules.college.task.service.ICollegeEmployeeTaskTutorService;
import com.microtomato.hirun.modules.college.task.service.ICollegeStudyTaskScoreService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import io.lettuce.core.output.DoubleOutput;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.Internal;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (CollegeStudyTaskCfg)表服务实现类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-08-26 02:04:48
 */
@Service("collegeStudyTaskCfgService")
@DataSource(DataSourceKey.SYS)
public class CollegeStudyTaskCfgServiceImpl extends ServiceImpl<CollegeStudyTaskCfgMapper, CollegeStudyTaskCfg> implements ICollegeStudyTaskCfgService {

    @Autowired
    private CollegeStudyTaskCfgMapper collegeStudyTaskCfgMapper;

    @Autowired
    private ICollegeCourseChaptersCfgService collegeCourseChaptersCfgServiceImpl;

    @Autowired
    private IStaticDataService staticDataServiceImpl;

    @Autowired
    private ICollegeEmployeeTaskService collegeEmployeeTaskServiceImpl;

    @Autowired
    private ICollegeStudyTaskScoreService collegeStudyTaskScoreServiceImpl;

    @Autowired
    private ICollegeExamCfgService collegeExamCfgServiceImpl;

    @Autowired
    private ICollegeExamRelCfgService collegeExamRelCfgServiceImpl;

    @Override
    public List<CollegeStudyTaskCfg> queryByTaskType(String taskType) {
        return this.list(Wrappers.<CollegeStudyTaskCfg>lambdaQuery()
                .eq(CollegeStudyTaskCfg::getTaskType, taskType).eq(CollegeStudyTaskCfg::getStatus, '0')
                .orderByAsc(CollegeStudyTaskCfg::getStudyTaskId));
    }

    @Override
    public IPage<CollegeStudyTaskResponseDTO> queryCollegeStudyByPage(CollegeStudyTaskRequestDTO collegeStudyTaskRequestDTO, Page<CollegeStudyTaskRequestDTO> page) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(null != collegeStudyTaskRequestDTO.getTaskType(), "task_type", collegeStudyTaskRequestDTO.getTaskType());
        queryWrapper.like(StringUtils.isNotEmpty(collegeStudyTaskRequestDTO.getStudyName()), "study_name", collegeStudyTaskRequestDTO.getStudyName());
        queryWrapper.eq(null != collegeStudyTaskRequestDTO.getStudyTaskId(), "study_task_id" , collegeStudyTaskRequestDTO.getStudyTaskId());
        queryWrapper.eq(null != collegeStudyTaskRequestDTO.getStudyId(), "study_id", collegeStudyTaskRequestDTO.getStudyId());
        queryWrapper.eq(StringUtils.isNotEmpty(collegeStudyTaskRequestDTO.getReleaseStatus()), "release_status", collegeStudyTaskRequestDTO.getReleaseStatus());
        queryWrapper.eq("status", '0');
        IPage<CollegeStudyTaskResponseDTO> collegeStudyTaskResponseDTOIPage = this.collegeStudyTaskCfgMapper.queryCollegeStudyByPage(page, queryWrapper);
        List<CollegeStudyTaskResponseDTO> records = collegeStudyTaskResponseDTOIPage.getRecords();
        for (CollegeStudyTaskResponseDTO collegeStudyTaskResponseDTO : records){
            String studyId = collegeStudyTaskResponseDTO.getStudyId();
            String studyType = collegeStudyTaskResponseDTO.getStudyType();
            String studyTypeName = studyType;
            if (StringUtils.isNotEmpty(studyType)){
                studyTypeName = staticDataServiceImpl.getCodeName("TASK_COURSEWARE_TYPE", studyType);
            }
            collegeStudyTaskResponseDTO.setStudyTypeName(studyTypeName);
            String taskType = collegeStudyTaskResponseDTO.getTaskType();
            String taskTypeName = studyType;
            if (StringUtils.isNotEmpty(taskType)){
                taskTypeName = staticDataServiceImpl.getCodeName("STUDY_TASK_TYPE", taskType);
            }
            collegeStudyTaskResponseDTO.setTaskTypeName(taskTypeName);
            String jobType = collegeStudyTaskResponseDTO.getJobType();
            String jobTypeName = jobType;
            if (StringUtils.isNotEmpty(jobType)){
                jobTypeName = staticDataServiceImpl.getCodeName("JOB_TYPE", jobType);
            }
            collegeStudyTaskResponseDTO.setJobTypeName(jobTypeName);
            String studyModel = collegeStudyTaskResponseDTO.getStudyModel();
            String studyModelName = studyModel;
            if (StringUtils.isNotEmpty(studyModel)){
                studyModelName = staticDataServiceImpl.getCodeName("STUDY_MODEL", studyModel);
            }
            collegeStudyTaskResponseDTO.setStudyModelName(studyModelName);
            String togetherStudyTaskId = collegeStudyTaskResponseDTO.getTogetherStudyTaskId();
            if (StringUtils.isNotEmpty(togetherStudyTaskId)){
                CollegeStudyTaskCfg togetherCollegeStudyTaskCfg = this.getEffectiveByStudyTaskId(Long.valueOf(togetherStudyTaskId));
                if (null != togetherCollegeStudyTaskCfg){
                    collegeStudyTaskResponseDTO.setTogetherStudyTaskName(togetherCollegeStudyTaskCfg.getTaskName());
                }
            }

            String studyStartType = collegeStudyTaskResponseDTO.getStudyStartType();
            String studyStartTypeName = "";
            if (StringUtils.isNotEmpty(studyStartType)){
                studyStartTypeName = staticDataServiceImpl.getCodeName("STUDY_START_TYPE", studyStartType);
            }
            if (StringUtils.isEmpty(studyStartTypeName)){
                studyStartTypeName = studyStartType;
            }
            collegeStudyTaskResponseDTO.setStudyStartTypeName(studyStartTypeName);
            String releaseStatus = collegeStudyTaskResponseDTO.getReleaseStatus();
            String releaseStatusName = staticDataServiceImpl.getCodeName("RELEASE_STATUS", releaseStatus);
            if (StringUtils.isEmpty(releaseStatusName)){
                releaseStatusName = releaseStatus;
            }
            collegeStudyTaskResponseDTO.setReleaseStatusName(releaseStatusName);
            List<CollegeCourseChaptersCfg> collegeCourseChaptersCfgList = collegeCourseChaptersCfgServiceImpl.queryByStudyId(studyId);
            List<CollegeCourseChaptersTaskResponseDTO> collegeCourseChaptersTaskResponseDTOList = new ArrayList<>();
            if (ArrayUtils.isNotEmpty(collegeCourseChaptersCfgList)){
                for (CollegeCourseChaptersCfg collegeCourseChaptersCfg : collegeCourseChaptersCfgList){
                    CollegeCourseChaptersTaskResponseDTO collegeCourseChaptersTaskResponseDTO = new CollegeCourseChaptersTaskResponseDTO();
                    BeanUtils.copyProperties(collegeCourseChaptersCfg, collegeCourseChaptersTaskResponseDTO);
                    String chaptersType = collegeCourseChaptersTaskResponseDTO.getChaptersType();
                    String chaptersTypeName = chaptersType;
                    if (StringUtils.isNotEmpty(chaptersType)){
                        chaptersTypeName = staticDataServiceImpl.getCodeName("CHAPTERS_TYPE", chaptersType);
                    }
                    collegeCourseChaptersTaskResponseDTO.setChaptersTypeName(chaptersTypeName);
                    String chapterStudyModel = collegeCourseChaptersTaskResponseDTO.getStudyModel();
                    String chapterStudyModelName = chapterStudyModel;
                    if (StringUtils.isNotEmpty(chapterStudyModel)){
                        chapterStudyModelName = staticDataServiceImpl.getCodeName("STUDY_MODEL", chapterStudyModel);
                    }
                    collegeCourseChaptersTaskResponseDTO.setStudyModelName(chapterStudyModelName);
                    String togetherChaptersId = collegeCourseChaptersTaskResponseDTO.getTogetherChaptersId();
                    String togetherChaptersName = "";
                    if(StringUtils.isNotEmpty(togetherChaptersId)){
                        togetherChaptersName = collegeCourseChaptersCfgServiceImpl.getChapterNameByChaptersId(Long.valueOf(togetherChaptersId));
                    }
                    collegeCourseChaptersTaskResponseDTO.setTogetherChaptersName(togetherChaptersName);
                    collegeCourseChaptersTaskResponseDTOList.add(collegeCourseChaptersTaskResponseDTO);
                }
            }
            collegeStudyTaskResponseDTO.setCollegeCourseChaptersList(collegeCourseChaptersTaskResponseDTOList);
        }
        return collegeStudyTaskResponseDTOIPage;
    }

    @Override
    public List<CollegeStudyTaskCfg> queryByStudyTaskIdList(List<Long> studyTaskIdList) {
        return this.list(Wrappers.<CollegeStudyTaskCfg>lambdaQuery().eq(CollegeStudyTaskCfg::getStatus, '0')
                .in(CollegeStudyTaskCfg::getStudyTaskId, studyTaskIdList));
    }

    @Override
    public CollegeStudyTaskCfg getEffectiveByStudyTaskId(Long studyTaskId) {
        return this.getOne(Wrappers.<CollegeStudyTaskCfg>lambdaQuery().eq(CollegeStudyTaskCfg::getStudyTaskId, studyTaskId)
                .eq(CollegeStudyTaskCfg::getStatus, "0"));
    }

    @Override
    public CollegeStudyTaskCfg getEffectiveByStudyId(String studyId) {
        return this.getOne(Wrappers.<CollegeStudyTaskCfg>lambdaQuery().eq(CollegeStudyTaskCfg::getStudyId, studyId)
                .eq(CollegeStudyTaskCfg::getStatus, "0"), false);
    }

    @Override
    public CollegeStudyTaskResponseDTO getCollegeStudyTaskByStudyTaskId(String studyTaskId) {
        LocalDateTime now = TimeUtils.getCurrentLocalDateTime();
        CollegeStudyTaskResponseDTO result = new CollegeStudyTaskResponseDTO();
        if (StringUtils.isNotEmpty(studyTaskId)){
            CollegeStudyTaskCfg collegeStudyTaskCfg = this.getEffectiveByStudyTaskId(Long.valueOf(studyTaskId));
            if (null != collegeStudyTaskCfg){
                BeanUtils.copyProperties(collegeStudyTaskCfg, result);
                String jobType = result.getJobType();
                String jobTypeName = "";
                if (StringUtils.isNotEmpty(jobType)){
                    jobTypeName = staticDataServiceImpl.getCodeName("JOB_TYPE", jobType);
                }
                if (StringUtils.isEmpty(jobTypeName)){
                    jobTypeName = jobType;
                }
                result.setJobTypeName(jobTypeName);

                String studyType = result.getStudyType();
                String studyTypeName = "";
                if (StringUtils.isNotEmpty(studyType)){
                    studyTypeName = staticDataServiceImpl.getCodeName("TASK_COURSEWARE_TYPE", studyType);
                }
                if (StringUtils.isEmpty(studyTypeName)){
                    studyTypeName = studyType;
                }
                result.setStudyTypeName(studyTypeName);

                String taskType = result.getTaskType();
                String taskTypeName = "";
                if (StringUtils.isNotEmpty(taskType)){
                    taskTypeName = staticDataServiceImpl.getCodeName("STUDY_TASK_TYPE", taskType);
                }
                if (StringUtils.isEmpty(taskTypeName)){
                    taskTypeName = taskType;
                }
                result.setTaskTypeName(taskTypeName);

                //设置任务分配详情
                //任务分配总数量
                Integer taskAllNum = 0;
                //任务分配有效数量
                Integer taskEffectiveNum = 0;
                //任务分配有效百分比
                Integer taskEffective = 0;
                //任务分配完成数量
                Integer taskFinishNum = 0;
                //任务分配完成百分比
                Integer taskFinish = 0;
                //任务分配延期数量
                Integer taskDelayNum = 0;
                //任务分配延期百分比
                Integer taskDelay = 0;
                //任务分配涉及员工数量
                Integer taskEmployeeNum = 0;
                //任务难度平均分
                Double argTaskDifficultyScore = 0.0;
                //老师平均分
                Double argTutorScore = 0.0;
                Integer allTaskDifficultyScore = 0;
                Integer allEmployeeScoreNum = 0;
                Integer allTutorScore = 0;
                List<CollegeEmployeeTask> collegeEmployeeTaskList = collegeEmployeeTaskServiceImpl.queryByStudyTaskId(studyTaskId);
                Map<String, String> employeeMap = new HashMap<>();
                if (ArrayUtils.isNotEmpty(collegeEmployeeTaskList)){
                    taskAllNum = collegeEmployeeTaskList.size();
                    for (CollegeEmployeeTask collegeEmployeeTask : collegeEmployeeTaskList) {
                        if (StringUtils.equals("0", collegeEmployeeTask.getStatus())){
                            taskEffectiveNum++;
                        }

                        if (null != collegeEmployeeTask.getTaskCompleteDate()){
                            taskFinishNum++;
                        }

                        if (TimeUtils.compareTwoTime(now, collegeEmployeeTask.getStudyEndDate()) > 0){
                            taskDelayNum++;
                        }
                        employeeMap.put(collegeEmployeeTask.getEmployeeId(), null);

                        Long taskId = collegeEmployeeTask.getTaskId();
                        CollegeStudyTaskScore studyScoreByTaskId = collegeStudyTaskScoreServiceImpl.getStudyScoreByTaskId(String.valueOf(taskId));
                        if (null != studyScoreByTaskId){
                            allTaskDifficultyScore += null != studyScoreByTaskId.getTaskDifficultyScore() ? studyScoreByTaskId.getTaskDifficultyScore() : 0;
                            allTutorScore += null != studyScoreByTaskId.getTutorScore() ? studyScoreByTaskId.getTutorScore() : 0;
                            allEmployeeScoreNum++;
                        }
                    }
                    if (null != employeeMap && employeeMap.size() > 0){
                        taskEmployeeNum = employeeMap.size();
                    }
                }
                result.setTaskAllNum(taskAllNum);
                result.setTaskEffectiveNum(taskEffectiveNum);
                result.setTaskFinishNum(taskFinishNum);
                result.setTaskDelayNum(taskDelayNum);
                result.setTaskEmployeeNum(taskEmployeeNum);

                if (taskAllNum > 0){
                    taskEffective = (taskEffectiveNum * 100) / taskAllNum;
                    taskFinish = (taskFinishNum * 100) / taskAllNum;
                    taskDelay = (taskDelayNum * 100) / taskAllNum;
                }
                result.setTaskEffective(taskEffective);
                result.setTaskFinish(taskFinish);
                result.setTaskDelay(taskDelay);

                if (0 != allEmployeeScoreNum){
                    argTaskDifficultyScore = new BigDecimal((float)allTaskDifficultyScore/allEmployeeScoreNum).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                    argTutorScore = new BigDecimal((float)allTutorScore/allEmployeeScoreNum).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                }
                result.setArgTaskDifficultyScore(argTaskDifficultyScore);
                result.setArgTutorScore(argTutorScore);

                //设置评分排名
                int count = this.count();
                Integer taskDifficultyScoreRanking = count;
                Integer tutorScoreRanking = count;
                String taskDifficultyScoreCxceedPercentage = "0%";
                String tutorScoreCxceedPercentage = "0%";
                if (argTaskDifficultyScore > 0 && argTutorScore > 0){
                    Map<String, Map<String, Integer>> taskDifficultyScoreMap = new HashMap<>();
                    Map<String, Double> taskDifficultyArgScoreMap = new HashMap<>();
                    Map<String, Map<String, Integer>> tutorScoreMap = new HashMap<>();
                    Map<String, Double> taskTutorArgScoreMap = new HashMap<>();
                    List<CollegeStudyTaskScore> collegeStudyTaskScores = collegeStudyTaskScoreServiceImpl.queryAllStudyScore();
                    if (ArrayUtils.isNotEmpty(collegeStudyTaskScores)){
                        for (CollegeStudyTaskScore collegeStudyTaskScore : collegeStudyTaskScores) {
                            String scoreStudyTaskId = collegeStudyTaskScore.getStudyTaskId();
                            Integer taskDifficultyScore = 0;
                            Integer employeeNum = 1;
                            Map<String, Integer> difficultyAllScoreMap = new HashMap<>();
                            if (taskDifficultyScoreMap.containsKey(scoreStudyTaskId)){
                                difficultyAllScoreMap = taskDifficultyScoreMap.get(scoreStudyTaskId);
                            }
                            if (difficultyAllScoreMap.containsKey("score")){
                                taskDifficultyScore = difficultyAllScoreMap.get("score") + collegeStudyTaskScore.getTaskDifficultyScore();
                            }else {
                                taskDifficultyScore = collegeStudyTaskScore.getTaskDifficultyScore();
                            }
                            if (difficultyAllScoreMap.containsKey("employeeNum")){
                                employeeNum = difficultyAllScoreMap.get("employeeNum") + 1;
                            }
                            difficultyAllScoreMap.put("score", taskDifficultyScore);
                            difficultyAllScoreMap.put("employeeNum", employeeNum);
                            taskDifficultyScoreMap.put(scoreStudyTaskId, difficultyAllScoreMap);


                            Map<String, Integer> tutorAllScoreMap = new HashMap<>();
                            Integer tutorScore = 0;
                            if (tutorScoreMap.containsKey(scoreStudyTaskId)){
                                tutorAllScoreMap = tutorScoreMap.get(scoreStudyTaskId);
                            }
                            if (tutorAllScoreMap.containsKey("score")){
                                tutorScore = tutorAllScoreMap.get("score") + collegeStudyTaskScore.getTutorScore();
                            }else {
                                tutorScore = collegeStudyTaskScore.getTutorScore();
                            }

                            tutorAllScoreMap.put("score", tutorScore);
                            tutorAllScoreMap.put("employeeNum", employeeNum);
                            tutorScoreMap.put(scoreStudyTaskId, tutorAllScoreMap);
                        }
                        if (null != taskDifficultyScoreMap && taskDifficultyScoreMap.size() > 0){
                            taskDifficultyScoreRanking = taskDifficultyScoreMap.size();
                            for (String key : taskDifficultyScoreMap.keySet()){
                                Map<String, Integer> map = taskDifficultyScoreMap.get(key);
                                Integer allScore = map.get("score");
                                Integer employeeNum = map.get("employeeNum");
                                Double argScore = new BigDecimal((float)allScore/employeeNum).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                                taskDifficultyArgScoreMap.put(key, argScore);
                                if (!StringUtils.equals(studyTaskId, key) && argTaskDifficultyScore >= argScore){
                                    taskDifficultyScoreRanking --;
                                }
                            }
                            taskDifficultyScoreCxceedPercentage = ((count - taskDifficultyScoreRanking + 1) * 100 / count) + "%";
                        }
                        if (null != tutorScoreMap && tutorScoreMap.size() > 0){
                            tutorScoreRanking = tutorScoreMap.size();
                            for (String key : tutorScoreMap.keySet()){
                                Map<String, Integer> map = tutorScoreMap.get(key);
                                Integer allScore = map.get("score");
                                Integer employeeNum = map.get("employeeNum");
                                Double argScore = new BigDecimal((float)allScore/employeeNum).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                                if (!StringUtils.equals(studyTaskId, key) && argTutorScore >= argScore){
                                    tutorScoreRanking --;
                                }
                                tutorScoreCxceedPercentage = ((count - tutorScoreRanking + 1) * 100 / count) + "%";
                            }
                        }
                    }
                }
                result.setTaskDifficultyScoreRanking(taskDifficultyScoreRanking);
                result.setTaskDifficultyScoreCxceedPercentage(taskDifficultyScoreCxceedPercentage);
                result.setTutorScoreRanking(tutorScoreRanking);
                result.setTutorScoreCxceedPercentage(tutorScoreCxceedPercentage);

                CollegeExamCfg collegeExamCfg = this.collegeExamCfgServiceImpl.getByStudyTaskIdAndExamType(studyTaskId, "1");
                Boolean examFlag = false;
                if (null != collegeExamCfg){
                    examFlag = true;
                    result.setMinNum(null != collegeExamCfg.getMinNum() ? collegeExamCfg.getMinNum() : 0);
                    result.setPassScore(null != collegeExamCfg.getPassScore() ? collegeExamCfg.getPassScore() : 0);
                    result.setExamMaxNum(null != collegeExamCfg.getExamMaxNum() ? collegeExamCfg.getExamMaxNum() : 0);
                    List<CollegeExamRelCfg> collegeExamRelCfgs = collegeExamRelCfgServiceImpl.queryByExamTopicId(collegeExamCfg.getExamTopicId());
                    if (ArrayUtils.isNotEmpty(collegeExamRelCfgs)){
                        List<ExamTopicResponseDTO> examTopicList = new ArrayList<>();
                        for (CollegeExamRelCfg collegeExamRelCfg : collegeExamRelCfgs) {
                            ExamTopicResponseDTO examTopicResponseDTO = new ExamTopicResponseDTO();
                            examTopicResponseDTO.setTopicNum(collegeExamRelCfg.getTopicNum());
                            String topicType = collegeExamRelCfg.getTopicType();
                            String topicTypeName = staticDataServiceImpl.getCodeName("EXERCISES_TYPE", topicType);
                            if (StringUtils.isEmpty(topicTypeName)){
                                topicTypeName = topicType;
                            }
                            examTopicResponseDTO.setTopicType(topicTypeName);
                            examTopicList.add(examTopicResponseDTO);
                        }
                        result.setExamTopicList(examTopicList);
                    }
                }
                result.setExamFlag(examFlag);

                CollegeExamCfg exercisesCfg = this.collegeExamCfgServiceImpl.getByStudyTaskIdAndExamType(studyTaskId, "0");
                Boolean exercisesFlag = false;
                if (null != exercisesCfg){
                    exercisesFlag = true;
                    result.setMinNum(null != exercisesCfg.getMinNum() ? exercisesCfg.getMinNum() : 0);
                    result.setPassScore(null != exercisesCfg.getPassScore() ? exercisesCfg.getPassScore() : 0);
                    result.setExamMaxNum(null != exercisesCfg.getExamMaxNum() ? exercisesCfg.getExamMaxNum() : 0);
                    List<CollegeExamRelCfg> collegeExamRelCfgs = collegeExamRelCfgServiceImpl.queryByExamTopicId(exercisesCfg.getExamTopicId());
                    if (ArrayUtils.isNotEmpty(collegeExamRelCfgs)){
                        List<ExamTopicResponseDTO> examTopicList = new ArrayList<>();
                        for (CollegeExamRelCfg collegeExamRelCfg : collegeExamRelCfgs) {
                            ExamTopicResponseDTO examTopicResponseDTO = new ExamTopicResponseDTO();
                            examTopicResponseDTO.setTopicNum(collegeExamRelCfg.getTopicNum());
                            String topicType = collegeExamRelCfg.getTopicType();
                            String topicTypeName = staticDataServiceImpl.getCodeName("EXERCISES_TYPE", topicType);
                            if (StringUtils.isEmpty(topicTypeName)){
                                topicTypeName = topicType;
                            }
                            examTopicResponseDTO.setTopicType(topicTypeName);
                            examTopicList.add(examTopicResponseDTO);
                        }
                        result.setExercisesTopicList(examTopicList);
                    }
                }
                result.setExercisesFlag(exercisesFlag);

                /*//设置章节信息
                List<CollegeCourseChaptersCfg> collegeCourseChaptersCfgList = collegeCourseChaptersCfgServiceImpl.queryByStudyId(result.getStudyId());
                List<CollegeCourseChaptersTaskResponseDTO> collegeCourseChaptersTaskResponseDTOList = new ArrayList<>();
                if (ArrayUtils.isNotEmpty(collegeCourseChaptersCfgList)){
                    for (CollegeCourseChaptersCfg collegeCourseChaptersCfg : collegeCourseChaptersCfgList){
                        CollegeCourseChaptersTaskResponseDTO collegeCourseChaptersTaskResponseDTO = new CollegeCourseChaptersTaskResponseDTO();
                        BeanUtils.copyProperties(collegeCourseChaptersCfg, collegeCourseChaptersTaskResponseDTO);
                        collegeCourseChaptersTaskResponseDTO.setStudyName(result.getStudyName());
                        String chaptersType = collegeCourseChaptersTaskResponseDTO.getChaptersType();
                        String chaptersTypeName = chaptersType;
                        if (StringUtils.isNotEmpty(chaptersType)){
                            chaptersTypeName = staticDataServiceImpl.getCodeName("CHAPTERS_TYPE", chaptersType);
                        }
                        collegeCourseChaptersTaskResponseDTO.setChaptersTypeName(chaptersTypeName);
                        String studyModel = collegeCourseChaptersTaskResponseDTO.getStudyModel();
                        String studyModelName = studyModel;
                        if (StringUtils.isNotEmpty(studyModel)){
                            studyModelName = staticDataServiceImpl.getCodeName("STUDY_MODEL", studyModel);
                        }
                        collegeCourseChaptersTaskResponseDTO.setStudyModelName(studyModelName);
                        String togetherChaptersId = collegeCourseChaptersTaskResponseDTO.getTogetherChaptersId();
                        String togetherChaptersName = "";
                        if(StringUtils.isNotEmpty(togetherChaptersId)){
                            togetherChaptersName = collegeCourseChaptersCfgServiceImpl.getChapterNameByChaptersId(Long.valueOf(togetherChaptersId));
                        }
                        collegeCourseChaptersTaskResponseDTO.setTogetherChaptersName(togetherChaptersName);
                        collegeCourseChaptersTaskResponseDTOList.add(collegeCourseChaptersTaskResponseDTO);
                    }
                }
                result.setCollegeCourseChaptersList(collegeCourseChaptersTaskResponseDTOList);*/
            }
        }
        return result;
    }

    @Override
    public List<CollegeTogetherStudyTaskResponseDTO> queryEffectiveTogetherStudyTaskList() {
        List<CollegeTogetherStudyTaskResponseDTO> result = new ArrayList<>();
        List<CollegeStudyTaskCfg> collegeStudyTaskCfgList = this.list(Wrappers.<CollegeStudyTaskCfg>lambdaQuery().eq(CollegeStudyTaskCfg::getStatus, "0")
                .orderByAsc(CollegeStudyTaskCfg::getStudyTaskId));
        if (ArrayUtils.isNotEmpty(collegeStudyTaskCfgList)){
            for (CollegeStudyTaskCfg collegeStudyTaskCfg : collegeStudyTaskCfgList){
                CollegeTogetherStudyTaskResponseDTO collegeTogetherStudyTaskResponseDTO = new CollegeTogetherStudyTaskResponseDTO();
                BeanUtils.copyProperties(collegeStudyTaskCfg, collegeTogetherStudyTaskResponseDTO);
                collegeTogetherStudyTaskResponseDTO.setStudyTaskId(String.valueOf(collegeStudyTaskCfg.getStudyTaskId()));
                result.add(collegeTogetherStudyTaskResponseDTO);
            }
        }
        return result;
    }

    @Override
    public CollegeStudyTaskCfg getAllByStudyTaskId(Long studyTaskId) {
        return this.getOne(Wrappers.<CollegeStudyTaskCfg>lambdaQuery().eq(CollegeStudyTaskCfg::getStudyTaskId, studyTaskId));
    }

    @Override
    public List<CollegeStudyTaskCfg> queryEffectiveReleased() {
        return this.list(Wrappers.<CollegeStudyTaskCfg>lambdaQuery().eq(CollegeStudyTaskCfg::getStatus, '0')
                .in(CollegeStudyTaskCfg::getReleaseStatus, '1')
                .orderByAsc(CollegeStudyTaskCfg::getStudyTaskId));
    }

    @Override
    public List<CollegeStudyTaskCfg> queryByStudyType(String studyType) {
        return this.list(Wrappers.<CollegeStudyTaskCfg>lambdaQuery()
                .eq(CollegeStudyTaskCfg::getStudyType, studyType)
                .orderByDesc(CollegeStudyTaskCfg::getStudyTaskId));
    }
}