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
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTaskScore;
import com.microtomato.hirun.modules.college.task.service.ICollegeEmployeeTaskScoreService;
import com.microtomato.hirun.modules.college.task.service.ICollegeEmployeeTaskService;
import com.microtomato.hirun.modules.college.task.service.ITaskDomainOpenService;
import com.microtomato.hirun.modules.organization.entity.dto.SimpleEmployeeDTO;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.service.IEmployeeJobRoleService;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.ListUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                Integer exercisesCompletedNumber = collegeEmployeeTask.getExercisesCompletedNumber();
                if (null != exercisesCompletedNumber){
                    Integer exercisesNumber = collegeStudyTask.getExercisesNumber();
                    if (null != exercisesNumber){
                        if (exercisesCompletedNumber >= exercisesNumber){
                            exercisesFinishNum++;
                        }
                    }
                }
                Integer examScore = collegeEmployeeTask.getExamScore();
                if (null != examScore){
                    Integer passScore = collegeStudyTask.getPassScore();
                    if (null != passScore){
                        if (null != examScore && examScore > passScore){
                            examPassNum++;
                        }
                    }
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
}