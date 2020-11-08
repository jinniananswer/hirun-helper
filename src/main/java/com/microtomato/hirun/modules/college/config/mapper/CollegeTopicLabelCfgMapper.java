package com.microtomato.hirun.modules.college.config.mapper;

import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeTopicLabelCfg;

/**
 * (CollegeTopicLabelCfg)表数据库访问层
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-10-20 01:28:12
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface CollegeTopicLabelCfgMapper extends BaseMapper<CollegeTopicLabelCfg> {

}