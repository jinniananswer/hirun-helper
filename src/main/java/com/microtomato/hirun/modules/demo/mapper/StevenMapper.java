package com.microtomato.hirun.modules.demo.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.modules.demo.entity.po.Steven;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Steven
 * @since 2019-10-30
 */
@Storage
@DS("sys")
public interface StevenMapper extends BaseMapper<Steven> {

}
