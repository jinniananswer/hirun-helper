package com.microtomato.hirun.modules.college.config.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.microtomato.hirun.modules.college.config.entity.po.CollegeTopicLabelCfg;
import com.microtomato.hirun.modules.college.config.service.ICollegeTopicLabelCfgService;
import com.microtomato.hirun.framework.annotation.RestResult;

import java.io.Serializable;
import java.util.List;

/**
 * (CollegeTopicLabelCfg)表控制层
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-10-20 01:28:12
 */
@RestController
@RequestMapping("/api/CollegeTopicLabelCfg")
public class CollegeTopicLabelCfgController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeTopicLabelCfgService collegeTopicLabelCfgService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param collegeTopicLabelCfg 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<CollegeTopicLabelCfg> selectByPage(Page<CollegeTopicLabelCfg> page, CollegeTopicLabelCfg collegeTopicLabelCfg) {
        return this.collegeTopicLabelCfgService.page(page, new QueryWrapper<>(collegeTopicLabelCfg));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public CollegeTopicLabelCfg selectById(@PathVariable Serializable id) {
        return this.collegeTopicLabelCfgService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param collegeTopicLabelCfg 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody CollegeTopicLabelCfg collegeTopicLabelCfg) {
        return this.collegeTopicLabelCfgService.save(collegeTopicLabelCfg);
    }

    /**
     * 修改数据
     *
     * @param collegeTopicLabelCfg 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody CollegeTopicLabelCfg collegeTopicLabelCfg) {
        return this.collegeTopicLabelCfgService.updateById(collegeTopicLabelCfg);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.collegeTopicLabelCfgService.removeByIds(idList);
    }
}