package com.microtomato.hirun.modules.college.task.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.college.task.entity.dto.CollegeEmployeeTaskTutorRequestDTO;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTaskTutor;
import com.microtomato.hirun.modules.college.task.service.ICollegeEmployeeTaskTutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * (CollegeEmployeeTaskTutor)表控制层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-04 22:53:05
 */
@RestController
@RequestMapping("/api/CollegeEmployeeTaskTutor")
public class CollegeEmployeeTaskTutorController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeEmployeeTaskTutorService collegeEmployeeTaskTutorService;

    /**
     * 分页查询所有数据
     *
     * @param page                     分页对象
     * @param collegeEmployeeTaskTutor 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<CollegeEmployeeTaskTutor> selectByPage(Page<CollegeEmployeeTaskTutor> page, CollegeEmployeeTaskTutor collegeEmployeeTaskTutor) {
        return this.collegeEmployeeTaskTutorService.page(page, new QueryWrapper<>(collegeEmployeeTaskTutor));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public CollegeEmployeeTaskTutor selectById(@PathVariable Serializable id) {
        return this.collegeEmployeeTaskTutorService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param collegeEmployeeTaskTutor 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody CollegeEmployeeTaskTutor collegeEmployeeTaskTutor) {
        return this.collegeEmployeeTaskTutorService.save(collegeEmployeeTaskTutor);
    }

    /**
     * 修改数据
     *
     * @param collegeEmployeeTaskTutor 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody CollegeEmployeeTaskTutor collegeEmployeeTaskTutor) {
        return this.collegeEmployeeTaskTutorService.updateById(collegeEmployeeTaskTutor);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.collegeEmployeeTaskTutorService.removeByIds(idList);
    }

    @PostMapping("addByTaskIdAndSelectTutor")
    @RestResult
    public void addByTaskIdAndSelectTutor(@RequestBody CollegeEmployeeTaskTutorRequestDTO collegeEmployeeTaskTutorRequestDTO){
        this.collegeEmployeeTaskTutorService.addByTaskIdAndSelectTutor(collegeEmployeeTaskTutorRequestDTO.getTaskId(), collegeEmployeeTaskTutorRequestDTO.getSelectTutor());
    }
}