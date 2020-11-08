package com.microtomato.hirun.modules.college.topic.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.microtomato.hirun.modules.college.topic.entity.po.ExamTopicOption;
import com.microtomato.hirun.modules.college.topic.service.IExamTopicOptionService;
import com.microtomato.hirun.framework.annotation.RestResult;

import java.io.Serializable;
import java.util.List;

/**
 * (ExamTopicOption)表控制层
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-09-04 11:39:06
 */
@RestController
@RequestMapping("/api/ExamTopicOption")
public class ExamTopicOptionController {

    /**
     * 服务对象
     */
    @Autowired
    private IExamTopicOptionService examTopicOptionService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param examTopicOption 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<ExamTopicOption> selectByPage(Page<ExamTopicOption> page, ExamTopicOption examTopicOption) {
        return this.examTopicOptionService.page(page, new QueryWrapper<>(examTopicOption));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public ExamTopicOption selectById(@PathVariable Serializable id) {
        return this.examTopicOptionService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param examTopicOption 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody ExamTopicOption examTopicOption) {
        return this.examTopicOptionService.save(examTopicOption);
    }

    /**
     * 修改数据
     *
     * @param examTopicOption 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody ExamTopicOption examTopicOption) {
        return this.examTopicOptionService.updateById(examTopicOption);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.examTopicOptionService.removeByIds(idList);
    }
}