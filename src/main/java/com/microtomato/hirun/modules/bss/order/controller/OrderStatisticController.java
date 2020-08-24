package com.microtomato.hirun.modules.bss.order.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.order.service.IOrderStatisticService;
import com.microtomato.hirun.modules.organization.entity.dto.StatisticBarDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: hirun-helper
 * @description: 订单统计相关的控制器
 * @author: jinnian
 * @create: 2020-07-28 11:04
 **/

@RestController
@Slf4j
@RequestMapping("/api/bss.order/order-statistic")
public class OrderStatisticController {

    @Autowired
    private IOrderStatisticService orderStatisticService;

    @PostMapping("/consoleBar")
    @RestResult
    public StatisticBarDTO consoleBar() {
        return this.orderStatisticService.consoleBar();
    }
}
