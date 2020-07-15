package com.microtomato.hirun.modules.bss.supply.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.microtomato.hirun.modules.bss.supply.entity.po.SupplyOrderDetail;
import com.microtomato.hirun.modules.bss.supply.service.ISupplyOrderDetailService;
import com.microtomato.hirun.framework.annotation.RestResult;

import java.io.Serializable;
import java.util.List;

/**
 * 供应订单明细表(SupplyOrderDetail)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-15 11:26:08
 */
@RestController
@RequestMapping("/api/bss.supply/supply-order-detail")
public class SupplyOrderDetailController {

    /**
     * 服务对象
     */
    @Autowired
    private ISupplyOrderDetailService supplyOrderDetailService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param supplyOrderDetail 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<SupplyOrderDetail> selectByPage(Page<SupplyOrderDetail> page, SupplyOrderDetail supplyOrderDetail) {
        return this.supplyOrderDetailService.page(page, new QueryWrapper<>(supplyOrderDetail));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public SupplyOrderDetail selectById(@PathVariable Serializable id) {
        return this.supplyOrderDetailService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param supplyOrderDetail 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody SupplyOrderDetail supplyOrderDetail) {
        return this.supplyOrderDetailService.save(supplyOrderDetail);
    }

    /**
     * 修改数据
     *
     * @param supplyOrderDetail 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody SupplyOrderDetail supplyOrderDetail) {
        return this.supplyOrderDetailService.updateById(supplyOrderDetail);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.supplyOrderDetailService.removeByIds(idList);
    }
}