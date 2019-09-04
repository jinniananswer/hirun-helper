package com.microtomato.hirun.modules.user.mapper;

import com.microtomato.hirun.modules.user.entity.po.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jinnian
 * @since 2019-07-29
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
