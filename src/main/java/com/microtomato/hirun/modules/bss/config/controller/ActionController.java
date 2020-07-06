package com.microtomato.hirun.modules.bss.config.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.bss.config.service.IActionService;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 动作定义表 前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2020-04-27
 */
@RestController
@Slf4j
@RequestMapping("/api/bss.config/action")
public class ActionController {

    @Autowired
    private IActionService actionServiceImpl;



}
