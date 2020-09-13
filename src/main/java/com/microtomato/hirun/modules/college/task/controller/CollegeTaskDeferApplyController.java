package com.microtomato.hirun.modules.college.task.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeTaskDeferApply;
import com.microtomato.hirun.modules.college.task.service.ICollegeTaskDeferApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * (CollegeTaskDeferApply)表控制层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-13 14:01:02
 */
@RestController
@RequestMapping("/api/CollegeTaskDeferApply")
public class CollegeTaskDeferApplyController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeTaskDeferApplyService collegeTaskDeferApplyService;

    /**
     * 分页查询所有数据
     *
     * @param page                  分页对象
     * @param collegeTaskDeferApply 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<CollegeTaskDeferApply> selectByPage(Page<CollegeTaskDeferApply> page, CollegeTaskDeferApply collegeTaskDeferApply) {
        return this.collegeTaskDeferApplyService.page(page, new QueryWrapper<>(collegeTaskDeferApply));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public CollegeTaskDeferApply selectById(@PathVariable Serializable id) {
        return this.collegeTaskDeferApplyService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param collegeTaskDeferApply 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody CollegeTaskDeferApply collegeTaskDeferApply) {
        return this.collegeTaskDeferApplyService.save(collegeTaskDeferApply);
    }

    /**
     * 修改数据
     *
     * @param collegeTaskDeferApply 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody CollegeTaskDeferApply collegeTaskDeferApply) {
        return this.collegeTaskDeferApplyService.updateById(collegeTaskDeferApply);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.collegeTaskDeferApplyService.removeByIds(idList);
    }
}