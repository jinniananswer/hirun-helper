package com.microtomato.hirun.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

import java.util.List;

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
        return menuClickMapper.hostMenus(userId);
    }

}
