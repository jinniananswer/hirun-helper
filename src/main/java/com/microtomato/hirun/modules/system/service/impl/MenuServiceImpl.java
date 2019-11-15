package com.microtomato.hirun.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.security.Role;
import com.microtomato.hirun.framework.util.Constants;
import com.microtomato.hirun.modules.system.entity.po.Menu;
import com.microtomato.hirun.modules.system.mapper.MenuMapper;
import com.microtomato.hirun.modules.system.service.IMenuService;
import com.microtomato.hirun.modules.user.entity.po.MenuRole;
import com.microtomato.hirun.modules.user.service.IMenuRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jinnian
 * @author Steven
 * @since 2019-09-05
 */
@Slf4j
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

	@Autowired
	private IMenuRoleService menuRoleServiceImpl;

	@Autowired
	private IMenuService menuServiceImpl;

	@Cacheable(value = "menuid-with-role", key = "#role.id")
	@Override
	public List<Long> listMenusByRole(Role role) {

		List<Long> myMenuIds = new ArrayList<>(100);

		LambdaQueryWrapper<MenuRole> lambdaQueryWrapper = Wrappers.lambdaQuery();
		lambdaQueryWrapper.eq(MenuRole::getRoleId, role.getId()).eq(MenuRole::getStatus, Constants.STATUS_OK);
		List<MenuRole> menuRoleList = menuRoleServiceImpl.list(lambdaQueryWrapper);
		menuRoleList.forEach(menuRole -> myMenuIds.add(menuRole.getMenuId()));

		return myMenuIds;
	}

	/**
	 * 超级管理员默认能看到所有菜单
	 */
	@Override
	public List<Long> listMenusForAdmin() {
		List<Long> myMenuIds = new ArrayList<>(100);

		LambdaQueryWrapper<MenuRole> lambdaQueryWrapper = Wrappers.lambdaQuery();
		lambdaQueryWrapper.eq(MenuRole::getStatus, Constants.STATUS_OK);
		List<Menu> menuList = menuServiceImpl.list();
		menuList.forEach(menu -> myMenuIds.add(menu.getMenuId()));

		return myMenuIds;
	}

	@Cacheable(value = "all-menus", key = "#isEmbedPage")
	@Override
	public Map<Long, Menu> listAllMenus(boolean isEmbedPage) {

		List<Menu> menuList = this.list(Wrappers.<Menu>lambdaQuery().eq(Menu::getEmbedPage, isEmbedPage));

		// 转换成 menuid 为 key 的 Map
		Map<Long, Menu> menuMap = new HashMap<>(menuList.size());
		menuList.forEach(
			menu -> menuMap.put(menu.getMenuId(), menu)
		);
		return menuMap;
	}

}
