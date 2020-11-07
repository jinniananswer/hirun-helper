package com.microtomato.hirun.modules.college.config.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeTaskEmployeeCfg;
import com.microtomato.hirun.modules.college.config.service.ICollegeTaskEmployeeCfgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * (CollegeTaskEmployeeCfg)表控制层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-11-07 22:36:24
 */
@RestController
@RequestMapping("/api/CollegeTaskEmployeeCfg")
public class CollegeTaskEmployeeCfgController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeTaskEmployeeCfgService collegeTaskEmployeeCfgService;

    /**
     * 分页查询所有数据
     *
     * @param page                   分页对象
     * @param collegeTaskEmployeeCfg 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<CollegeTaskEmployeeCfg> selectByPage(Page<CollegeTaskEmployeeCfg> page, CollegeTaskEmployeeCfg collegeTaskEmployeeCfg) {
        return this.collegeTaskEmployeeCfgService.page(page, new QueryWrapper<>(collegeTaskEmployeeCfg));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public CollegeTaskEmployeeCfg selectById(@PathVariable Serializable id) {
        return this.collegeTaskEmployeeCfgService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param collegeTaskEmployeeCfg 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody CollegeTaskEmployeeCfg collegeTaskEmployeeCfg) {
        return this.collegeTaskEmployeeCfgService.save(collegeTaskEmployeeCfg);
    }

    /**
     * 修改数据
     *
     * @param collegeTaskEmployeeCfg 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody CollegeTaskEmployeeCfg collegeTaskEmployeeCfg) {
        return this.collegeTaskEmployeeCfgService.updateById(collegeTaskEmployeeCfg);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.collegeTaskEmployeeCfgService.removeByIds(idList);
    }
}