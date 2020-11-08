package com.microtomato.hirun.modules.college.knowhow.mapper;

import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeQuestionReplyerCfg;

/**
 * (CollegeQuestionReplyerCfg)表数据库访问层
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-11-07 13:31:08
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface CollegeQuestionReplyerCfgMapper extends BaseMapper<CollegeQuestionReplyerCfg> {

}