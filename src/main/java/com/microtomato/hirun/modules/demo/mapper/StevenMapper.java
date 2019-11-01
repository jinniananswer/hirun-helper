package com.microtomato.hirun.modules.demo.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.microtomato.hirun.modules.demo.entity.po.Steven;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Steven
 * @since 2019-10-30
 */
@Mapper
@DS("sys")
public interface StevenMapper extends BaseMapper<Steven> {

}
