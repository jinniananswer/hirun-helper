package com.microtomato.hirun.modules.college.teacher.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.teacher.entity.po.Teacher;

import java.util.List;

/**
 * (Teacher)表服务接口
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-09-17 01:10:28
 */
public interface ITeacherService extends IService<Teacher> {

    List<Teacher> queryTeacher();

    Teacher getById(Long id);
}