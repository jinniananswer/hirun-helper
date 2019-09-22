package com.microtomato.hirun.modules.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.user.service.IMenuRoleService;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 角色下挂菜单 前端控制器
 * </p>
 *
 * @author Steven
 * @since 2019-09-22
 */
@RestController
@Slf4j
@RequestMapping("/api/user/menu-role")
public class MenuRoleController {

    @Autowired
    private IMenuRoleService menuRoleServiceImpl;



}
