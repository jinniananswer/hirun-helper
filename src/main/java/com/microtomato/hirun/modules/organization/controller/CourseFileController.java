package com.microtomato.hirun.modules.organization.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.bss.order.entity.po.Decorator;
import com.microtomato.hirun.modules.bss.supply.entity.po.Supplier;
import com.microtomato.hirun.modules.organization.entity.po.CourseFile;
import com.microtomato.hirun.modules.organization.service.ICourseFileService;
import com.microtomato.hirun.modules.system.entity.po.UploadFile;
import com.microtomato.hirun.modules.system.service.IUploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Optional;

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

    @Value("${hirun.data.path}")
    private String dataPath;

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

    @Autowired
    private IUploadFileService uploadFileService;

    @PostMapping("addUploadCourseFile")
    @RestResult
    public void addUploadCourseFile(@RequestBody String fileId) {
        UploadFile uploadFile = uploadFileService.getByFileId(fileId.substring(0, fileId.length()-1));
        if (null != uploadFile) {
            String realPath = FilenameUtils.concat(dataPath, uploadFile.getFilePath());
            courseFileServiceImpl.save(CourseFile.builder()
                    .name(uploadFile.getFileName())
                    .storagePath(realPath).status("0").build());
        }
    }
}
