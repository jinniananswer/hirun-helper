package com.microtomato.hirun.modules.organization.controller;

import com.microtomato.hirun.modules.organization.service.ICourseService;
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
 * @since 2019-12-18
 */
@RestController
@Slf4j
@RequestMapping("/api/organization/course")
public class CourseController {

    @Autowired
    private ICourseService courseServiceImpl;



}
