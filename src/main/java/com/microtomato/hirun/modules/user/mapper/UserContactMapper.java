package com.microtomato.hirun.modules.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.modules.user.entity.po.UserContact;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jinnian
 * @since 2019-11-02
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface UserContactMapper extends BaseMapper<UserContact> {

}
