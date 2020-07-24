package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.organization.entity.po.Course;
import com.microtomato.hirun.modules.organization.entity.po.CourseFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author huanghua
 * @since 2020-07-21
 */
public interface ICourseFileService extends IService<CourseFile> {

    IPage<CourseFile> queryCourseFileInfo(CourseFile courseFile, int current, int size);

    IPage<CourseFile> queryCourseFileInfoByName(String name, int current, int size);
}
