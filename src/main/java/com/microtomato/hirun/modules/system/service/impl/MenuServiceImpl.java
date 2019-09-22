package com.microtomato.hirun.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.security.Role;
import com.microtomato.hirun.framework.utils.Constants;
import com.microtomato.hirun.framework.utils.WebContextUtil;
import com.microtomato.hirun.modules.system.entity.po.Menu;
import com.microtomato.hirun.modules.system.mapper.MenuMapper;
import com.microtomato.hirun.modules.system.service.IMenuService;
import com.microtomato.hirun.modules.user.entity.po.MenuRole;
import com.microtomato.hirun.modules.user.service.IMenuRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

	@Override
	public List<Menu> listAllMenus() {

		// 查询全部菜单
		Map<Integer, Menu> menuMap = listToMap(this.list());

		// 查询用户归属角色下对应的菜单集
		Set<Integer> myMenuIds = new HashSet<>();
		List<Role> roles = WebContextUtil.getUserContext().getRoles();
		for (Role role : roles) {
			LambdaQueryWrapper<MenuRole> lambdaQueryWrapper = Wrappers.lambdaQuery();
			lambdaQueryWrapper.eq(MenuRole::getRoleId, role.getId()).eq(MenuRole::getStatus, Constants.STATUS_OK);
			List<MenuRole> menuRoleList = menuRoleServiceImpl.list(lambdaQueryWrapper);
			menuRoleList.forEach(menuRole -> myMenuIds.add(menuRole.getMenuId()));
		}

		Set<String> menuUrls = new HashSet<>();

		// 过滤
		Map<Integer, Menu> filteredMenuMap = new HashMap<>(20);
		for (Menu currMenu : menuMap.values()) {
			if (myMenuIds.contains(currMenu.getMenuId())) {
				filteredMenuMap.put(currMenu.getMenuId(), currMenu);
				menuUrls.add(currMenu.getMenuUrl());

				// 递归增加父菜单
				addParentMenus(currMenu, filteredMenuMap, menuMap);
			}
		}

		WebContextUtil.getUserContext().setMenuUrls(menuUrls);

		return new ArrayList(filteredMenuMap.values());
	}

	/**
	 * 递归将子菜单所有的父菜单添加进去
	 *
	 * @param currMenu
	 * @param filteredMenuMap
	 * @param menuMap
	 */
	private void addParentMenus(Menu currMenu, Map<Integer, Menu> filteredMenuMap, Map<Integer, Menu> menuMap) {
		Integer parentMenuId = currMenu.getParentMenuId();
		if (null != parentMenuId) {
			Menu parentMenu = menuMap.get(parentMenuId);
			filteredMenuMap.put(parentMenu.getMenuId(), parentMenu);
			addParentMenus(parentMenu, filteredMenuMap, menuMap);
		}
	}

	/**
	 * List 转 Map
	 *
	 * @param menus
	 * @return
	 */
	private Map<Integer, Menu> listToMap(List<Menu> menus) {
		Map<Integer, Menu> menuMap = new HashMap<>(menus.size());
		menus.forEach(
			menu -> menuMap.put(menu.getMenuId(), menu)
		);
		return menuMap;
	}

}
