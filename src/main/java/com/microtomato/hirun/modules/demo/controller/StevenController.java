package com.microtomato.hirun.modules.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.demo.service.IStevenService;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Steven
 * @since 2019-10-30
 */
@RestController
@Slf4j
@RequestMapping("/api/demo/steven")
public class StevenController {

    @Autowired
    private IStevenService stevenServiceImpl;



}
