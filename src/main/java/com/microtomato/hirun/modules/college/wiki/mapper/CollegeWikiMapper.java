package com.microtomato.hirun.modules.college.wiki.mapper;

import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.college.wiki.entity.po.CollegeWiki;

/**
 * (CollegeWiki)表数据库访问层
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-10-28 02:20:47
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface CollegeWikiMapper extends BaseMapper<CollegeWiki> {

}