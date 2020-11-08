package com.microtomato.hirun.modules.college.knowhow.mapper;

import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeReply;

/**
 * (CollegeReply)表数据库访问层
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-08-16 16:10:21
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface CollegeReplyMapper extends BaseMapper<CollegeReply> {

}