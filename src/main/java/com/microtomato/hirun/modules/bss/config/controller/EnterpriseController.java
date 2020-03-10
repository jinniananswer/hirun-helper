package com.microtomato.hirun.modules.bss.config.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.bss.config.service.IEnterpriseService;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sunxin
 * @since 2020-03-10
 */
@RestController
@Slf4j
@RequestMapping("/api/bss.config/enterprise")
public class EnterpriseController {

    @Autowired
    private IEnterpriseService enterpriseServiceImpl;



}
