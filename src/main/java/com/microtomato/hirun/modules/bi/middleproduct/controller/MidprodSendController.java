package com.microtomato.hirun.modules.bi.middleproduct.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bi.middleproduct.entity.po.MidprodSend;
import com.microtomato.hirun.modules.bi.middleproduct.service.IMidprodSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * (MidprodSend)表控制层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-26 00:48:47
 */
@RestController
@RequestMapping("/api/MidprodSend")
public class MidprodSendController {

    /**
     * 服务对象
     */
    @Autowired
    private IMidprodSendService midprodSendService;

    /**
     * 分页查询所有数据
     *
     * @param page        分页对象
     * @param midprodSend 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<MidprodSend> selectByPage(Page<MidprodSend> page, MidprodSend midprodSend) {
        return this.midprodSendService.page(page, new QueryWrapper<>(midprodSend));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public MidprodSend selectById(@PathVariable Serializable id) {
        return this.midprodSendService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param midprodSend 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody MidprodSend midprodSend) {
        return this.midprodSendService.save(midprodSend);
    }

    /**
     * 修改数据
     *
     * @param midprodSend 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody MidprodSend midprodSend) {
        return this.midprodSendService.updateById(midprodSend);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.midprodSendService.removeByIds(idList);
    }
}