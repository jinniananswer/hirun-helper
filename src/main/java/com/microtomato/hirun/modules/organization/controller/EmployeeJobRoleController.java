package com.microtomato.hirun.modules.organization.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.organization.service.IEmployeeJobRoleService;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jinnian
 * @since 2019-10-09
 */
@RestController
@Slf4j
@RequestMapping("api/organization/employee-job-role")
public class EmployeeJobRoleController {

    @Autowired
    private IEmployeeJobRoleService employeeJobRoleServiceImpl;



}
