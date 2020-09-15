package com.microtomato.hirun.modules.college.task.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.microtomato.hirun.modules.college.task.entity.po.CollegeStudyTopicCfg;
import com.microtomato.hirun.modules.college.task.service.ICollegeStudyTopicCfgService;
import com.microtomato.hirun.framework.annotation.RestResult;

import java.io.Serializable;
import java.util.List;

/**
 * (CollegeStudyTopicCfg)表控制层
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-09-16 01:58:14
 */
@RestController
@RequestMapping("/api/CollegeStudyTopicCfg")
public class CollegeStudyTopicCfgController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeStudyTopicCfgService collegeStudyTopicCfgService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param collegeStudyTopicCfg 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<CollegeStudyTopicCfg> selectByPage(Page<CollegeStudyTopicCfg> page, CollegeStudyTopicCfg collegeStudyTopicCfg) {
        return this.collegeStudyTopicCfgService.page(page, new QueryWrapper<>(collegeStudyTopicCfg));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public CollegeStudyTopicCfg selectById(@PathVariable Serializable id) {
        return this.collegeStudyTopicCfgService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param collegeStudyTopicCfg 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody CollegeStudyTopicCfg collegeStudyTopicCfg) {
        return this.collegeStudyTopicCfgService.save(collegeStudyTopicCfg);
    }

    /**
     * 修改数据
     *
     * @param collegeStudyTopicCfg 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody CollegeStudyTopicCfg collegeStudyTopicCfg) {
        return this.collegeStudyTopicCfgService.updateById(collegeStudyTopicCfg);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.collegeStudyTopicCfgService.removeByIds(idList);
    }
}