package com.microtomato.hirun.modules.college.teacher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.college.teacher.mapper.TeacherMapper;
import com.microtomato.hirun.modules.college.teacher.entity.po.Teacher;
import com.microtomato.hirun.modules.college.teacher.service.ITeacherService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * (Teacher)表服务实现类
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-09-17 01:10:28
 */
@Service("teacherService")
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements ITeacherService {

    @Autowired
    private TeacherMapper teacherMapper;


    @Override
    public List<Teacher> queryTeacher() {
        return this.list(new QueryWrapper<Teacher>().lambda().eq(Teacher::getStatus, "0"));
    }

    @Override
    public Teacher getById(Long id) {
        return this.getOne(new QueryWrapper<Teacher>().lambda().eq(Teacher::getId, id), false);
    }
}