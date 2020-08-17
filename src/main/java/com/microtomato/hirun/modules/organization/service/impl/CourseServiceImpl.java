package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.organization.entity.dto.CourseTreeResponseDTO;
import com.microtomato.hirun.modules.organization.entity.po.Course;
import com.microtomato.hirun.modules.organization.mapper.CourseMapper;
import com.microtomato.hirun.modules.organization.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

    @Override
    public List<Course> queryEffectiveByParentCourseId(Long parentCourseId) {
        return this.list(Wrappers.<Course>lambdaQuery().eq(Course::getParentCourseId, parentCourseId)
                .eq(Course::getStatus, "0"));
    }
}
