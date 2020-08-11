package com.microtomato.hirun.modules.college.config.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeCourseTaskCfg;
import com.microtomato.hirun.modules.college.config.service.ICollegeCourseTaskCfgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * (CollegeCourseTaskCfg)表控制层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-08-04 00:19:05
 */
@RestController
@RequestMapping("/api/CollegeCourseTaskCfg")
public class CollegeCourseTaskCfgController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeCourseTaskCfgService collegeCourseTaskCfgService;

    /**
     * 分页查询所有数据
     *
     * @param page                 分页对象
     * @param collegeCourseTaskCfg 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<CollegeCourseTaskCfg> selectByPage(Page<CollegeCourseTaskCfg> page, CollegeCourseTaskCfg collegeCourseTaskCfg) {
        return this.collegeCourseTaskCfgService.page(page, new QueryWrapper<>(collegeCourseTaskCfg));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public CollegeCourseTaskCfg selectById(@PathVariable Serializable id) {
        return this.collegeCourseTaskCfgService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param collegeCourseTaskCfg 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody CollegeCourseTaskCfg collegeCourseTaskCfg) {
        return this.collegeCourseTaskCfgService.save(collegeCourseTaskCfg);
    }

    /**
     * 修改数据
     *
     * @param collegeCourseTaskCfg 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody CollegeCourseTaskCfg collegeCourseTaskCfg) {
        return this.collegeCourseTaskCfgService.updateById(collegeCourseTaskCfg);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.collegeCourseTaskCfgService.removeByIds(idList);
    }
}