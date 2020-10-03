package com.microtomato.hirun.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.framework.security.Role;
import com.microtomato.hirun.modules.system.entity.po.Menu;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
     * 超级管理员默认能看到所有菜单
     *
     * @return
     */
    Set<Long> listMenusForAdmin();

    /**
     * 普通员工登录所能看到的菜单
     *
     * @return
     */
    Set<Long> listMenusForNormal();

    /**
     * 获取菜单集合
     *
     * @return
     */
    //@Cacheable(value = "all-menus")
    Map<Long, Menu> listAllMenus();

    /**
     * 根据角色获取菜单 ID 集合
     *
     * @param role
     * @return
     */
    List<Long> listMenusByRole(Role role);

    /**
     * 根据 menuUrl 查询 menuId
     *
     * @param menuUrl 菜单地址
     * @return 菜单Id
     */
    //@Cacheable(value = "menu-url-to-id", key = "#menuUrl")
    Long getMenuId(String menuUrl);

    Set<Long> listPhoneMenusForAdmin();

    Map<Long, Menu> listAllPhoneMenus();
}
