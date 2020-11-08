package com.microtomato.hirun.modules.college.config.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeTaskJobCfg;

/**
 * (CollegeTaskJobCfg)表数据库访问层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-16 03:19:46
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface CollegeTaskJobCfgMapper extends BaseMapper<CollegeTaskJobCfg> {

}