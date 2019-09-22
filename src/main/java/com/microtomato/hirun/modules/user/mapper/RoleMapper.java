package com.microtomato.hirun.modules.user.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.microtomato.hirun.modules.user.entity.po.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 角色表
(归属组织的角色，职级的角色等) Mapper 接口
 * </p>
 *
 * @author Steven
 * @since 2019-09-22
 */
@Mapper
@DS("ins")
public interface RoleMapper extends BaseMapper<Role> {

}
