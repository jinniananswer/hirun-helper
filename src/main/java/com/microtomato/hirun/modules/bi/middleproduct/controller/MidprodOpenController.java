package com.microtomato.hirun.modules.bi.middleproduct.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bi.middleproduct.entity.po.MidprodOpen;
import com.microtomato.hirun.modules.bi.middleproduct.service.IMidprodOpenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * (MidprodOpen)表控制层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-26 00:48:46
 */
@RestController
@RequestMapping("/api/MidprodOpen")
public class MidprodOpenController {

    /**
     * 服务对象
     */
    @Autowired
    private IMidprodOpenService midprodOpenService;

    /**
     * 分页查询所有数据
     *
     * @param page        分页对象
     * @param midprodOpen 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<MidprodOpen> selectByPage(Page<MidprodOpen> page, MidprodOpen midprodOpen) {
        return this.midprodOpenService.page(page, new QueryWrapper<>(midprodOpen));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public MidprodOpen selectById(@PathVariable Serializable id) {
        return this.midprodOpenService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param midprodOpen 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody MidprodOpen midprodOpen) {
        return this.midprodOpenService.save(midprodOpen);
    }

    /**
     * 修改数据
     *
     * @param midprodOpen 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody MidprodOpen midprodOpen) {
        return this.midprodOpenService.updateById(midprodOpen);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.midprodOpenService.removeByIds(idList);
    }
}