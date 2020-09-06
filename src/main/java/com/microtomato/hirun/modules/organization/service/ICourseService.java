package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.organization.entity.dto.CourseTreeResponseDTO;
import com.microtomato.hirun.modules.organization.entity.po.Course;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jinnian
 * @since 2019-12-18
 */
public interface ICourseService extends IService<Course> {

    /**
     * 根据上级课程id查询所有课程
     * @param parentCourseId
     * @return
     */
    List<Course> queryEffectiveByParentCourseId(Long parentCourseId);

    IPage<Course> queryCourseInfo(Page<Course> page);
}
