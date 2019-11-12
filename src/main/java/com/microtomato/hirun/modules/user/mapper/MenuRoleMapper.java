package com.microtomato.hirun.modules.user.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.modules.user.entity.po.MenuRole;

/**
 * <p>
 * 角色下挂菜单 Mapper 接口
 * </p>
 *
 * @author Steven
 * @since 2019-09-22
 */
@Storage
@DS("ins")
public interface MenuRoleMapper extends BaseMapper<MenuRole> {

}
