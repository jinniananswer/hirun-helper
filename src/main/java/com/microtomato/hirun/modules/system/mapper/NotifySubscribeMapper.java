package com.microtomato.hirun.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.modules.system.entity.po.NotifySubscribe;

/**
 * <p>
 * 消息订阅表 Mapper 接口
 * </p>
 *
 * @author Steven
 * @since 2019-11-11
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface NotifySubscribeMapper extends BaseMapper<NotifySubscribe> {

}
