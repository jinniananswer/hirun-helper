package com.microtomato.hirun.modules.organization.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.order.entity.po.Decorator;
import com.microtomato.hirun.modules.organization.entity.po.CourseFile;
import com.microtomato.hirun.modules.organization.service.ICourseFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author huanghua
 * @since 2020-07-21
 */
@RestController
@Slf4j
@RequestMapping("/api/organization/coursefile")
public class CourseFileController {

    @Autowired
    private ICourseFileService courseFileServiceImpl;

    @GetMapping("/queryCourseFileInfo")
    @RestResult
    public IPage<CourseFile> queryCourseFileInfo(CourseFile courseFile, int page, int size) {
        return this.courseFileServiceImpl.queryCourseFileInfo(courseFile, page, size);
    }

    @GetMapping("/queryCourseFileInfoByName")
    @RestResult
    public IPage<CourseFile> queryCourseFileInfoByName(String name, int page, int size) {
        return this.courseFileServiceImpl.queryCourseFileInfoByName(name, page, size);
    }

}
