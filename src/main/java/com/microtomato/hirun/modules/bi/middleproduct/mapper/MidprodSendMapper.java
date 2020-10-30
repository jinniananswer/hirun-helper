package com.microtomato.hirun.modules.bi.middleproduct.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.bi.middleproduct.entity.po.MidprodSend;

/**
 * (MidprodSend)表数据库访问层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-26 00:48:48
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface MidprodSendMapper extends BaseMapper<MidprodSend> {

}