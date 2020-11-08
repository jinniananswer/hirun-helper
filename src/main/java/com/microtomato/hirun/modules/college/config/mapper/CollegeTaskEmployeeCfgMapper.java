package com.microtomato.hirun.modules.college.config.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeTaskEmployeeCfg;

/**
 * (CollegeTaskEmployeeCfg)表数据库访问层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-11-07 22:36:24
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface CollegeTaskEmployeeCfgMapper extends BaseMapper<CollegeTaskEmployeeCfg> {

}