package com.microtomato.hirun.modules.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.user.service.IRoleMappingService;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Steven
 * @since 2019-11-17
 */
@RestController
@Slf4j
@RequestMapping("/api/user/role-mapping")
public class RoleMappingController {

    @Autowired
    private IRoleMappingService roleMappingServiceImpl;



}
