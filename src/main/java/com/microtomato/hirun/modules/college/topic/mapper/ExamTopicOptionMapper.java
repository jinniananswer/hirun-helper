package com.microtomato.hirun.modules.college.topic.mapper;

import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.college.topic.entity.po.ExamTopicOption;

/**
 * (ExamTopicOption)表数据库访问层
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-09-04 11:39:06
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface ExamTopicOptionMapper extends BaseMapper<ExamTopicOption> {

}