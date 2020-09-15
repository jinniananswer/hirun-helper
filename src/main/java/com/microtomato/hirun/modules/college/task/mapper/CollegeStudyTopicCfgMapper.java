package com.microtomato.hirun.modules.college.task.mapper;

import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeStudyTopicCfg;

/**
 * (CollegeStudyTopicCfg)表数据库访问层
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-09-16 01:58:14
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface CollegeStudyTopicCfgMapper extends BaseMapper<CollegeStudyTopicCfg> {

}