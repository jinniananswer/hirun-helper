package com.microtomato.hirun.modules.college.task.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeStudyTaskScore;
import com.microtomato.hirun.modules.college.task.service.ICollegeStudyTaskScoreService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * (CollegeStudyTaskScore)表控制层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-21 23:53:59
 */
@RestController
@RequestMapping("/api/CollegeStudyTaskScore")
public class CollegeStudyTaskScoreController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeStudyTaskScoreService collegeStudyTaskScoreService;

    /**
     * 分页查询所有数据
     *
     * @param page                  分页对象
     * @param collegeStudyTaskScore 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<CollegeStudyTaskScore> selectByPage(Page<CollegeStudyTaskScore> page, CollegeStudyTaskScore collegeStudyTaskScore) {
        return this.collegeStudyTaskScoreService.page(page, new QueryWrapper<>(collegeStudyTaskScore));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public CollegeStudyTaskScore selectById(@PathVariable Serializable id) {
        return this.collegeStudyTaskScoreService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param collegeStudyTaskScore 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody CollegeStudyTaskScore collegeStudyTaskScore) {
        return this.collegeStudyTaskScoreService.save(collegeStudyTaskScore);
    }

    /**
     * 修改数据
     *
     * @param collegeStudyTaskScore 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody CollegeStudyTaskScore collegeStudyTaskScore) {
        return this.collegeStudyTaskScoreService.updateById(collegeStudyTaskScore);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.collegeStudyTaskScoreService.removeByIds(idList);
    }

    @PostMapping("taskScore")
    @RestResult
    public void taskScore(@RequestParam("taskId") String taskId, @RequestParam("taskDifficultyScore") String taskDifficultyScore, @RequestParam("tutorScore") String tutorScore){
        CollegeStudyTaskScore collegeStudyTaskScore = new CollegeStudyTaskScore();
        collegeStudyTaskScore.setTaskId(taskId);
        if (StringUtils.isNotEmpty(taskDifficultyScore)){
            collegeStudyTaskScore.setTaskDifficultyScore(Integer.valueOf(taskDifficultyScore));
        }
        if (StringUtils.isNotEmpty(tutorScore)){
            collegeStudyTaskScore.setTutorScore(Integer.valueOf(tutorScore));
        }
        this.collegeStudyTaskScoreService.taskScore(collegeStudyTaskScore);
    }
}