package com.microtomato.hirun.modules.college.wiki.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.microtomato.hirun.modules.college.wiki.entity.po.CollegeWikiReply;
import com.microtomato.hirun.modules.college.wiki.service.ICollegeWikiReplyService;
import com.microtomato.hirun.framework.annotation.RestResult;

import java.io.Serializable;
import java.util.List;

/**
 * (CollegeWikiReply)表控制层
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-10-28 02:20:48
 */
@RestController
@RequestMapping("/api/CollegeWikiReply")
public class CollegeWikiReplyController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeWikiReplyService collegeWikiReplyService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param collegeWikiReply 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<CollegeWikiReply> selectByPage(Page<CollegeWikiReply> page, CollegeWikiReply collegeWikiReply) {
        return this.collegeWikiReplyService.page(page, new QueryWrapper<>(collegeWikiReply));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public CollegeWikiReply selectById(@PathVariable Serializable id) {
        return this.collegeWikiReplyService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param collegeWikiReply 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody CollegeWikiReply collegeWikiReply) {
        return this.collegeWikiReplyService.save(collegeWikiReply);
    }

    /**
     * 修改数据
     *
     * @param collegeWikiReply 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody CollegeWikiReply collegeWikiReply) {
        return this.collegeWikiReplyService.updateById(collegeWikiReply);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.collegeWikiReplyService.removeByIds(idList);
    }
}