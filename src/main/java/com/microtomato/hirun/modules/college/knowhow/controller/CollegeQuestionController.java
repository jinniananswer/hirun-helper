package com.microtomato.hirun.modules.college.knowhow.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeQuestion;
import com.microtomato.hirun.modules.college.knowhow.service.ICollegeQuestionService;
import com.microtomato.hirun.framework.annotation.RestResult;

import java.io.Serializable;
import java.util.List;

/**
 * (CollegeQuestion)表控制层
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-08-16 16:14:44
 */
@RestController
@RequestMapping("/api/CollegeQuestion")
public class CollegeQuestionController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeQuestionService collegeQuestionService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param collegeQuestion 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<CollegeQuestion> selectByPage(Page<CollegeQuestion> page, CollegeQuestion collegeQuestion) {
        return this.collegeQuestionService.page(page, new QueryWrapper<>(collegeQuestion));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public CollegeQuestion selectById(@PathVariable Serializable id) {
        return this.collegeQuestionService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param collegeQuestion 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody CollegeQuestion collegeQuestion) {
        return this.collegeQuestionService.save(collegeQuestion);
    }

    /**
     * 修改数据
     *
     * @param collegeQuestion 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody CollegeQuestion collegeQuestion) {
        return this.collegeQuestionService.updateById(collegeQuestion);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.collegeQuestionService.removeByIds(idList);
    }

    /**
     * 1.知乎首页
     * 2.提问
     * 3.回答
     * 4.审批
     */
}