package com.microtomato.hirun.modules.college.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTaskScore;

/**
 * (CollegeEmployeeTaskScore)表数据库访问层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-18 23:42:17
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface CollegeEmployeeTaskScoreMapper extends BaseMapper<CollegeEmployeeTaskScore> {

}