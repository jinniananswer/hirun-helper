package com.microtomato.hirun.modules.college.topic.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.microtomato.hirun.modules.college.topic.entity.po.CollegeTopicLabelRel;
import com.microtomato.hirun.modules.college.topic.service.ICollegeTopicLabelRelService;
import com.microtomato.hirun.framework.annotation.RestResult;

import java.io.Serializable;
import java.util.List;

/**
 * (CollegeTopicLabelRel)表控制层
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-10-20 01:22:12
 */
@RestController
@RequestMapping("/api/CollegeTopicLabelRel")
public class CollegeTopicLabelRelController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeTopicLabelRelService collegeTopicLabelRelService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param collegeTopicLabelRel 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<CollegeTopicLabelRel> selectByPage(Page<CollegeTopicLabelRel> page, CollegeTopicLabelRel collegeTopicLabelRel) {
        return this.collegeTopicLabelRelService.page(page, new QueryWrapper<>(collegeTopicLabelRel));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public CollegeTopicLabelRel selectById(@PathVariable Serializable id) {
        return this.collegeTopicLabelRelService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param collegeTopicLabelRel 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody CollegeTopicLabelRel collegeTopicLabelRel) {
        return this.collegeTopicLabelRelService.save(collegeTopicLabelRel);
    }

    /**
     * 修改数据
     *
     * @param collegeTopicLabelRel 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody CollegeTopicLabelRel collegeTopicLabelRel) {
        return this.collegeTopicLabelRelService.updateById(collegeTopicLabelRel);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.collegeTopicLabelRelService.removeByIds(idList);
    }
}