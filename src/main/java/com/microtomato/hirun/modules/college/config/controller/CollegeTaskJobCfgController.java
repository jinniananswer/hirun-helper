package com.microtomato.hirun.modules.college.config.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeTaskJobCfg;
import com.microtomato.hirun.modules.college.config.service.ICollegeTaskJobCfgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * (CollegeTaskJobCfg)表控制层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-16 03:19:46
 */
@RestController
@RequestMapping("/api/CollegeTaskJobCfg")
public class CollegeTaskJobCfgController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeTaskJobCfgService collegeTaskJobCfgService;

    /**
     * 分页查询所有数据
     *
     * @param page              分页对象
     * @param collegeTaskJobCfg 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<CollegeTaskJobCfg> selectByPage(Page<CollegeTaskJobCfg> page, CollegeTaskJobCfg collegeTaskJobCfg) {
        return this.collegeTaskJobCfgService.page(page, new QueryWrapper<>(collegeTaskJobCfg));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public CollegeTaskJobCfg selectById(@PathVariable Serializable id) {
        return this.collegeTaskJobCfgService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param collegeTaskJobCfg 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody CollegeTaskJobCfg collegeTaskJobCfg) {
        return this.collegeTaskJobCfgService.save(collegeTaskJobCfg);
    }

    /**
     * 修改数据
     *
     * @param collegeTaskJobCfg 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody CollegeTaskJobCfg collegeTaskJobCfg) {
        return this.collegeTaskJobCfgService.updateById(collegeTaskJobCfg);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.collegeTaskJobCfgService.removeByIds(idList);
    }
}