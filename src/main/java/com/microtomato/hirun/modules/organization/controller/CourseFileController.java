package com.microtomato.hirun.modules.organization.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.bss.order.entity.po.Decorator;
import com.microtomato.hirun.modules.bss.supply.entity.po.Supplier;
import com.microtomato.hirun.modules.organization.entity.po.CourseFile;
import com.microtomato.hirun.modules.organization.service.ICourseFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/updateCourseFileById")
    @RestResult
    public boolean updateCourseFileById(@RequestBody CourseFile courseFile){
        System.out.println(courseFile);
        return this.courseFileServiceImpl.updateById(courseFile);
    }

    @PostMapping("addCourseFile")
    @RestResult
    public boolean addCourseFile(@RequestBody CourseFile courseFile){
        courseFile.setStatus("0");
        return this.courseFileServiceImpl.save(courseFile);
    }

    @PostMapping("deleteCourseFileById")
    @RestResult
    public boolean deleteCourseFileById(@RequestBody CourseFile courseFile){
        courseFile.setStatus("1");
        return this.courseFileServiceImpl.updateById(courseFile);
    }

    @PostMapping("deleteCourseFileByIds")
    @RestResult
    public boolean deleteCourseFileByIds(@RequestBody List<CourseFile> courseFileList){
        System.out.println(courseFileList);
        if(ArrayUtils.isNotEmpty(courseFileList)){
            for (CourseFile courseFile : courseFileList){
                courseFile.setStatus("1");
            }
        }
        return this.courseFileServiceImpl.deleteCourseFileByIds(courseFileList);
    }
}
