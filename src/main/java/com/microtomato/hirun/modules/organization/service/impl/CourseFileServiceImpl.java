package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.supply.entity.po.Supplier;
import com.microtomato.hirun.modules.organization.entity.po.CourseFile;
import com.microtomato.hirun.modules.organization.mapper.CourseFileMapper;
import com.microtomato.hirun.modules.organization.mapper.CourseMapper;
import com.microtomato.hirun.modules.organization.service.ICourseFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author huanghua
 * @since 2020-07-21
 */
@Slf4j
@Service
public class CourseFileServiceImpl extends ServiceImpl<CourseFileMapper, CourseFile> implements ICourseFileService {

    @Autowired
    private CourseFileMapper courseFileMapper;

    @Override
    public IPage<CourseFile> queryCourseFileInfo(CourseFile courseFile, int current, int size) {
        LambdaQueryWrapper<CourseFile> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(null != courseFile.getFileId(), CourseFile::getFileId, courseFile.getFileId())
                .eq(null != courseFile.getCourseId(), CourseFile::getCourseId, courseFile.getCourseId())
                .like(StringUtils.isNotEmpty(courseFile.getName()), CourseFile::getName, courseFile.getName())
                .like(StringUtils.isNotEmpty(courseFile.getStoragePath()), CourseFile::getStoragePath, courseFile.getStoragePath());
        Page<CourseFile> page = new Page<>(current, size);
        IPage<CourseFile> courseFilePages = courseFileMapper.selectPage(page, lambdaQueryWrapper);

        return courseFilePages;
    }

    @Override
    public IPage<CourseFile> queryCourseFileInfoByName(String name, int current, int size) {
        LambdaQueryWrapper<CourseFile> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.like(CourseFile::getName, name);
        Page<CourseFile> page = new Page<>(current, size);
        IPage<CourseFile> courseFilePages = courseFileMapper.selectPage(page, lambdaQueryWrapper);

        return courseFilePages;
    }

    @Override
    public boolean deleteCourseFileByIds(List<CourseFile> courseFileList){
        return super.updateBatchById(courseFileList);
    }
}
