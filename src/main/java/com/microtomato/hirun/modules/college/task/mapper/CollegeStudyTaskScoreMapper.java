package com.microtomato.hirun.modules.college.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeStudyTaskScore;

/**
 * (CollegeStudyTaskScore)表数据库访问层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-21 23:54:00
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface CollegeStudyTaskScoreMapper extends BaseMapper<CollegeStudyTaskScore> {

}