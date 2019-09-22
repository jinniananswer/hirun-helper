package com.microtomato.hirun.modules.user.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.microtomato.hirun.modules.user.entity.po.MenuRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 角色下挂菜单 Mapper 接口
 * </p>
 *
 * @author Steven
 * @since 2019-09-22
 */
@Mapper
@DS("ins")
public interface MenuRoleMapper extends BaseMapper<MenuRole> {

}
