package com.microtomato.hirun.modules.bss.order.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.bss.order.service.INormalPayNoService;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 非主营支付流水表 前端控制器
 * </p>
 *
 * @author sunxin
 * @since 2020-03-26
 */
@RestController
@Slf4j
@RequestMapping("/api/bss.order/normal-pay-no")
public class NormalPayNoController {

    @Autowired
    private INormalPayNoService normalPayNoServiceImpl;



}
