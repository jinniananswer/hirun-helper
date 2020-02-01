package com.microtomato.hirun.modules.bss.customer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import com.microtomato.hirun.modules.bss.customer.service.ICustBaseService;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 客户信息 前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2020-02-01
 */
@RestController
@Slf4j
@RequestMapping("/api/customer/cust-base")
public class CustBaseController {

    @Autowired
    private ICustBaseService custBaseServiceImpl;



}
