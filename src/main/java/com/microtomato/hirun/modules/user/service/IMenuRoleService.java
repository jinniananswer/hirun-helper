package com.microtomato.hirun.modules.user.service;

import com.microtomato.hirun.modules.user.entity.po.MenuRole;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 角色下挂菜单 服务类
 * </p>
 *
 * @author Steven
 * @since 2019-09-22
 */
public interface IMenuRoleService extends IService<MenuRole> {

    /**
     * 更新角色对应的菜单集
     *
     * @param roleId 角色Id
     * @param menuIds 角色对应的菜单Id集
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    void updateMenuRole(Long roleId, List<Long> menuIds);
}
