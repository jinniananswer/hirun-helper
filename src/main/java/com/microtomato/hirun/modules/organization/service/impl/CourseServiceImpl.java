package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.organization.entity.dto.CourseTreeResponseDTO;
import com.microtomato.hirun.modules.organization.entity.po.Course;
import com.microtomato.hirun.modules.organization.entity.po.CourseFile;
import com.microtomato.hirun.modules.organization.mapper.CourseMapper;
import com.microtomato.hirun.modules.organization.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2019-12-18
 */
@Slf4j
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {
    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<Course> queryEffectiveByParentCourseId(Long parentCourseId) {
        return this.list(Wrappers.<Course>lambdaQuery().eq(Course::getParentCourseId, parentCourseId)
                .eq(Course::getStatus, "0"));
    }

    @Override
    public IPage<Course> queryCourseInfo(Page<Course> page) {
        return this.courseMapper.selectPage(page, new QueryWrapper<Course>().lambda().eq(Course::getStatus, "0"));
    }

    @Override
    public String getCourseNameByCourseId(Long courseId) {
        Course course = this.getOne(Wrappers.<Course>lambdaQuery().eq(Course::getCourseId, courseId), false);
        if (null != course){
            return course.getName();
        }
        return "";
    }
}
