package com.microtomato.hirun.modules.college.teacher.mapper;

import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.college.teacher.entity.po.Teacher;

/**
 * (Teacher)表数据库访问层
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-09-17 01:10:28
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface TeacherMapper extends BaseMapper<Teacher> {

}