package com.microtomato.hirun.modules.bss.customer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.bss.customer.service.IProjectOriginalActionService;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 客户原始动作记录 前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2020-04-30
 */
@RestController
@Slf4j
@RequestMapping("/api/bss.customer/project-original-action")
public class ProjectOriginalActionController {

    @Autowired
    private IProjectOriginalActionService projectOriginalActionServiceImpl;



}
