package com.microtomato.hirun.modules.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.system.entity.po.Menu;
import com.microtomato.hirun.modules.system.entity.po.MenuClick;
import com.microtomato.hirun.modules.system.mapper.MenuClickMapper;
import com.microtomato.hirun.modules.system.service.IMenuClickService;
import com.microtomato.hirun.modules.system.service.IMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单点击数统计 服务实现类
 * </p>
 *
 * @author Steven
 * @since 2019-12-10
 */
@Slf4j
@Service
public class MenuClickServiceImpl extends ServiceImpl<MenuClickMapper, MenuClick> implements IMenuClickService {

    @Autowired
    private MenuClickMapper menuClickMapper;

    @Autowired
    private IMenuService menuServiceImpl;

    @Override
    public boolean updateClicks(Long userId, Long menuId, Long clicks) {
        int i = menuClickMapper.updateClicks(userId, menuId, clicks);
        return 0 == i ? false : true;
    }

    /**
     * 查热点菜单
     *
     * @param userId
     * @return
     */
    @Override
    public List<Menu> hostMenus(Long userId) {
        List<MenuClick> list = list(
            Wrappers.<MenuClick>lambdaQuery()
                .select(MenuClick::getMenuId)
                .eq(MenuClick::getUserId, userId)
                .orderByDesc(MenuClick::getClicks)
                .last("LIMIT 8")
        );

        List<Long> menuIds = list.stream().map(MenuClick::getMenuId).collect(Collectors.toList());

        List<Menu> menuList = menuServiceImpl.list(
            Wrappers.<Menu>lambdaQuery()
                .select(Menu::getMenuId, Menu::getTitle, Menu::getMenuUrl)
                .in(Menu::getMenuId, menuIds)
        );

        return menuList;
    }

}
