package com.microtomato.hirun.modules.system.service;

import com.microtomato.hirun.modules.system.entity.po.Menu;
import com.microtomato.hirun.modules.system.entity.po.MenuClick;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单点击数统计 服务类
 * </p>
 *
 * @author Steven
 * @since 2019-12-10
 */
public interface IMenuClickService extends IService<MenuClick> {

    boolean updateClicks(Long userId, Long menuId, Long clicks);

    /**
     * 查热点菜单
     *
     * @param userId
     * @return
     */
    List<Menu> hostMenus(Long userId);
}
