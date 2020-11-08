package com.microtomato.hirun.modules.college.task.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeReleaseExamTaskDTO;
import com.microtomato.hirun.modules.college.config.entity.po.*;
import com.microtomato.hirun.modules.college.config.service.*;
import com.microtomato.hirun.modules.college.task.entity.dto.*;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTask;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTaskScore;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTaskTutor;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeStudyTopicRel;
import com.microtomato.hirun.modules.college.task.service.*;
import com.microtomato.hirun.modules.college.topic.entity.dto.TopicServiceDTO;
import com.microtomato.hirun.modules.college.topic.entity.po.CollegeTopicLabelRel;
import com.microtomato.hirun.modules.college.topic.service.ICollegeTopicLabelRelService;
import com.microtomato.hirun.modules.college.topic.service.IExamTopicService;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.service.IEmployeeJobRoleService;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.ListUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private ICollegeEmployeeTaskScoreService collegeEmployeeTaskScoreServiceImpl;
    
    @Autowired
    private IEmployeeJobRoleService employeeJobRoleServiceImpl;
    
    @Autowired
    private IOrgService orgServiceImpl;

    @Autowired
    private ICollegeStudyTopicRelService collegeStudyTopicRelService;

    @Autowired
    private ICollegeTopicLabelRelService collegeTopicLabelRelService;

    @Autowired
    private IExamTopicService examTopicService;

    @Autowired
    private ICollegeExamCfgService collegeExamCfgServiceImpl;

    @Autowired
    private ICollegeTaskExperienceService collegeTaskExperienceServiceImpl;

    @Autowired
    private ICollegeEmployeeTaskService collegeEmployeeTaskServiceImpl;

    @Autowired
    private ICollegeTaskEmployeeCfgService collegeTaskEmployeeCfgServiceImpl;

    @Autowired
    private ICollegeTaskJobCfgService collegeTaskJobCfgServiceImpl;

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
            return employeeServiceImpl.getEmployeeByEmployeeId(Long.valueOf(employeeId));
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
        int evaluateTaskNum = 0;
        int allScore = 0;
        int allStudyScore = 0;
        int allExercisesScore = 0;
        int allExamScore = 0;
        if (ArrayUtils.isNotEmpty(collegeEmployeeTaskList)){
            allTaskNum = collegeEmployeeTaskList.size();
            for (CollegeEmployeeTask collegeEmployeeTask : collegeEmployeeTaskList){
                String studyTaskId = collegeEmployeeTask.getStudyTaskId();
                CollegeStudyTaskCfg collegeStudyTask = collegeStudyTaskCfgServiceImpl.getAllByStudyTaskId(Long.valueOf(studyTaskId));
                LocalDateTime taskCompleteDate = collegeEmployeeTask.getTaskCompleteDate();
                if (null != taskCompleteDate){
                    taskFinishNum++;
                }

                CollegeExamCfg exercisesCfg = collegeExamCfgServiceImpl.getByStudyTaskIdAndExamType(studyTaskId, "0");
                //是否需要考试
                if (null != exercisesCfg){
                    //有练习
                    Integer exercisesNumber = exercisesCfg.getExamMaxNum();
                    if (null != exercisesNumber){
                        //有练习次数要求，判断练习次数
                        Integer exercisesCompletedNumber = collegeEmployeeTask.getExercisesCompletedNumber();
                        if (exercisesCompletedNumber >= exercisesNumber){
                            exercisesFinishNum++;
                        }
                    }else {
                        //无练习次数要求，默认为完成
                        exercisesFinishNum++;
                    }
                }else {
                    //无练习，默认为完成
                    exercisesFinishNum++;
                }
                //是否需要考试
                CollegeExamCfg examCfg = collegeExamCfgServiceImpl.getByStudyTaskIdAndExamType(studyTaskId, "1");

                if (null != examCfg){
                    //有考试，判断考试分数是否合格
                    Integer examScore = collegeEmployeeTask.getExamScore();
                    if (null != examScore){
                        Integer passScore = exercisesCfg.getPassScore();
                        if (null != passScore){
                            if (null != examScore && examScore > passScore){
                                examPassNum++;
                            }
                        }
                    }
                }else {
                    //无考试，默认合格
                    examPassNum++;
                }

                Integer score = collegeEmployeeTask.getScore();
                if (null != score){
                    evaluateTaskNum++;
                    allScore += score;
                    CollegeEmployeeTaskScore collegeEmployeeTaskScore = collegeEmployeeTaskScoreServiceImpl.getByTaskId(String.valueOf(collegeEmployeeTask.getTaskId()));
                    if (null != collegeEmployeeTaskScore){
                        if (!StringUtils.equals("2", collegeStudyTask.getStudyType())){
                            Integer examEvaluate = collegeEmployeeTaskScore.getExamScore();
                            allExamScore += examEvaluate;
                            Integer exercisesEvaluate = collegeEmployeeTaskScore.getExercisesScore();
                            allExercisesScore += exercisesEvaluate;
                            Integer studyEvaluate = collegeEmployeeTaskScore.getStudyScore();
                            allStudyScore += studyEvaluate;
                        }
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

        //查询评分信息
        Double argTaskScore = 0.0;
        Double argExercisesScore = 0.0;
        Double argStudyScore = 0.0;
        Double argExamScore = 0.0;
        if (evaluateTaskNum > 0){
            argTaskScore = new BigDecimal((float)allScore/evaluateTaskNum).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            argExercisesScore = new BigDecimal((float)allExercisesScore/evaluateTaskNum).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            argStudyScore = new BigDecimal((float)allStudyScore/evaluateTaskNum).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            argExamScore = new BigDecimal((float)allExamScore/evaluateTaskNum).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        result.setArgExamScore(argExamScore);
        result.setArgExercisesScore(argExercisesScore);
        result.setArgStudyScore(argStudyScore);
        result.setArgTaskScore(argTaskScore);

        //设置全部员工评分比较
        CollegeEmployeeTaskScoreDTO allEmployeeTaskScoreInfo = new CollegeEmployeeTaskScoreDTO();
        List<CollegeEmployeeTask> allCollegeEmployeeTaskList = collegeEmployeeTaskService.queryAllEffective();
        Map<String, List<Double>> employeeTaskScore = getEmployeeTaskScore(allCollegeEmployeeTaskList, employeeId);
        List<Double> allArgTaskScoreList = employeeTaskScore.get("argTaskScore");
        int allTaskScoreRanking = getScoreRanking(allArgTaskScoreList, argTaskScore);
        allEmployeeTaskScoreInfo.setEmployeeTaskScoreRanking(allTaskScoreRanking);
        //计算百分比
        String allEmployeeTaskScoreCxceedPercentage = "100";
        if (ArrayUtils.isNotEmpty(allArgTaskScoreList)){
            if(allTaskScoreRanking > 1){
                allEmployeeTaskScoreCxceedPercentage = String.valueOf(new BigDecimal((float) (allArgTaskScoreList.size() + 1 - allTaskScoreRanking) * 100 / allArgTaskScoreList.size()).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
            }
        }
        allEmployeeTaskScoreInfo.setEmployeeTaskScoreCxceedPercentage(allEmployeeTaskScoreCxceedPercentage + "%");
        List<Double> allArgExercisesScoreList = employeeTaskScore.get("argExercisesScore");
        int allExercisesRanking = getScoreRanking(allArgExercisesScoreList, argExercisesScore);
        allEmployeeTaskScoreInfo.setEmployeeExercisesScoreRanking(allExercisesRanking);
        //计算百分比
        String allEmployeeExercisesScoreCxceedPercentage = "100";
        if (ArrayUtils.isNotEmpty(allArgExercisesScoreList)){
            if (allExercisesRanking > 1){
                allEmployeeExercisesScoreCxceedPercentage = String.valueOf(new BigDecimal((float) (allArgExercisesScoreList.size() + 1 - allExercisesRanking) * 100 / allArgExercisesScoreList.size()).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
            }
        }
        allEmployeeTaskScoreInfo.setEmployeeExercisesScoreCxceedPercentage(allEmployeeExercisesScoreCxceedPercentage + "%");
        List<Double> allArgStudyScoreList = employeeTaskScore.get("argStudyScore");
        int allStudyScoreRanking = getScoreRanking(allArgStudyScoreList, argStudyScore);
        allEmployeeTaskScoreInfo.setEmployeeStudyScoreRanking(allStudyScoreRanking);
        //计算百分比
        String allEmployeeStudyScoreCxceedPercentage = "100";
        if (ArrayUtils.isNotEmpty(allArgStudyScoreList)){
            if (allStudyScoreRanking > 1){
                allEmployeeStudyScoreCxceedPercentage = String.valueOf(new BigDecimal((float) (allArgStudyScoreList.size() + 1 - allStudyScoreRanking) * 100 / allArgStudyScoreList.size()).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
            }
        }
        allEmployeeTaskScoreInfo.setEmployeeStudyScoreCxceedPercentage(allEmployeeStudyScoreCxceedPercentage + "%");
        List<Double> allArgExamScoreList = employeeTaskScore.get("argExamScore");
        int allExamScoreRanking = getScoreRanking(allArgExamScoreList, argExamScore);
        allEmployeeTaskScoreInfo.setEmployeeExamScoreRanking(allExamScoreRanking);
        //计算百分比
        String allEmployeeExamScoreCxceedPercentage = "100";
        if (ArrayUtils.isNotEmpty(allArgExamScoreList)){
            if (allExamScoreRanking > 1){
                allEmployeeExamScoreCxceedPercentage = String.valueOf(new BigDecimal((float) (allArgExamScoreList.size() + 1 - allExamScoreRanking) * 100 / allArgExamScoreList.size()).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
            }
        }
        allEmployeeTaskScoreInfo.setEmployeeExamScoreCxceedPercentage(allEmployeeExamScoreCxceedPercentage + "%");
        result.setAllEmployeeTaskScoreInfo(allEmployeeTaskScoreInfo);
        //设置本部门员工评分比较

        EmployeeJobRole employeeJobRole = employeeJobRoleServiceImpl.queryValidMain(Long.valueOf(employeeId));
        Long orgId = employeeJobRole.getOrgId();
        List<Long> orgIdList = new ArrayList<>();
        if (null != orgId){
            Org org = orgServiceImpl.queryByOrgId(orgId);
            if (null != org){
                orgIdList.add(org.getOrgId());
                List<Org> children = orgServiceImpl.findChildren(org);
                if (ArrayUtils.isNotEmpty(children)){
                    for (Org childrenOrg : children){
                        orgIdList.add(childrenOrg.getOrgId());
                    }
                }
            }
        }
        List<String> employeeIdList = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(orgIdList)){
            List<EmployeeJobRole> employeeJobRoleList = employeeJobRoleServiceImpl.queryEffectiveByOrgIdList(orgIdList);
            if(ArrayUtils.isNotEmpty(employeeJobRoleList)){
                for (EmployeeJobRole item : employeeJobRoleList){
                    employeeIdList.add(String.valueOf(item.getEmployeeId()));
                }
            }
        }
        CollegeEmployeeTaskScoreDTO orgEmployeeTaskScoreInfo = new CollegeEmployeeTaskScoreDTO();
        List<CollegeEmployeeTask> orgCollegeEmployeeTaskList = collegeEmployeeTaskService.queryEffectiveByEmployeeIdList(employeeIdList);
        Map<String, List<Double>> orgEmployeeTaskScore = getEmployeeTaskScore(orgCollegeEmployeeTaskList, employeeId);
        List<Double> orgArgTaskScoreList = orgEmployeeTaskScore.get("argTaskScore");
        int orgTaskScoreRanking = getScoreRanking(orgArgTaskScoreList, argTaskScore);
        orgEmployeeTaskScoreInfo.setEmployeeTaskScoreRanking(orgTaskScoreRanking);
        //计算百分比
        String orgEmployeeTaskScoreCxceedPercentage = "100";
        if (ArrayUtils.isNotEmpty(orgArgTaskScoreList)){
            if(orgTaskScoreRanking > 1){
                orgEmployeeTaskScoreCxceedPercentage = String.valueOf(new BigDecimal((float) (orgArgTaskScoreList.size() + 1 - orgTaskScoreRanking) * 100 / orgArgTaskScoreList.size()).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
            }
        }
        orgEmployeeTaskScoreInfo.setEmployeeTaskScoreCxceedPercentage(orgEmployeeTaskScoreCxceedPercentage + "%");
        List<Double> orgArgExercisesScoreList = orgEmployeeTaskScore.get("argExercisesScore");
        int orgExercisesRanking = getScoreRanking(orgArgExercisesScoreList, argExercisesScore);
        orgEmployeeTaskScoreInfo.setEmployeeExercisesScoreRanking(orgExercisesRanking);
        //计算百分比
        String orgEmployeeExercisesScoreCxceedPercentage = "100";
        if (ArrayUtils.isNotEmpty(orgArgExercisesScoreList)){
            if (orgExercisesRanking > 1){
                orgEmployeeExercisesScoreCxceedPercentage = String.valueOf(new BigDecimal((float) (orgArgExercisesScoreList.size() + 1 - orgExercisesRanking) * 100 / orgArgExercisesScoreList.size()).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
            }
        }
        orgEmployeeTaskScoreInfo.setEmployeeExercisesScoreCxceedPercentage(orgEmployeeExercisesScoreCxceedPercentage + "%");
        List<Double> orgArgStudyScoreList = orgEmployeeTaskScore.get("argStudyScore");
        int orgStudyScoreRanking = getScoreRanking(orgArgStudyScoreList, argStudyScore);
        orgEmployeeTaskScoreInfo.setEmployeeStudyScoreRanking(orgStudyScoreRanking);
        //计算百分比
        String orgEmployeeStudyScoreCxceedPercentage = "100";
        if (ArrayUtils.isNotEmpty(orgArgStudyScoreList)){
            if (orgStudyScoreRanking > 1){
                orgEmployeeStudyScoreCxceedPercentage = String.valueOf(new BigDecimal((float) (orgArgStudyScoreList.size() + 1 - orgStudyScoreRanking) * 100 / orgArgStudyScoreList.size()).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
            }
        }
        orgEmployeeTaskScoreInfo.setEmployeeStudyScoreCxceedPercentage(orgEmployeeStudyScoreCxceedPercentage + "%");
        List<Double> orgArgExamScoreList = orgEmployeeTaskScore.get("argExamScore");
        int orgExamScoreRanking = getScoreRanking(orgArgExamScoreList, argExamScore);
        orgEmployeeTaskScoreInfo.setEmployeeExamScoreRanking(orgExamScoreRanking);
        //计算百分比
        String orgEmployeeExamScoreCxceedPercentage = "100";
        if (ArrayUtils.isNotEmpty(orgArgExamScoreList)){
            if (orgExamScoreRanking > 1){
                orgEmployeeExamScoreCxceedPercentage = String.valueOf(new BigDecimal((float) (orgArgExamScoreList.size() + 1 - orgExamScoreRanking) * 100 / orgArgExamScoreList.size()).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
            }
        }
        orgEmployeeTaskScoreInfo.setEmployeeExamScoreCxceedPercentage(orgEmployeeExamScoreCxceedPercentage + "%");
        result.setOrgEmployeeTaskScoreInfo(orgEmployeeTaskScoreInfo);
        return result;
    }

    //获取分数排名
    private int getScoreRanking(List<Double> scoreList, Double currentScore){
        int result = 1;
        if (ArrayUtils.isNotEmpty(scoreList)){
            result = scoreList.size() + 1;
            for (int i = 0; i < scoreList.size(); i++){
                if (currentScore > scoreList.get(i)){
                    result--;
                }else {
                    break;
                }
            }
        }
        return result;
    }

    private Map<String, List<Double>> getEmployeeTaskScore(List<CollegeEmployeeTask> allCollegeEmployeeTaskList, String currentEmployeeId){
        Map<String, List<Double>> result = new HashMap<>();
        List<Double> argTaskScoreList = new ArrayList<>();
        List<Double> argExercisesScoreList = new ArrayList<>();
        List<Double> argStudyScoreList = new ArrayList<>();
        List<Double> argExamScoreList = new ArrayList<>();
        Map<String, List<CollegeEmployeeTask>> employeeTaskMap = new HashMap<>();
        if (ArrayUtils.isNotEmpty(allCollegeEmployeeTaskList)){
            for (CollegeEmployeeTask collegeEmployeeTask : allCollegeEmployeeTaskList){
                String employeeId = collegeEmployeeTask.getEmployeeId();
                //如果是员工本身，则过滤
                if (StringUtils.equals(currentEmployeeId, employeeId)){
                    continue;
                }
                List<CollegeEmployeeTask> collegeEmployeeTaskList = new ArrayList<>();
                if (employeeTaskMap.containsKey(employeeId)){
                    collegeEmployeeTaskList = employeeTaskMap.get(employeeId);
                }
                collegeEmployeeTaskList.add(collegeEmployeeTask);
                employeeTaskMap.put(employeeId, collegeEmployeeTaskList);
            }
        }
        if (null != employeeTaskMap && employeeTaskMap.size() > 0){
            for (String employeeId : employeeTaskMap.keySet()){
                List<CollegeEmployeeTask> collegeEmployeeTaskList = employeeTaskMap.get(employeeId);
                if (ArrayUtils.isNotEmpty(collegeEmployeeTaskList)){
                    //查询评分信息
                    int evaluateTaskNum = 0;
                    int allScore = 0;
                    int allStudyScore = 0;
                    int allExercisesScore = 0;
                    int allExamScore = 0;
                    for (CollegeEmployeeTask collegeEmployeeTask : collegeEmployeeTaskList){
                        String studyTaskId = collegeEmployeeTask.getStudyTaskId();
                        CollegeStudyTaskCfg collegeStudyTask = collegeStudyTaskCfgServiceImpl.getAllByStudyTaskId(Long.valueOf(studyTaskId));
                        Integer score = collegeEmployeeTask.getScore();
                        if (null != score){
                            evaluateTaskNum++;
                            allScore += score;
                            CollegeEmployeeTaskScore collegeEmployeeTaskScore = collegeEmployeeTaskScoreServiceImpl.getByTaskId(String.valueOf(collegeEmployeeTask.getTaskId()));
                            if (null != collegeEmployeeTaskScore){
                                Integer examEvaluate = collegeEmployeeTaskScore.getExamScore();
                                allExamScore += examEvaluate;
                                Integer exercisesEvaluate = collegeEmployeeTaskScore.getExercisesScore();
                                allExercisesScore += exercisesEvaluate;
                                Integer studyEvaluate = collegeEmployeeTaskScore.getStudyScore();
                                allStudyScore += studyEvaluate;
                            }
                        }
                    }
                    Double argTaskScore = 0.0;
                    Double argExercisesScore = 0.0;
                    Double argStudyScore = 0.0;
                    Double argExamScore = 0.0;
                    if (evaluateTaskNum > 0){
                        argTaskScore = new BigDecimal((float)allScore/evaluateTaskNum).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                        argExercisesScore = new BigDecimal((float)allExercisesScore/evaluateTaskNum).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                        argStudyScore = new BigDecimal((float)allStudyScore/evaluateTaskNum).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                        argExamScore = new BigDecimal((float)allExamScore/evaluateTaskNum).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                    }
                    argTaskScoreList.add(argTaskScore);
                    argExercisesScoreList.add(argExercisesScore);
                    argStudyScoreList.add(argStudyScore);
                    argExamScoreList.add(argExamScore);
                }
            }
        }
        result.put("argTaskScore", ListUtils.sort(argTaskScoreList));
        result.put("argExercisesScore", ListUtils.sort(argExercisesScoreList));
        result.put("argStudyScore", ListUtils.sort(argStudyScoreList));
        result.put("argExamScore", ListUtils.sort(argExamScoreList));
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

    @PostMapping("taskReleaseByTaskList")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void taskReleaseByTaskList(@RequestBody List<Long> studyTaskIdList){
        this.taskDomainOpenServiceImpl.taskReleaseByTaskList(studyTaskIdList);
        //任务发布后，分配给对应员工
        LocalDateTime now = LocalDateTime.now();
        List<CollegeStudyTaskCfg> collegeStudyTaskCfgList = collegeStudyTaskCfgServiceImpl.queryByStudyTaskIdList(studyTaskIdList);
        if (ArrayUtils.isNotEmpty(collegeStudyTaskCfgList)){
            List<CollegeStudyTaskCfg> allTaskList = new ArrayList<>();
            for (CollegeStudyTaskCfg collegeStudyTaskCfg : collegeStudyTaskCfgList) {
                String taskType = collegeStudyTaskCfg.getTaskType();
                //答题任务单独设置
                if (!StringUtils.equals("3", collegeStudyTaskCfg.getStudyType())) {
                    if (StringUtils.equals("1", taskType)) {
                        allTaskList.add(collegeStudyTaskCfg);
                    } else if (StringUtils.equals("2", taskType)) {
                        //活动任务判断有效期
                        LocalDateTime taskReleaseDate = collegeStudyTaskCfg.getTaskReleaseDate();
                        Integer taskValidityTerm = collegeStudyTaskCfg.getTaskValidityTerm();
                        //如果在有效期内
                        if (TimeUtils.compareTwoTime(now, TimeUtils.addDays(taskReleaseDate, taskValidityTerm)) < 0) {
                            allTaskList.add(collegeStudyTaskCfg);
                        }
                    }
                }
            }
            if (ArrayUtils.isNotEmpty(allTaskList)) {
                for (int i = 0; i < allTaskList.size(); i++) {
                    CollegeStudyTaskCfg collegeStudyTaskCfg = allTaskList.get(i);
                    String jobType = collegeStudyTaskCfg.getJobType();
                    Long studyTaskId = collegeStudyTaskCfg.getStudyTaskId();
                    //所有员工
                    List<Long> employeeIdList = new ArrayList<>();
                    if (StringUtils.equals("2", jobType)) {
                        List<Employee> employeeList = employeeServiceImpl.queryAllEffectiveEmployee();
                        if (ArrayUtils.isNotEmpty(employeeList)) {
                            for (Employee employee : employeeList) {
                                Long employeeId = employee.getEmployeeId();
                                employeeIdList.add(employeeId);
                            }
                        }
                    } else if (StringUtils.equals("3", jobType)) {
                        //指定员工
                        List<CollegeTaskEmployeeCfg> employeeCfgList = collegeTaskEmployeeCfgServiceImpl.queryEffectiveByTaskId(String.valueOf(studyTaskId));
                        if (ArrayUtils.isNotEmpty(employeeCfgList)) {
                            for (CollegeTaskEmployeeCfg collegeTaskEmployeeCfg : employeeCfgList) {
                                employeeIdList.add(Long.valueOf(collegeTaskEmployeeCfg.getEmployeeId()));
                            }
                        }

                    } else {
                        List<CollegeTaskJobCfg> collegeTaskJobCfgList = collegeTaskJobCfgServiceImpl.queryEffectiveByTaskId(String.valueOf(studyTaskId));
                        if (ArrayUtils.isNotEmpty(collegeTaskJobCfgList)) {
                            List<String> jobRoleList = new ArrayList<>();
                            for (CollegeTaskJobCfg collegeTaskJobCfg : collegeTaskJobCfgList) {
                                String jobRole = collegeTaskJobCfg.getJobRole();
                                jobRoleList.add(jobRole);
                            }
                            if (ArrayUtils.isNotEmpty(jobRoleList)) {
                                List<Employee> employeeList = new ArrayList<>();
                                if (StringUtils.equals("0", jobType)) {
                                    //正式员工
                                    employeeList = employeeServiceImpl.queryEffectiveByJobRoleList(jobRoleList);
                                } else if (StringUtils.equals("1", jobType)) {
                                    //新员工
                                    employeeList = employeeServiceImpl.queryNewEffectiveByJobRoleList(jobRoleList);
                                }
                                if (ArrayUtils.isNotEmpty(employeeList) && employeeList.size() > 0) {
                                    for (Employee employee : employeeList) {
                                        Long employeeId = employee.getEmployeeId();
                                        employeeIdList.add(employeeId);
                                    }
                                }
                            }
                        }
                    }
                    if (ArrayUtils.isNotEmpty(employeeIdList)) {
                        List<CollegeEmployeeTask> collegeEmployeeTaskList = new ArrayList<>();
                        for (Long employeeId : employeeIdList) {
                            CollegeEmployeeTask employeeTask = collegeEmployeeTaskServiceImpl.getLastEffectiveByEmployeeIdAndStudyTaskId(String.valueOf(employeeId), String.valueOf(studyTaskId));
                            //如果该员工分配了该项任务，则跳过
                            if (null != employeeTask) {
                                continue;
                            }
                            String studyStartType = collegeStudyTaskCfg.getStudyStartType();
                            LocalDateTime startDate = now;
                            LocalDateTime endDate = now;
                            if (StringUtils.equals("3", studyStartType)) {
                                String studyModel = collegeStudyTaskCfg.getStudyModel();
                                if (StringUtils.equals("1", studyModel)) {
                                    //如果是同时学习
                                    String togetherStudyTaskId = collegeStudyTaskCfg.getTogetherStudyTaskId();
                                    CollegeEmployeeTask togetherCollegeEmployeeTask = collegeEmployeeTaskServiceImpl.getLastEffectiveByEmployeeIdAndStudyTaskId(String.valueOf(employeeId), togetherStudyTaskId);
                                    if (null != togetherCollegeEmployeeTask) {
                                        startDate = togetherCollegeEmployeeTask.getStudyStartDate();
                                    } else {
                                        CollegeEmployeeTask collegeEmployeeTask = getTogetherStudyTaskByStudyTaskIdAndEmployeeId(collegeEmployeeTaskList, String.valueOf(employeeId), togetherStudyTaskId);
                                        if (null != collegeEmployeeTask) {
                                            startDate = collegeEmployeeTask.getStudyStartDate();
                                        }
                                    }
                                } else if (StringUtils.equals("0", studyModel)) {
                                    //如果是顺序学习
                                    //查询员工最后一次学习任务
                                    CollegeEmployeeTask collegeEmployeeTask = collegeEmployeeTaskServiceImpl.getLastEffectiveByEmployeeId(String.valueOf(employeeId));
                                    if (null != collegeEmployeeTask) {
                                        LocalDateTime togetherTaskEndDate = collegeEmployeeTask.getStudyEndDate();
                                        //如果同时学习开始时间小于当前时间，则立即后一天开始
                                        if (TimeUtils.compareTwoTime(togetherTaskEndDate, now) < 0) {
                                            startDate = TimeUtils.getFirstSecondDay(now, 1);
                                        } else {
                                            //否则取最后一次任务结束时间后一天第一秒
                                            startDate = TimeUtils.getFirstSecondDay(togetherTaskEndDate, 1);
                                        }
                                    } else {
                                        //如果员工没有任务
                                        startDate = TimeUtils.getFirstSecondDay(now, 1);
                                    }
                                }

                            } else {
                                if (StringUtils.equals("1", studyStartType)) {
                                    //立即开始
                                    startDate = TimeUtils.getFirstSecondDay(now, 1);
                                } else if (StringUtils.equals("2", studyStartType)) {
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
                        if (ArrayUtils.isNotEmpty(collegeEmployeeTaskList)) {
                            collegeEmployeeTaskServiceImpl.saveBatch(collegeEmployeeTaskList);
                        }
                    }
                }
            }
        }
    }

    @GetMapping("queryLoginEmployeeTaskInfo")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public CollegeLoginTaskInfoResponseDTO queryLoginEmployeeTaskInfo() {
        return this.collegeEmployeeTaskService.queryLoginEmployeeTaskInfo();
    }

    @GetMapping("queryEmployTaskInfoByTaskId")
    @RestResult
    public CollegeEmployTaskInfoResponseDTO queryEmployTaskInfoByTaskId(@RequestParam("taskId") Long taskId){
        return this.collegeEmployeeTaskService.queryEmployTaskInfoByTaskId(taskId);
    }

    @GetMapping("queryLoginEmployeeSelectTutor")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public List<CollegeEmployeeTaskTutorOptionsDTO> queryLoginEmployeeSelectTutor(){
        return this.collegeEmployeeTaskService.queryLoginEmployeeSelectTutor();
    }

    @Autowired
    private ICollegeExamCfgService collegeExamCfgService;

    @Autowired
    private ICollegeExamRelCfgService collegeExamRelCfgService;

    /**
     * 获取任务考试习题
     * @param taskId
     * @param scoreType
     * @return
     */
    @GetMapping("queryTopicByTaskId")
    @RestResult
    public CollegeEmployeeTaskTopicDTO queryTopicByTaskId(@RequestParam("taskId") Long taskId, @RequestParam("scoreType") String scoreType) {
        CollegeEmployeeTaskTopicDTO response = new CollegeEmployeeTaskTopicDTO();
        // 获取studyTaskId
        CollegeEmployeeTask employeeTask = collegeEmployeeTaskService.getById(taskId);
        String studyTaskId = null != employeeTask ? employeeTask.getStudyTaskId() : "";
        // 获取studyId
        if (StringUtils.isBlank(studyTaskId)) {
            return response;
        }
        // 获取考试习题配置
        CollegeExamCfg examCfg = collegeExamCfgService.getByStudyTaskIdAndExamType(studyTaskId, scoreType);
        if (Objects.isNull(examCfg)) {
            return response;
        }
        List<CollegeExamRelCfg> examRels = collegeExamRelCfgService.queryExamRelInfo(examCfg.getExamTopicId());
        if (ArrayUtils.isEmpty(examRels)) {
            return response;
        }
        Integer passScore = examCfg.getPassScore();
        if (null == passScore){
            passScore = 80;
        }
        response.setPassScore(passScore);
        Integer examTime = examCfg.getExamTime();
        if (null == examTime){
            examTime = 60;
        }
        response.setTaskTimeLen(examTime);

        // 获取考试范围
        CollegeStudyTaskCfg studyTask = collegeStudyTaskCfgServiceImpl.getEffectiveByStudyTaskId(Long.parseLong(studyTaskId));
        String studyId = StringUtils.isNotBlank(studyTask.getStudyId()) ? studyTask.getStudyId() : "";
        // 获取labelId
        if (StringUtils.isBlank(studyId)) {
            return response;
        }
        List<CollegeTopicLabelRel> topicRels = new ArrayList<>();
        List<CollegeStudyTopicRel> studyTopicList = collegeStudyTopicRelService.getEffectiveByStudyId(studyId);
        if (ArrayUtils.isNotEmpty(studyTopicList)){
            for (CollegeStudyTopicRel collegeStudyTopicRel : studyTopicList) {
                Long labelId = collegeStudyTopicRel.getLabelId();
                // 获取topic
                List<CollegeTopicLabelRel> collegeTopicLabelRels = collegeTopicLabelRelService.queryEffectiveByLabelId(labelId);
                if (ArrayUtils.isNotEmpty(collegeTopicLabelRels)) {
                    topicRels.addAll(collegeTopicLabelRels);
                }
            }
        }
        if (ArrayUtils.isEmpty(topicRels)){
            return response;
        }

        // 获取topicIds,需要根据任务的习题数量配置以及标签获取
        List<Long> topicIds = new ArrayList<>();
        topicRels.stream().forEach(x -> {
            topicIds.add(x.getTopicId());
        });
        List<TopicServiceDTO> topicServices = examTopicService.queryByTopicIds(topicIds);
        // 根据配置筛选习题
        List<TopicServiceDTO> topics = filterTopic(examRels, topicServices);

        setNum(topics);
        response.setTaskId(taskId);
        response.setTaskTopics(topics);
        return response;
    }

    /**
     * 获取登陆员工点评任务
     * @return
     */
    @GetMapping("queryLoginEmployeeCommentTaskInfo")
    @RestResult
    public CollegeLoginCommentTaskInfoResponseDTO queryLoginEmployeeCommentTaskInfo() {
        CollegeLoginCommentTaskInfoResponseDTO result = new CollegeLoginCommentTaskInfoResponseDTO();
        List<CollegeEmployeeTaskTutor> collegeEmployeeTaskTutors = this.collegeEmployeeTaskService.queryLoginEmployeeCommentTaskInfo();
        List<CollegeTaskCommentDetailResponseDTO> noComment = new ArrayList<>();
        List<CollegeTaskCommentDetailResponseDTO> finishComment = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(collegeEmployeeTaskTutors)){
            for (CollegeEmployeeTaskTutor collegeEmployeeTaskTutor : collegeEmployeeTaskTutors) {
                String taskId = collegeEmployeeTaskTutor.getTaskId();
                CollegeEmployeeTask collegeEmployeeTask = this.collegeEmployeeTaskService.getById(Long.valueOf(taskId));
                if (null != collegeEmployeeTask){
                    LocalDateTime taskCompleteDate = collegeEmployeeTask.getTaskCompleteDate();
                    //只展示已经完成的任务
                    if (null == taskCompleteDate){
                        continue;
                    }
                    String studyTaskId = collegeEmployeeTask.getStudyTaskId();
                    CollegeStudyTaskCfg collegeStudyTaskCfg = this.collegeStudyTaskCfgServiceImpl.getEffectiveByStudyTaskId(Long.valueOf(studyTaskId));
                    if (null != collegeStudyTaskCfg){
                        //只展示实践任务
                        if (!StringUtils.equals("2", collegeStudyTaskCfg.getStudyType())){
                            continue;
                        }
                        CollegeTaskCommentDetailResponseDTO collegeTaskCommentDetailResponseDTO = new CollegeTaskCommentDetailResponseDTO();
                        collegeTaskCommentDetailResponseDTO.setTaskDesc(collegeStudyTaskCfg.getTaskDesc());
                        collegeTaskCommentDetailResponseDTO.setTaskName(collegeStudyTaskCfg.getTaskName());
                        collegeTaskCommentDetailResponseDTO.setTaskId(Long.valueOf(taskId));

                        String employeeId = collegeEmployeeTask.getEmployeeId();
                        collegeTaskCommentDetailResponseDTO.setEmployeeId(employeeId);
                        String employeeName = this.employeeServiceImpl.getEmployeeNameEmployeeId(Long.valueOf(employeeId));
                        collegeTaskCommentDetailResponseDTO.setEmployeeName(employeeName);
                        CollegeEmployeeTaskScore collegeEmployeeTaskScore = collegeEmployeeTaskScoreServiceImpl.getByTaskId(taskId);
                        boolean isFinish = false;
                        if (null != collegeEmployeeTaskScore){
                            Integer imgScore = collegeEmployeeTaskScore.getImgScore();
                            Integer experienceScore = collegeEmployeeTaskScore.getExperienceScore();
                            if (null != imgScore && null != experienceScore){
                                isFinish = true;
                            }
                        }
                        if (isFinish){
                            finishComment.add(collegeTaskCommentDetailResponseDTO);
                        }else {
                            noComment.add(collegeTaskCommentDetailResponseDTO);
                        }
                    }
                }
            }
        }
        result.setFinishComment(finishComment);
        result.setNoComment(noComment);
        return result;
    }

    @GetMapping("queryTaskCommentInfoByTaskId")
    @RestResult
    public CollegeTaskCommentInfoResponseDTO queryTaskCommentInfoByTaskId(@RequestParam("taskId") Long taskId){
        CollegeTaskCommentInfoResponseDTO result = new CollegeTaskCommentInfoResponseDTO();
        LocalDateTime nowTime = TimeUtils.getCurrentLocalDateTime();
        CollegeEmployeeTask collegeEmployeeTask = this.collegeEmployeeTaskService.getById(taskId);
        if (null != collegeEmployeeTask){
            BeanUtils.copyProperties(collegeEmployeeTask, result);
            String studyTaskId = collegeEmployeeTask.getStudyTaskId();
            CollegeStudyTaskCfg collegeStudyTaskCfg = this.collegeStudyTaskCfgServiceImpl.getEffectiveByStudyTaskId(Long.valueOf(studyTaskId));
            if (null != collegeStudyTaskCfg){
                String employeeId = collegeEmployeeTask.getEmployeeId();
                result.setEmployeeId(employeeId);
                String employeeName = this.employeeServiceImpl.getEmployeeNameEmployeeId(Long.valueOf(employeeId));
                result.setEmployeeName(employeeName);
                CollegeEmployeeTaskScore collegeEmployeeTaskScore = collegeEmployeeTaskScoreServiceImpl.getByTaskId(String.valueOf(taskId));
                if (null != collegeEmployeeTaskScore){
                    Integer imgScore = collegeEmployeeTaskScore.getImgScore();
                    Integer experienceScore = collegeEmployeeTaskScore.getExperienceScore();
                    if (null != imgScore && null != experienceScore){
                        result.setImgScore(imgScore);
                        result.setExperienceScore(experienceScore);
                    }
                }
                Boolean isDelayFlag = false;
                LocalDateTime studyEndDate = result.getStudyEndDate();
                LocalDateTime taskCompleteDate = result.getTaskCompleteDate();
                if(TimeUtils.compareTwoTime(studyEndDate, taskCompleteDate) < 0){
                    isDelayFlag = true;
                }
                result.setIsDelayFlag(isDelayFlag);
                CollegeTaskExperienceScoreResponseDTO collegeTaskExperienceScoreResponseDTO = this.collegeTaskExperienceServiceImpl.queryByTaskId(String.valueOf(taskId));
                if(null != collegeTaskExperienceScoreResponseDTO){
                    result.setExperience(collegeTaskExperienceScoreResponseDTO.getWrittenExperience());
                    result.setFileList(collegeTaskExperienceScoreResponseDTO.getImgExperienceList());
                    result.setExperienceImgList(collegeTaskExperienceScoreResponseDTO.getExperienceDescImgList());
                }
            }
        }
        return result;
    }

    /**
     * 根据配置筛选习题
     * 1.获取任务对应的标签下所有习题
     * 2.根据配置随机获取，数量不够则重复使用
     */
    private List<TopicServiceDTO> filterTopic(List<CollegeExamRelCfg> examRels, List<TopicServiceDTO> topicServices) {
        List<TopicServiceDTO> topics = new ArrayList<>();
        for (CollegeExamRelCfg examRel : examRels) {
            List<TopicServiceDTO> collect = topicServices.stream().filter(x ->
                    StringUtils.equals(x.getType(), examRel.getTopicType())).collect(Collectors.toList());
            if (ArrayUtils.isEmpty(collect)) {
                return new ArrayList<>();
            }
            Collections.shuffle(collect);
            int num = 0;
            int size = collect.size();
            int topicNum = examRel.getTopicNum();
            if (size > topicNum) {
                num = size - topicNum;
                for (int i = 0; i < num; i++) {
                    collect.remove(i);
                }
            } else if (size < topicNum){
                num = topicNum - size;
                List<TopicServiceDTO> temps = new ArrayList<>();
                int i = 0;
                Random random = new Random();
                while (i < num) {
                    i++;
                    TopicServiceDTO temp = new TopicServiceDTO();
                    BeanUtils.copyProperties(collect.get(random.nextInt(size)), temp);
                    temps.add(temp);
                }
                collect.addAll(temps);
            }

            topics.addAll(collect);
        }

        return topics;
    }

    private void setNum(List<TopicServiceDTO> topics) {
        topics = topics.stream().sorted(Comparator.comparing(TopicServiceDTO::getType)).collect(Collectors.toList());
        if (ArrayUtils.isNotEmpty(topics)) {
            for (int i = 0; i < topics.size(); i++) {
                TopicServiceDTO topicServiceDTO = topics.get(i);
                topicServiceDTO.setTopicNum(Long.parseLong(String.valueOf(i+1)));
            }
        }
    }

    private CollegeEmployeeTask getTogetherStudyTaskByStudyTaskIdAndEmployeeId(List<CollegeEmployeeTask> list, String employeeId, String studyTaskId) {
        if (ArrayUtils.isNotEmpty(list)) {
            for (CollegeEmployeeTask collegeEmployeeTask : list) {
                if (StringUtils.equals(collegeEmployeeTask.getEmployeeId(), employeeId) && StringUtils.equals(collegeEmployeeTask.getStudyTaskId(), studyTaskId)) {
                    return collegeEmployeeTask;
                }
            }
        }
        return null;
    }
}