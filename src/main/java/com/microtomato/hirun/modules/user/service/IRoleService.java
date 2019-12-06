package com.microtomato.hirun.modules.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.user.entity.po.Role;

import java.util.Set;

/**
 * <p>
 * 角色表
(归属组织的角色，职级的角色等) 服务类
 * </p>
 *
 * @author Steven
 * @since 2019-09-22
 */
public interface IRoleService extends IService<Role> {

    /**
     * 根据角色Id查对应的菜单
     *
     * @param roleId 角色
     * @return 角色对应的菜单集
     */
    Set<Long> queryMenuId(Long roleId);
}
