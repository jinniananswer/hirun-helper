package com.microtomato.hirun.modules.organization.controller;

import com.microtomato.hirun.modules.organization.service.IEmployeeOrgRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jinnian
 * @since 2019-12-23
 */
@RestController
@Slf4j
@RequestMapping("/api/organization/employee-org-rel")
public class EmployeeOrgRelController {

    @Autowired
    private IEmployeeOrgRelService employeeOrgRelServiceImpl;



}
