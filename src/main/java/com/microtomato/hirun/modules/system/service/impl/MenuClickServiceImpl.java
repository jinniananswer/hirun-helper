package com.microtomato.hirun.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.system.entity.po.Menu;
import com.microtomato.hirun.modules.system.entity.po.MenuClick;
import com.microtomato.hirun.modules.system.mapper.MenuClickMapper;
import com.microtomato.hirun.modules.system.service.IMenuClickService;
import com.microtomato.hirun.modules.system.service.IMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateClicks(Long userId, Long menuId, Long clicks) {
        int i = menuClickMapper.updateClicks(userId, menuId, clicks);
        return i > 0 ? true : false;
    }

    /**
     * 查热点菜单
     *
     * @param userId
     * @return
     */
    @Override
    public List<Menu> hostMenus(Long userId) {
        List<Menu> menus = menuClickMapper.hostMenus(userId);
        return filter(menus);
    }

    /**
     * 快捷菜单权限过滤
     *
     * @param menus
     */
    private List<Menu> filter(List<Menu> menus) {
        List<Menu> rtn = new ArrayList<>();

        UserContext userContext = WebContextUtils.getUserContext();
        Set<Long> menuIdSet = null;
        if (userContext.isAdmin()) {
            menuIdSet = menuServiceImpl.listMenusForAdmin();
        } else {
            menuIdSet = menuServiceImpl.listMenusForNormal();
        }

        Iterator<Menu> iter = menus.iterator();
        while (iter.hasNext()) {
            Menu menu = iter.next();
            if (menuIdSet.contains(menu.getMenuId())) {
                rtn.add(menu);
                if (rtn.size() == 8) {
                    // 只展示 8 条数据
                    break;
                }
            }
        }

        return rtn;
    }

}
