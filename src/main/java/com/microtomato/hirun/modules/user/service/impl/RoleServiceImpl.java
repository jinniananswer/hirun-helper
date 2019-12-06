package com.microtomato.hirun.modules.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.Constants;
import com.microtomato.hirun.modules.system.entity.po.Menu;
import com.microtomato.hirun.modules.system.service.IMenuService;
import com.microtomato.hirun.modules.user.entity.po.MenuRole;
import com.microtomato.hirun.modules.user.entity.po.Role;
import com.microtomato.hirun.modules.user.mapper.RoleMapper;
import com.microtomato.hirun.modules.user.service.IMenuRoleService;
import com.microtomato.hirun.modules.user.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色表
(归属组织的角色，职级的角色等) 服务实现类
 * </p>
 *
 * @author Steven
 * @since 2019-09-22
 */
@Slf4j
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private IMenuRoleService menuRoleServiceImpl;

    @Autowired
    private IMenuService menuServiceImpl;

    /**
     * 根据角色Id查对应的菜单
     *
     * @param roleId
     * @return
     */
    @Override
    public Set<Long> queryMenuId(Long roleId) {
        Set<Long> rtn = new HashSet<>();

        if (Constants.SUPER_ROLE_ID.equals(roleId)) {
            // 超级管理员查询所有菜单
            List<Menu> list = menuServiceImpl.list(
                Wrappers.<Menu>lambdaQuery().select(Menu::getMenuId)
            );
            list.forEach(menu -> rtn.add(menu.getMenuId()));
        } else {
            List<MenuRole> list = menuRoleServiceImpl.list(
                Wrappers.<MenuRole>lambdaQuery()
                    .select(MenuRole::getMenuId)
                    .eq(MenuRole::getStatus, "0")
                    .eq(MenuRole::getRoleId, roleId)
            );
            list.forEach(menuRole -> rtn.add(menuRole.getMenuId()));
        }

        return rtn;
    }

}
