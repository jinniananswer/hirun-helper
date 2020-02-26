package com.microtomato.hirun.modules.bss.config.mapper;

import com.microtomato.hirun.modules.bss.config.entity.po.PayItemCfg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;

/**
 * <p>
 * 收款项配置表 Mapper 接口
 * </p>
 *
 * @author jinnian
 * @since 2020-02-23
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface PayItemCfgMapper extends BaseMapper<PayItemCfg> {

}
