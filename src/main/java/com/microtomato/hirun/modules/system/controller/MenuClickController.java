package com.microtomato.hirun.modules.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.system.service.IMenuClickService;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 菜单点击数统计 前端控制器
 * </p>
 *
 * @author Steven
 * @since 2019-12-10
 */
@RestController
@Slf4j
@RequestMapping("/api/system/menu-click")
public class MenuClickController {

    @Autowired
    private IMenuClickService menuClickServiceImpl;



}
