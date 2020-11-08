package com.microtomato.hirun.modules.college.task.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeStudyTopicRel;
import com.microtomato.hirun.modules.college.task.service.ICollegeStudyTopicRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * (CollegeStudyTopicRel)表控制层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-20 02:17:17
 */
@RestController
@RequestMapping("/api/CollegeStudyTopicRel")
public class CollegeStudyTopicRelController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeStudyTopicRelService collegeStudyTopicRelService;

    /**
     * 分页查询所有数据
     *
     * @param page                 分页对象
     * @param collegeStudyTopicRel 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<CollegeStudyTopicRel> selectByPage(Page<CollegeStudyTopicRel> page, CollegeStudyTopicRel collegeStudyTopicRel) {
        return this.collegeStudyTopicRelService.page(page, new QueryWrapper<>(collegeStudyTopicRel));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public CollegeStudyTopicRel selectById(@PathVariable Serializable id) {
        return this.collegeStudyTopicRelService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param collegeStudyTopicRel 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody CollegeStudyTopicRel collegeStudyTopicRel) {
        return this.collegeStudyTopicRelService.save(collegeStudyTopicRel);
    }

    /**
     * 修改数据
     *
     * @param collegeStudyTopicRel 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody CollegeStudyTopicRel collegeStudyTopicRel) {
        return this.collegeStudyTopicRelService.updateById(collegeStudyTopicRel);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.collegeStudyTopicRelService.removeByIds(idList);
    }
}