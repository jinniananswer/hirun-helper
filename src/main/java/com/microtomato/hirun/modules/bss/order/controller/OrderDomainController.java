package com.microtomato.hirun.modules.bss.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.order.entity.dto.*;
import com.microtomato.hirun.modules.bss.order.service.IOrderDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 订单领域控制器
 * @author: jinnian
 * @create: 2020sssss-02-12 00:21
 **/
@RestController
@Slf4j
@RequestMapping("/api/bss.order/order-domain")
public class OrderDomainController {

    @Autowired
    private IOrderDomainService domainService;

    @GetMapping("/getPendingTask")
    @RestResult
    public List<PendingTaskDTO> getPendingTask() {
        return this.domainService.queryPendingTask();
    }

    /**
     * 订单信息组件所用查询订单详情的方法
     * @param orderId
     * @return
     */
    @GetMapping("/getOrderDetail")
    @RestResult
    public OrderDetailDTO getOrderDetail(Long orderId) {
        return this.domainService.getOrderDetail(orderId);
    }

    @GetMapping("/queryCustOrderInfo")
    @RestResult
    public IPage<CustOrderInfoDTO> queryCustOrderInfo(CustOrderQueryDTO queryCond) {
        Page<CustOrderQueryDTO> page = new Page<>(queryCond.getPage(), queryCond.getLimit());
        return this.domainService.queryCustOrderInfos(queryCond, page);
    }

    @GetMapping("/queryOrderTasks")
    @RestResult
    public IPage<OrderTaskDTO> queryOrderTasks(OrderTaskQueryDTO condition) {
        return this.domainService.queryOrderTasks(condition);
    }

    @GetMapping("/queryEmployeeResults")
    @RestResult
    public List<EmployeeResultsDTO> queryEmployeeResults(EmployeeResultsQueryDTO queryCond) {
        return this.domainService.queryEmployeeResults(queryCond);
    }
}
