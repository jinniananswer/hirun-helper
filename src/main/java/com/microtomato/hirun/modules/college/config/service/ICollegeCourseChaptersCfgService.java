package com.microtomato.hirun.modules.college.config.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeCourseChaptersCfg;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeQueryDTO;
import com.microtomato.hirun.modules.organization.entity.po.Employee;

import java.util.List;

/**
 * (CollegeCourseChaptersCfg)表服务接口
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-08-04 00:18:35
 */
public interface ICollegeCourseChaptersCfgService extends IService<CollegeCourseChaptersCfg> {

    /**
     * 根据课程ID查询所有章节配置
     * @param courseId
     * @return
     */
    List<CollegeCourseChaptersCfg> queryByCourseId(String courseId);

    /**
     * 根据课程ID集合查询所有章节配置
     * @param courseIdList
     * @return
     */
    List<CollegeCourseChaptersCfg> queryByCourseIdList(List<String> courseIdList);

}