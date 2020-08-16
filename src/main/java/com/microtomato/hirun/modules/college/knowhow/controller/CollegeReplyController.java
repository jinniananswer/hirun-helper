package com.microtomato.hirun.modules.college.knowhow.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeReply;
import com.microtomato.hirun.modules.college.knowhow.service.ICollegeReplyService;
import com.microtomato.hirun.framework.annotation.RestResult;

import java.io.Serializable;
import java.util.List;

/**
 * (CollegeReply)表控制层
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-08-16 16:10:20
 */
@RestController
@RequestMapping("/api/CollegeReply")
public class CollegeReplyController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeReplyService collegeReplyService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param collegeReply 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<CollegeReply> selectByPage(Page<CollegeReply> page, CollegeReply collegeReply) {
        return this.collegeReplyService.page(page, new QueryWrapper<>(collegeReply));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public CollegeReply selectById(@PathVariable Serializable id) {
        return this.collegeReplyService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param collegeReply 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody CollegeReply collegeReply) {
        return this.collegeReplyService.save(collegeReply);
    }

    /**
     * 修改数据
     *
     * @param collegeReply 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody CollegeReply collegeReply) {
        return this.collegeReplyService.updateById(collegeReply);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.collegeReplyService.removeByIds(idList);
    }
}