package com.microtomato.hirun.modules.college.task.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyTaskCfg;
import com.microtomato.hirun.modules.college.config.service.ICollegeStudyTaskCfgService;
import com.microtomato.hirun.modules.college.task.entity.dto.CollegeEmployeeTaskScoreRequestDTO;
import com.microtomato.hirun.modules.college.task.entity.dto.CollegeJudgeEvaluateTaskResponseDTO;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTask;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTaskScore;
import com.microtomato.hirun.modules.college.task.service.ICollegeEmployeeTaskScoreService;
import com.microtomato.hirun.modules.college.task.service.ICollegeEmployeeTaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * (CollegeEmployeeTaskScore)表控制层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-18 23:21:01
 */
@RestController
@RequestMapping("/api/CollegeEmployeeTaskScore")
public class CollegeEmployeeTaskScoreController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeEmployeeTaskScoreService collegeEmployeeTaskScoreService;

    @Autowired
    private ICollegeEmployeeTaskService collegeEmployeeTaskServiceImpl;

    @Autowired
    private ICollegeStudyTaskCfgService collegeStudyTaskCfgServiceImpl;

    /**
     * 分页查询所有数据
     *
     * @param page                     分页对象
     * @param collegeEmployeeTaskScore 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<CollegeEmployeeTaskScore> selectByPage(Page<CollegeEmployeeTaskScore> page, CollegeEmployeeTaskScore collegeEmployeeTaskScore) {
        return this.collegeEmployeeTaskScoreService.page(page, new QueryWrapper<>(collegeEmployeeTaskScore));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public CollegeEmployeeTaskScore selectById(@PathVariable Serializable id) {
        return this.collegeEmployeeTaskScoreService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param collegeEmployeeTaskScore 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody CollegeEmployeeTaskScore collegeEmployeeTaskScore) {
        return this.collegeEmployeeTaskScoreService.save(collegeEmployeeTaskScore);
    }

    /**
     * 修改数据
     *
     * @param collegeEmployeeTaskScore 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody CollegeEmployeeTaskScore collegeEmployeeTaskScore) {
        return this.collegeEmployeeTaskScoreService.updateById(collegeEmployeeTaskScore);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.collegeEmployeeTaskScoreService.removeByIds(idList);
    }

    @PostMapping("evaluateTask")
    @RestResult
    public void evaluateTask(@RequestBody CollegeEmployeeTaskScoreRequestDTO collegeEmployeeTaskScoreRequestDTO){
        String taskId = collegeEmployeeTaskScoreRequestDTO.getTaskId();
        CollegeEmployeeTask collegeEmployeeTask = collegeEmployeeTaskServiceImpl.getById(Long.valueOf(taskId));
        if (null != collegeEmployeeTask){
            Integer taskScore = collegeEmployeeTaskScoreRequestDTO.getTaskScore();
            collegeEmployeeTask.setScore(taskScore);
            collegeEmployeeTaskServiceImpl.updateById(collegeEmployeeTask);
        }
        Integer studyScore = collegeEmployeeTaskScoreRequestDTO.getStudyScore();
        Integer exercisesScore = collegeEmployeeTaskScoreRequestDTO.getExercisesScore();
        Integer examScore = collegeEmployeeTaskScoreRequestDTO.getExamScore();
        CollegeEmployeeTaskScore studyScoreEntity = new CollegeEmployeeTaskScore();
        studyScoreEntity.setTaskId(taskId);
        studyScoreEntity.setStudyScore(studyScore);
        studyScoreEntity.setExercisesScore(exercisesScore);
        studyScoreEntity.setExamScore(examScore);
        collegeEmployeeTaskScoreService.save(studyScoreEntity);
    }

    @GetMapping("isEvaluateTaskByTaskId")
    @RestResult
    public CollegeJudgeEvaluateTaskResponseDTO isEvaluateTaskByTaskId(String taskId){
        CollegeJudgeEvaluateTaskResponseDTO result = new CollegeJudgeEvaluateTaskResponseDTO();
        if (StringUtils.isNotEmpty(taskId)){
            CollegeEmployeeTask collegeEmployeeTask = collegeEmployeeTaskServiceImpl.getById(Long.valueOf(taskId));
            if (null != collegeEmployeeTask){
                LocalDateTime studyCompleteDate = collegeEmployeeTask.getStudyCompleteDate();
                if (null == studyCompleteDate){
                    result.setEvaluateTask(false);
                    result.setScoreNotReasons("学习未完成，不能评分");
                    return result;
                }
                Integer exercisesCompletedNumber = collegeEmployeeTask.getExercisesCompletedNumber();
                if (null == exercisesCompletedNumber){
                    result.setEvaluateTask(false);
                    result.setScoreNotReasons("练习未完成，不能评分");
                    return result;

                }
                String studyTaskId = collegeEmployeeTask.getStudyTaskId();
                CollegeStudyTaskCfg collegeStudyTaskCfg = collegeStudyTaskCfgServiceImpl.getAllByStudyTaskId(Long.valueOf(studyTaskId));
                if (null != collegeStudyTaskCfg){
                    Integer exercisesNumber = collegeStudyTaskCfg.getExercisesNumber();
                    if (null != exercisesNumber && exercisesNumber >= exercisesCompletedNumber){
                        result.setEvaluateTask(false);
                        result.setScoreNotReasons("练习未完成，不能评分");
                        return result;
                    }
                }
                Integer examScore = collegeEmployeeTask.getExamScore();
                if (null == examScore){
                    result.setEvaluateTask(false);
                    result.setScoreNotReasons("未参加考试，不能评分");
                    return result;
                }
            }
            CollegeEmployeeTaskScore collegeEmployeeTaskScore = collegeEmployeeTaskScoreService.getByTaskId(taskId);
            if (null != collegeEmployeeTaskScore){
                result.setEvaluateTask(false);
                result.setScoreNotReasons("已经评分，不能再次评分");
                return result;
            }
        }
        result.setEvaluateTask(true);
        return result;
    }
}