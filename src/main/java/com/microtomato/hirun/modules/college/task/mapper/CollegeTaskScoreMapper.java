package com.microtomato.hirun.modules.college.task.mapper;

import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeTaskScore;

/**
 * (CollegeTaskScore)表数据库访问层
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-10-22 04:16:25
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface CollegeTaskScoreMapper extends BaseMapper<CollegeTaskScore> {

}