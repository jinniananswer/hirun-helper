package com.microtomato.hirun.modules.college.teacher.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.microtomato.hirun.modules.college.teacher.entity.po.Teacher;
import com.microtomato.hirun.modules.college.teacher.service.ITeacherService;
import com.microtomato.hirun.framework.annotation.RestResult;

import java.io.Serializable;
import java.util.List;

/**
 * (Teacher)表控制层
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-09-17 01:10:28
 */
@RestController
@RequestMapping("/api/Teacher")
public class TeacherController {

    /**
     * 服务对象
     */
    @Autowired
    private ITeacherService teacherService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param teacher 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<Teacher> selectByPage(Page<Teacher> page, Teacher teacher) {
        return this.teacherService.page(page, new QueryWrapper<>(teacher));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public Teacher selectById(@PathVariable Serializable id) {
        return this.teacherService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param teacher 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody Teacher teacher) {
        return this.teacherService.save(teacher);
    }

    /**
     * 修改数据
     *
     * @param teacher 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody Teacher teacher) {
        return this.teacherService.updateById(teacher);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.teacherService.removeByIds(idList);
    }
}