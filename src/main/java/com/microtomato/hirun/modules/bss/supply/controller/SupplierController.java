package com.microtomato.hirun.modules.bss.supply.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.supply.entity.dto.SupplierQueryDTO;
import com.microtomato.hirun.modules.bss.supply.entity.po.Supplier;
import com.microtomato.hirun.modules.bss.supply.service.ISupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 供应商表(SupplySupplier)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-05 17:41:16
 */
@RestController
@Slf4j
@RequestMapping("/api/SupplySupplier")
public class SupplierController {

    /**
     * 服务对象
     */
    @Autowired
    private ISupplierService supplySupplierService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param supplier 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<Supplier> selectByPage(Page<Supplier> page, Supplier supplier) {
        return this.supplySupplierService.page(page, new QueryWrapper<>(supplier));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public Supplier selectById(@PathVariable Serializable id) {
        return this.supplySupplierService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param supplier 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody Supplier supplier) {
        return this.supplySupplierService.save(supplier);
    }

    /**
     * 修改数据
     *
     * @param supplier 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody Supplier supplier) {
        return this.supplySupplierService.updateById(supplier);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.supplySupplierService.removeByIds(idList);
    }

    @GetMapping("queryByNameAndId")
    @RestResult
    public IPage<Supplier> queryByNameAndId(SupplierQueryDTO supplierQueryDTO){
        return this.supplySupplierService.queryByNameAndId(supplierQueryDTO);
    }
}