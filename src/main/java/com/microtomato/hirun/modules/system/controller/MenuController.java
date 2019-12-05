package com.microtomato.hirun.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.data.TreeNode;
import com.microtomato.hirun.framework.security.Role;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.*;
import com.microtomato.hirun.modules.system.entity.dto.MenuNode;
import com.microtomato.hirun.modules.system.entity.po.Menu;
import com.microtomato.hirun.modules.system.entity.po.Page;
import com.microtomato.hirun.modules.system.service.IMenuService;
import com.microtomato.hirun.modules.system.service.IPageService;
import com.microtomato.hirun.modules.user.entity.po.MenuTemp;
import com.microtomato.hirun.modules.user.service.IMenuTempService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
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

    @Autowired
    private IPageService pageServiceImpl;

    @Autowired
    private IMenuTempService menuTempServiceImpl;

    @Value("${mhirun.host-port}")
    private String mHirunHostPort;

    @GetMapping("list-all")
    @RestResult
    public List<MenuNode> listAll() {

        List<MenuNode> nodes = new ArrayList<>();
        Map<Long, Menu> longMenuMap = menuServiceImpl.listAllMenus();
        List<Menu> menus = new ArrayList(longMenuMap.values());

        for (Menu menu : menus) {

            MenuNode node = new MenuNode();
            node.setId(menu.getMenuId());
            node.setTitle(menu.getTitle());
            node.setField("");
            node.setChecked(false);
            node.setSpread(false);

            if (null != menu.getParentMenuId()) {
                node.setPid(menu.getParentMenuId());
            }

            nodes.add(node);
        }

        List<MenuNode> tree = MenuTreeUtils.build(nodes);
        return tree;
    }

    @GetMapping("/list")
    @RestResult
    public List<TreeNode> getMenuTree() {

        UserContext userContext = WebContextUtils.getUserContext();

        Set<Long> menuidSet;
        if (userContext.isAdmin()) {
            // 查超级管理员能看到的菜单
            menuidSet = listMenusForAdmin(userContext);
        } else {
            // 查普通用户能看到的菜单ID
            menuidSet = listMenusForNormal(userContext);
        }

        // 查询所有菜单集合
        Map<Long, Menu> menuMap = convert(menuServiceImpl.listAllMenus());

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

        // 查询所有页面
        Map<Long, Page> pageMap = pageServiceImpl.listAllPages();
        for (Page page : pageMap.values()) {
            // 如果页面的归属菜单有权限，那么将此页面 url 加入可信集合。
            if (menuidSet.contains(page.getMenuId())) {
                menuUrls.add(page.getUrl());
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
     * 查超级管理员能看到的菜单ID
     *
     * @return 菜单 Id 集合
     */
    private Set<Long> listMenusForAdmin(UserContext userContext) {
        log.debug("username: {}, 超级管理员，能看到所有菜单", userContext.getUsername());
        Set<Long> rtn = new HashSet<>(100);
        List<Long> menuids = menuServiceImpl.listMenusForAdmin();
        rtn.addAll(menuids);
        return rtn;
    }

    /**
     * 查普通用户能看到的菜单ID
     *
     * @return 菜单 Id 集合
     */
    private Set<Long> listMenusForNormal(UserContext userContext) {
        Set<Long> rtn = new HashSet<>(100);
        log.debug("username: {}, 查询临时菜单权限 + 角色对应的菜单权限", userContext.getUsername());
        List<MenuTemp> menuTempList = menuTempServiceImpl.list(
            new QueryWrapper<MenuTemp>().lambda()
                .select(MenuTemp::getMenuId)
                .eq(MenuTemp::getUserId, userContext.getUserId())
                .gt(MenuTemp::getExpireDate, LocalDateTime.now())
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

    /**
     * 老系统菜单通过 Url 参数携带新系统的 SessionId
     *
     * @param menuMap
     */
    private Map<Long, Menu> convert(Map<Long, Menu> menuMap) {

        Map<Long, Menu> rtn = (Map<Long, Menu>) CloneUtils.deepCopy(menuMap);

        String token = (String) WebContextUtils.getHttpSession().getAttribute("token");
        for (Menu menu : rtn.values()) {
            if ("M".equals(menu.getType())) {
                if (null != menu.getMenuUrl()) {
                    String menuUrl = mHirunHostPort + menu.getMenuUrl() + "?hirun-token=" + token;
                    menu.setMenuUrl(menuUrl);
                }
            }
        }

        return rtn;
    }
}
