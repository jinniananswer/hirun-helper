package com.microtomato.hirun.modules.bss.config.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.bss.config.service.IOrderStatusTransCfgService;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 订单状态转换配置表 前端控制器
 * </p>
 *
 * @author jinnian
 * @since 2020-02-07
 */
@RestController
@Slf4j
@RequestMapping("/api/bss.config/order-status-trans-cfg")
public class OrderStatusTransCfgController {

    @Autowired
    private IOrderStatusTransCfgService orderStatusTransCfgServiceImpl;



}
