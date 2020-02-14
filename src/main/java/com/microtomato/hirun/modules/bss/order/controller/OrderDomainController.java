package com.microtomato.hirun.modules.bss.order.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.order.entity.dto.PendingTaskDTO;
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
 * @create: 2020-02-12 00:21
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
}