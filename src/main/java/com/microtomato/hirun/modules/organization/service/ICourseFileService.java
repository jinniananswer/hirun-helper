package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.supply.entity.po.Supplier;
import com.microtomato.hirun.modules.organization.entity.po.Course;
import com.microtomato.hirun.modules.organization.entity.po.CourseFile;

import java.util.List;

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

    /**
     * 批量删除供应商
     * @param courseFileList
     * @return
     */
    boolean deleteCourseFileByIds(List<CourseFile> courseFileList);
}
