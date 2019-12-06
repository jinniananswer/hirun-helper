package com.microtomato.hirun.modules.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.microtomato.hirun.modules.user.entity.po.MenuRole;
import com.microtomato.hirun.modules.user.mapper.MenuRoleMapper;
import com.microtomato.hirun.modules.user.service.IMenuRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色下挂菜单 服务实现类
 * </p>
 *
 * @author Steven
 * @since 2019-09-22
 */
@Slf4j
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {

    /**
     * 更新角色对应的菜单集
     *
     * @param roleId
     * @param menuIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateMenuRole(Long roleId, List<Long> menuIds) {
        remove(Wrappers.<MenuRole>lambdaUpdate().eq(MenuRole::getRoleId, roleId));

        Set<Long> longSet = new HashSet();
        longSet.addAll(menuIds);

        for (Long menuId : longSet) {
            MenuRole menuRole = MenuRole.builder().roleId(roleId).menuId(menuId).status("0").build();
            save(menuRole);
        }
    }

}
