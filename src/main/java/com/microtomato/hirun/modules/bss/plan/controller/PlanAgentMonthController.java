package com.microtomato.hirun.modules.bss.plan.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.microtomato.hirun.modules.bss.plan.entity.po.PlanAgentMonth;
import com.microtomato.hirun.modules.bss.plan.service.IPlanAgentMonthService;
import com.microtomato.hirun.framework.annotation.RestResult;

import java.io.Serializable;
import java.util.List;

/**
 * (PlanAgentMonth)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-06-30 00:13:36
 */
@RestController
@RequestMapping("/api/bss.plan/plan-agent-month")
public class PlanAgentMonthController {

    /**
     * 服务对象
     */
    @Autowired
    private IPlanAgentMonthService planAgentMonthService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param planAgentMonth 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<PlanAgentMonth> selectByPage(Page<PlanAgentMonth> page, PlanAgentMonth planAgentMonth) {
        return this.planAgentMonthService.page(page, new QueryWrapper<>(planAgentMonth));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public PlanAgentMonth selectById(@PathVariable Serializable id) {
        return this.planAgentMonthService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param planAgentMonth 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody PlanAgentMonth planAgentMonth) {
        return this.planAgentMonthService.save(planAgentMonth);
    }

    /**
     * 修改数据
     *
     * @param planAgentMonth 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody PlanAgentMonth planAgentMonth) {
        return this.planAgentMonthService.updateById(planAgentMonth);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.planAgentMonthService.removeByIds(idList);
    }
}