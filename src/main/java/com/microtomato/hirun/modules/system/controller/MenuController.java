package com.microtomato.hirun.modules.system.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.data.TreeNode;
import com.microtomato.hirun.framework.security.Role;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TreeUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.system.entity.po.Menu;
import com.microtomato.hirun.modules.system.service.IMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jinnian
 * @author Steven
 * @since 2019-09-05
 */
@RestController
@Slf4j
@RequestMapping("api/system/menu/")
public class MenuController {

	@Autowired
	private IMenuService menuServiceImpl;

	@GetMapping("/list")
	@RestResult
	public List<TreeNode> getMenuTree() {

		List<Role> roles = WebContextUtils.getUserContext().getRoles();
		Set<Long> menuidSet = new HashSet<>();

		// 查询归属角色下有权访问的菜单ID
		for (Role role : roles) {
			List<Long> menuids = menuServiceImpl.listMenusByRole(role);
			menuidSet.addAll(menuids);
		}

		// 查询所有非嵌入式菜单集合
		Map<Long, Menu> menuMap = menuServiceImpl.listAllMenus(false);

		// 根据权限进行过滤
		Set<String> menuUrls = new HashSet<>();
		Map<Long, Menu> filteredMenuMap = new HashMap<>(20);
		for (Menu currMenu : menuMap.values()) {

			// 如果有该菜单权限
			if (menuidSet.contains(currMenu.getMenuId())) {
				filteredMenuMap.put(currMenu.getMenuId(), currMenu);
				menuUrls.add(currMenu.getMenuUrl());

				// 递归增加父菜单
				addParentMenus(currMenu, filteredMenuMap, menuMap);
			}
		}

		// 将有权访问的菜单 URL 设置到上下文中
		WebContextUtils.getUserContext().setMenuUrls(menuUrls);

		List<Menu> menus = new ArrayList(filteredMenuMap.values());
		if (ArrayUtils.isEmpty(menus)) {
			return null;
		}

		// 构建菜单结构树
		List<TreeNode> nodes = new ArrayList<>();
		for (Menu menu : menus) {
			TreeNode node = new TreeNode();
			node.setId(menu.getMenuId() + "");
			if (null != menu.getParentMenuId()) {
				node.setParentId(menu.getParentMenuId() + "");
			}
			node.setNode(menu);
			nodes.add(node);
		}

		List<TreeNode> tree = TreeUtils.build(nodes);
		return tree;
	}

	/**
	 * 递归将子菜单所有的父菜单添加进去
	 *
	 * @param currMenu
	 * @param filteredMenuMap
	 * @param menuMap
	 */
	private void addParentMenus(Menu currMenu, Map<Long, Menu> filteredMenuMap, Map<Long, Menu> menuMap) {
		Long parentMenuId = currMenu.getParentMenuId();
		if (null != parentMenuId) {
			Menu parentMenu = menuMap.get(parentMenuId);
			filteredMenuMap.put(parentMenu.getMenuId(), parentMenu);
			addParentMenus(parentMenu, filteredMenuMap, menuMap);
		}
	}
}
