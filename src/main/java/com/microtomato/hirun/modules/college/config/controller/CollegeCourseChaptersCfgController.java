package com.microtomato.hirun.modules.college.config.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeCourseChaptersCfg;
import com.microtomato.hirun.modules.college.config.service.ICollegeCourseChaptersCfgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * (CollegeCourseChaptersCfg)表控制层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-08-04 00:18:35
 */
@RestController
@RequestMapping("/api/CollegeCourseChaptersCfg")
public class CollegeCourseChaptersCfgController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeCourseChaptersCfgService collegeCourseChaptersCfgService;

    /**
     * 分页查询所有数据
     *
     * @param page                     分页对象
     * @param collegeCourseChaptersCfg 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<CollegeCourseChaptersCfg> selectByPage(Page<CollegeCourseChaptersCfg> page, CollegeCourseChaptersCfg collegeCourseChaptersCfg) {
        return this.collegeCourseChaptersCfgService.page(page, new QueryWrapper<>(collegeCourseChaptersCfg));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public CollegeCourseChaptersCfg selectById(@PathVariable Serializable id) {
        return this.collegeCourseChaptersCfgService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param collegeCourseChaptersCfg 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody CollegeCourseChaptersCfg collegeCourseChaptersCfg) {
        return this.collegeCourseChaptersCfgService.save(collegeCourseChaptersCfg);
    }

    /**
     * 修改数据
     *
     * @param collegeCourseChaptersCfg 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody CollegeCourseChaptersCfg collegeCourseChaptersCfg) {
        return this.collegeCourseChaptersCfgService.updateById(collegeCourseChaptersCfg);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.collegeCourseChaptersCfgService.removeByIds(idList);
    }
}