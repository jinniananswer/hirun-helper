package com.microtomato.hirun.modules.college.task.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.college.task.entity.dto.CollegeTaskExperienceRequetDTO;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeTaskExperience;
import com.microtomato.hirun.modules.college.task.service.ICollegeTaskExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * (CollegeTaskExperience)表控制层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-27 03:36:43
 */
@RestController
@RequestMapping("/api/CollegeTaskExperience")
public class CollegeTaskExperienceController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeTaskExperienceService collegeTaskExperienceService;

    /**
     * 分页查询所有数据
     *
     * @param page                  分页对象
     * @param collegeTaskExperience 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<CollegeTaskExperience> selectByPage(Page<CollegeTaskExperience> page, CollegeTaskExperience collegeTaskExperience) {
        return this.collegeTaskExperienceService.page(page, new QueryWrapper<>(collegeTaskExperience));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public CollegeTaskExperience selectById(@PathVariable Serializable id) {
        return this.collegeTaskExperienceService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param collegeTaskExperience 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody CollegeTaskExperience collegeTaskExperience) {
        return this.collegeTaskExperienceService.save(collegeTaskExperience);
    }

    /**
     * 修改数据
     *
     * @param collegeTaskExperience 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody CollegeTaskExperience collegeTaskExperience) {
        return this.collegeTaskExperienceService.updateById(collegeTaskExperience);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.collegeTaskExperienceService.removeByIds(idList);
    }

    @GetMapping("queryByTaskId")
    @RestResult
    public CollegeTaskExperienceRequetDTO queryByTaskId(String taskId){
        return this.collegeTaskExperienceService.queryByTaskId(taskId);
    }
}