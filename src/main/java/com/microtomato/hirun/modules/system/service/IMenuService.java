package com.microtomato.hirun.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.framework.security.Role;
import com.microtomato.hirun.modules.system.entity.po.Menu;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jinnian
 * @author Steven
 * @since 2019-09-05
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 获取菜单集合
     *
     * @param isEmbedPage
     * @return
     */
    Map<Long, Menu> listAllMenus(boolean isEmbedPage);

    /**
     * 根据角色获取菜单 ID 集合
     *
     * @param role
     * @return
     */
    List<Long> listMenusByRole(Role role);

}
