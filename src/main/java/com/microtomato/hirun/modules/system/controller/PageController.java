package com.microtomato.hirun.modules.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.system.service.IPageService;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 不用挂菜单的页面 前端控制器
 * </p>
 *
 * @author Steven
 * @since 2019-11-15
 */
@RestController
@Slf4j
@RequestMapping("/api/system/page")
public class PageController {

    @Autowired
    private IPageService pageServiceImpl;



}
