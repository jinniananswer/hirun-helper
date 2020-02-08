package com.microtomato.hirun.modules.bss.config.controller;

import com.microtomato.hirun.modules.bss.config.service.IOrderStatusCfgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 订单阶段及状态配置表 前端控制器
 * </p>
 *
 * @author jinnian
 * @since 2020-02-07
 */
@RestController
@Slf4j
@RequestMapping("/api/bss.config/order-status-cfg")
public class OrderStatusCfgController {

    @Autowired
    private IOrderStatusCfgService orderStatusCfgServiceImpl;



}
