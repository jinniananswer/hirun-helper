package com.microtomato.hirun.modules.college.config.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeExamRelCfg;
import com.microtomato.hirun.modules.college.config.service.ICollegeExamRelCfgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * (CollegeExamRelCfg)表控制层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-18 01:12:54
 */
@RestController
@RequestMapping("/api/CollegeExamRelCfg")
public class CollegeExamRelCfgController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeExamRelCfgService collegeExamRelCfgService;

    /**
     * 分页查询所有数据
     *
     * @param page              分页对象
     * @param collegeExamRelCfg 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<CollegeExamRelCfg> selectByPage(Page<CollegeExamRelCfg> page, CollegeExamRelCfg collegeExamRelCfg) {
        return this.collegeExamRelCfgService.page(page, new QueryWrapper<>(collegeExamRelCfg));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public CollegeExamRelCfg selectById(@PathVariable Serializable id) {
        return this.collegeExamRelCfgService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param collegeExamRelCfg 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody CollegeExamRelCfg collegeExamRelCfg) {
        return this.collegeExamRelCfgService.save(collegeExamRelCfg);
    }

    /**
     * 修改数据
     *
     * @param collegeExamRelCfg 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody CollegeExamRelCfg collegeExamRelCfg) {
        return this.collegeExamRelCfgService.updateById(collegeExamRelCfg);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.collegeExamRelCfgService.removeByIds(idList);
    }
}