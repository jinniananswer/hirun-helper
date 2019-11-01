package com.microtomato.hirun.modules.organization.controller;

import com.microtomato.hirun.modules.organization.service.IEmployeeWorkExperienceService;
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
 * @since 2019-10-19
 */
@RestController
@Slf4j
@RequestMapping("api/organization/employee-work-experience")
public class EmployeeWorkExperienceController {

    @Autowired
    private IEmployeeWorkExperienceService employeeWorkExperienceServiceImpl;



}
