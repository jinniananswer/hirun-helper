package com.microtomato.hirun.modules.college.task.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.microtomato.hirun.modules.college.task.entity.po.CollegeTaskScore;
import com.microtomato.hirun.modules.college.task.service.ICollegeTaskScoreService;
import com.microtomato.hirun.framework.annotation.RestResult;

import java.io.Serializable;
import java.util.List;

/**
 * (CollegeTaskScore)表控制层
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-10-22 04:16:25
 */
@RestController
@RequestMapping("/api/CollegeTaskScore")
public class CollegeTaskScoreController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeTaskScoreService collegeTaskScoreService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param collegeTaskScore 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<CollegeTaskScore> selectByPage(Page<CollegeTaskScore> page, CollegeTaskScore collegeTaskScore) {
        return this.collegeTaskScoreService.page(page, new QueryWrapper<>(collegeTaskScore));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public CollegeTaskScore selectById(@PathVariable Serializable id) {
        return this.collegeTaskScoreService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param collegeTaskScore 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody CollegeTaskScore collegeTaskScore) {
        return this.collegeTaskScoreService.save(collegeTaskScore);
    }

    /**
     * 修改数据
     *
     * @param collegeTaskScore 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody CollegeTaskScore collegeTaskScore) {
        return this.collegeTaskScoreService.updateById(collegeTaskScore);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.collegeTaskScoreService.removeByIds(idList);
    }
}