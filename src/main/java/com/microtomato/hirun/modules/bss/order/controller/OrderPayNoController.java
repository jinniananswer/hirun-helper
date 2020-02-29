package com.microtomato.hirun.modules.bss.order.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayNo;
import com.microtomato.hirun.modules.bss.order.service.IOrderPayNoService;
import com.microtomato.hirun.framework.annotation.RestResult;

import java.io.Serializable;
import java.util.List;

/**
 * 订单支付流水表表(OrderPayNo)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-02-29 11:59:42
 */
@RestController
@RequestMapping("/api/OrderPayNo")
public class OrderPayNoController {

    /**
     * 服务对象
     */
    @Autowired
    private IOrderPayNoService orderPayNoService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param orderPayNo 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<OrderPayNo> selectByPage(Page<OrderPayNo> page, OrderPayNo orderPayNo) {
        return this.orderPayNoService.page(page, new QueryWrapper<>(orderPayNo));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public OrderPayNo selectById(@PathVariable Serializable id) {
        return this.orderPayNoService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param orderPayNo 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody OrderPayNo orderPayNo) {
        return this.orderPayNoService.save(orderPayNo);
    }

    /**
     * 修改数据
     *
     * @param orderPayNo 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody OrderPayNo orderPayNo) {
        return this.orderPayNoService.updateById(orderPayNo);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.orderPayNoService.removeByIds(idList);
    }
}