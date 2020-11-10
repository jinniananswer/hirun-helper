package com.microtomato.hirun.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.security.Role;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.Constants;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.system.entity.po.Menu;
import com.microtomato.hirun.modules.system.mapper.MenuMapper;
import com.microtomato.hirun.modules.system.service.IMenuService;
import com.microtomato.hirun.modules.user.entity.po.MenuRole;
import com.microtomato.hirun.modules.user.entity.po.MenuTemp;
import com.microtomato.hirun.modules.user.service.IMenuRoleService;
import com.microtomato.hirun.modules.user.service.IMenuTempService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

	@Autowired
	private IMenuService menuServiceImpl;

	@Autowired
	private IMenuTempService menuTempServiceImpl;

	//@Cacheable(value = "menu::listMenusByRole", key = "#role.id")
	@Override
	public List<Long> listMenusByRole(Role role) {

		List<Long> myMenuIds = new ArrayList<>(100);

		List<MenuRole> menuRoleList = menuRoleServiceImpl.list(
			Wrappers.<MenuRole>lambdaQuery()
				.select(MenuRole::getMenuId)
				.eq(MenuRole::getRoleId, role.getId())
				.eq(MenuRole::getStatus, Constants.STATUS_OK)
				.orderByAsc(MenuRole::getMenuId)
		);
		menuRoleList.forEach(menuRole -> myMenuIds.add(menuRole.getMenuId()));

		return myMenuIds;
	}

	/**
	 * 超级管理员默认能看到所有菜单
	 * 注： BSS只查类型为 P 和 H 的菜单。
	 */
	//@Cacheable(value = "menu::listMenusForAdmin", key = "")
	@Override
	public Set<Long> listMenusForAdmin() {
		Set<Long> rtn = new HashSet<>();
		List<Menu> menuList = menuServiceImpl.list(
			Wrappers.<Menu>lambdaQuery()
				.select(Menu::getMenuId)
				.ne(Menu::getDisabled, true)
				.in(Menu::getType, "P", "H")
				.orderByAsc(Menu::getMenuId)

		);
		menuList.forEach(menu -> rtn.add(menu.getMenuId()));
		return rtn;
	}

	//@Cacheable(value = "menu::listMenusForNormal", key = "")
	@Override
	public Set<Long> listMenusForNormal() {
		UserContext userContext = WebContextUtils.getUserContext();

		Set<Long> rtn = new HashSet<>(100);
		List<MenuTemp> menuTempList = menuTempServiceImpl.list(
			new QueryWrapper<MenuTemp>().lambda()
				.select(MenuTemp::getMenuId)
				.eq(MenuTemp::getUserId, userContext.getUserId())
				.gt(MenuTemp::getExpireDate, LocalDateTime.now())
				.orderByAsc(MenuTemp::getMenuId)
		);
		menuTempList.forEach(menuTemp -> rtn.add(menuTemp.getMenuId()));

		// 查询归属角色下有权访问的菜单ID
		List<Role> roles = userContext.getRoles();
		for (Role role : roles) {
			List<Long> menuids = menuServiceImpl.listMenusByRole(role);
			rtn.addAll(menuids);
		}

		return rtn;
	}

	//@Cacheable(value = "menu::listAllMenus", key = "")
	@Override
	public Map<Long, Menu> listAllMenus() {

		List<Menu> menuList = this.list(
			Wrappers.<Menu>lambdaQuery()
				.ne(Menu::getDisabled, true)
				.in(Menu::getType, "P", "H")
				.orderByAsc(Menu::getMenuId)
		);

		// 转换成 menuid 为 key 的 Map
		Map<Long, Menu> menuMap = new HashMap<>(menuList.size());
		menuList.forEach(
			menu -> menuMap.put(menu.getMenuId(), menu)
		);
		return menuMap;
	}

	/**
	 * 根据 menuUrl 查询 menuId
	 *
	 * @param menuUrl 菜单地址
	 * @return 菜单Id
	 */
	@Override
	//@Cacheable(value = "menu::getMenuId", key = "#menuUrl")
	public Long getMenuId(String menuUrl) {
		Menu one = menuServiceImpl.getOne(
			Wrappers.<Menu>lambdaQuery()
				.select(Menu::getMenuId)
				.eq(Menu::getMenuUrl, menuUrl)
				.ne(Menu::getDisabled, true)
				.orderByAsc(Menu::getMenuId)
		);
		if (null == one) {
			return -1L;
		}
		return one.getMenuId();
	}

	@Override
	public Set<Long> listPhoneMenusForAdmin() {
		Set<Long> rtn = new HashSet<>();
		List<Menu> menuList = menuServiceImpl.list(
				Wrappers.<Menu>lambdaQuery()
						.select(Menu::getMenuId)
						.ne(Menu::getDisabled, true)
						.in(Menu::getType, "M", "H")
						.orderByAsc(Menu::getMenuId)

		);
		menuList.forEach(menu -> rtn.add(menu.getMenuId()));
		return rtn;
	}

	@Override
	public Map<Long, Menu> listAllPhoneMenus() {

		List<Menu> menuList = this.list(
				Wrappers.<Menu>lambdaQuery()
						.ne(Menu::getDisabled, true)
						.in(Menu::getType, "M", "H")
						.orderByAsc(Menu::getMenuId)
		);

		// 转换成 menuid 为 key 的 Map
		Map<Long, Menu> menuMap = new HashMap<>(menuList.size());
		menuList.forEach(
				menu -> menuMap.put(menu.getMenuId(), menu)
		);
		return menuMap;
	}
}
