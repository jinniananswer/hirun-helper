package com.microtomato.hirun.modules.bss.config.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.microtomato.hirun.modules.bss.config.entity.po.FeeItemCfg;
import com.microtomato.hirun.modules.bss.config.service.IFeeItemCfgService;
import com.microtomato.hirun.framework.annotation.RestResult;

import java.io.Serializable;
import java.util.List;

/**
 * 费用项配置表(FeeItemCfg)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-03-04 23:09:43
 */
@RestController
@RequestMapping("/api/FeeItemCfg")
public class FeeItemCfgController {

    /**
     * 服务对象
     */
    @Autowired
    private IFeeItemCfgService feeItemCfgService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param feeItemCfg 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<FeeItemCfg> selectByPage(Page<FeeItemCfg> page, FeeItemCfg feeItemCfg) {
        return this.feeItemCfgService.page(page, new QueryWrapper<>(feeItemCfg));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public FeeItemCfg selectById(@PathVariable Serializable id) {
        return this.feeItemCfgService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param feeItemCfg 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody FeeItemCfg feeItemCfg) {
        return this.feeItemCfgService.save(feeItemCfg);
    }

    /**
     * 修改数据
     *
     * @param feeItemCfg 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody FeeItemCfg feeItemCfg) {
        return this.feeItemCfgService.updateById(feeItemCfg);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.feeItemCfgService.removeByIds(idList);
    }
}