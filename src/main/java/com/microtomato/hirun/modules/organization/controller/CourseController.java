package com.microtomato.hirun.modules.organization.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.organization.entity.dto.CourseTreeResponseDTO;
import com.microtomato.hirun.modules.organization.entity.po.Course;
import com.microtomato.hirun.modules.organization.service.ICourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 查询所有课程树状结构
     * @return
     */
    @GetMapping("qeuryCourseTree")
    @RestResult
    public List<CourseTreeResponseDTO> qeuryCourseTree() {
        return this.queryCourseByParentCourseId(-1L);
    }

    private List<CourseTreeResponseDTO> queryCourseByParentCourseId(Long parentCourseId){
        List<CourseTreeResponseDTO> result = new ArrayList<>();
        List<Course> courseList = courseServiceImpl.queryEffectiveByParentCourseId(parentCourseId);
        if (ArrayUtils.isNotEmpty(courseList) && courseList.size() > 0){
            for (Course course : courseList){
                CourseTreeResponseDTO courseTreeResponseDTO = new CourseTreeResponseDTO();
                courseTreeResponseDTO.setStudyName(course.getName());
                courseTreeResponseDTO.setStudyId(course.getCourseId());
                //根据本级课程ID查询下级课程
                List<CourseTreeResponseDTO> courseTreeResponseDTOList = this.queryCourseByParentCourseId(course.getCourseId());
                courseTreeResponseDTO.setChildren(courseTreeResponseDTOList);
                if (ArrayUtils.isEmpty(courseTreeResponseDTOList) || courseTreeResponseDTOList.size() == 0){
                    courseTreeResponseDTO.setCourseFlag(true);
                }else {
                    courseTreeResponseDTO.setCourseFlag(false);
                }
                result.add(courseTreeResponseDTO);
            }
        }
        return result;
    }

    @GetMapping("queryCourseInfo")
    @RestResult
    public IPage<Course> queryCourseInfo(Page<Course> page) {
        return this.courseServiceImpl.queryCourseInfo(page);
    }

}
