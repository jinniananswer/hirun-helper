package com.microtomato.hirun.modules.bss.supply.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.microtomato.hirun.modules.bss.supply.entity.po.SupplierBrand;
import com.microtomato.hirun.modules.bss.supply.service.ISupplierBrandService;
import com.microtomato.hirun.framework.annotation.RestResult;

import java.io.Serializable;
import java.util.List;

/**
 * 供应商品牌表(SupplySupplierBrand)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-05 17:46:45
 */
@RestController
@RequestMapping("/api/SupplySupplierBrand")
public class SupplierBrandController {

    /**
     * 服务对象
     */
    @Autowired
    private ISupplierBrandService supplySupplierBrandService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param supplierBrand 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<SupplierBrand> selectByPage(Page<SupplierBrand> page, SupplierBrand supplierBrand) {
        return this.supplySupplierBrandService.page(page, new QueryWrapper<>(supplierBrand));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public SupplierBrand selectById(@PathVariable Serializable id) {
        return this.supplySupplierBrandService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param supplierBrand 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody SupplierBrand supplierBrand) {
        return this.supplySupplierBrandService.save(supplierBrand);
    }

    /**
     * 修改数据
     *
     * @param supplierBrand 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody SupplierBrand supplierBrand) {
        return this.supplySupplierBrandService.updateById(supplierBrand);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.supplySupplierBrandService.removeByIds(idList);
    }
}