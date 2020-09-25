package com.microtomato.hirun.modules.bss.order.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderInspectDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderMaterialContractDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.QueryMaterialCondDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.microtomato.hirun.modules.bss.order.entity.po.OrderMaterialContract;
import com.microtomato.hirun.modules.bss.order.service.IOrderMaterialContractService;
import com.microtomato.hirun.framework.annotation.RestResult;

import java.io.Serializable;
import java.util.List;

/**
 * (OrderMaterialContract)表控制层
 *
 * @author liuhui7
 * @version 1.0.0
 * @date 2020-09-24 00:05:42
 */
@RestController
@Slf4j
@RequestMapping("/api/bss.order/order-material-contract")
public class OrderMaterialContractController {

    /**
     * 服务对象
     */
    @Autowired
    private IOrderMaterialContractService orderMaterialContractService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param orderMaterialContract 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<OrderMaterialContract> selectByPage(Page<OrderMaterialContract> page, OrderMaterialContract orderMaterialContract) {
        return this.orderMaterialContractService.page(page, new QueryWrapper<>(orderMaterialContract));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public OrderMaterialContract selectById(@PathVariable Serializable id) {
        return this.orderMaterialContractService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param orderMaterialContract 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody OrderMaterialContract orderMaterialContract) {
        return this.orderMaterialContractService.save(orderMaterialContract);
    }

    /**
     * 修改数据
     *
     * @param orderMaterialContract 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody OrderMaterialContract orderMaterialContract) {
        return this.orderMaterialContractService.updateById(orderMaterialContract);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.orderMaterialContractService.removeByIds(idList);
    }

    @GetMapping("/queryMaterialContract")
    @RestResult
    public List<OrderMaterialContractDTO> queryMaterialContracts(QueryMaterialCondDTO queryCond){
        log.debug(queryCond.toString());
        return orderMaterialContractService.queryMaterialContracts(queryCond);
    }
}