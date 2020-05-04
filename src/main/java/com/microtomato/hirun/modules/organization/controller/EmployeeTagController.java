package com.microtomato.hirun.modules.organization.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.organization.service.IEmployeeTagService;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2020-05-04
 */
@RestController
@Slf4j
@RequestMapping("/api/organization/employee-tag")
public class EmployeeTagController {

    @Autowired
    private IEmployeeTagService employeeTagServiceImpl;



}
