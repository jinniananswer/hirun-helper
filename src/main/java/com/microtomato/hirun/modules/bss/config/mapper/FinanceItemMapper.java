package com.microtomato.hirun.modules.bss.config.mapper;

import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.bss.config.entity.po.FinanceItem;

/**
 * 财务科目表(FinanceItem)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-19 22:26:54
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface FinanceItemMapper extends BaseMapper<FinanceItem> {

}