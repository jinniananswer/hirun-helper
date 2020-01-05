package com.microtomato.hirun.modules.system.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.data.TreeNode;
import com.microtomato.hirun.framework.security.AssetSession;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.*;
import com.microtomato.hirun.modules.system.entity.dto.MenuNode;
import com.microtomato.hirun.modules.system.entity.po.Menu;
import com.microtomato.hirun.modules.system.entity.po.Page;
import com.microtomato.hirun.modules.system.service.IMenuService;
import com.microtomato.hirun.modules.system.service.IPageService;
import com.microtomato.hirun.modules.user.service.IMenuTempService;
import com.microtomato.hirun.modules.user.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private IPageService pageServiceImpl;

    @Autowired
    private IMenuTempService menuTempServiceImpl;

    @Autowired
    private IRoleService roleServiceImpl;

    @Autowired
    private AssetSession assetSession;

    @Value("${mhirun.host-port}")
    private String mHirunHostPort;

    @GetMapping("list-all")
    @RestResult
    public List<MenuNode> listAll(Long roleId) {
        Set<Long> menuIds = new HashSet<>();
        if (null != roleId) {
            menuIds = roleServiceImpl.queryMenuId(roleId);
        }

        List<MenuNode> nodes = new ArrayList<>();
        Map<Long, Menu> longMenuMap = menuServiceImpl.listAllMenus();
        List<Menu> menus = new ArrayList(longMenuMap.values());

        for (Menu menu : menus) {

            MenuNode node = MenuNode.builder()
                .id(menu.getMenuId())
                .title(menu.getTitle())
                .field("")
                .checked(menuIds.contains(menu.getMenuId()))
                .spread(true)
                .build();

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
            menuidSet = listMenusForAdmin();
        } else {
            // 查普通用户能看到的菜单ID
            menuidSet = listMenusForNormal();
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
        assetSession.setMenuUrls(menuUrls);

        List<Menu> menus = new ArrayList(filteredMenuMap.values());
        if (ArrayUtils.isEmpty(menus)) {
            return null;
        }

        // 把有权限的节点的所有上游节点都带着
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

        // 剔除没有叶子节点的分支
        List<TreeNode> treeNodeList = removeBranchNodeWithoutLeafNode(nodes, menuMap);
        log.info("treeNodeList: {}", treeNodeList);

        // 构建菜单树
        List<TreeNode> tree = TreeUtils.build(treeNodeList);
        return tree;
    }

    /**
     * 第一步：遍历所有叶子节点
     * 第二步：将叶子节点的所有上游节点都放进返回结果集。
     *
     * @param nodes
     * @return
     */
    private List<TreeNode> removeBranchNodeWithoutLeafNode(List<TreeNode> nodes, Map<Long, Menu> menuMap) {

        Map<Long, Menu> map = new HashMap<>(128);

        for (TreeNode node : nodes) {
            Menu menu = (Menu) node.getNode();

            // 叶子节点
            if (StringUtils.isNotBlank(menu.getMenuUrl()) && ArrayUtils.isEmpty(node.getChildren())) {
                map.put(menu.getMenuId(), menu);
                addParentMenus(menu, map, menuMap);
            }
        }

        List<TreeNode> rtn = new ArrayList<>();
        for (Menu menu : map.values()) {
            TreeNode node = new TreeNode();
            node.setId(menu.getMenuId() + "");
            if (null != menu.getParentMenuId()) {
                node.setParentId(menu.getParentMenuId() + "");
            }
            node.setNode(menu);
            rtn.add(node);
        }

        return rtn;
    }

    /**
     * 查超级管理员能看到的菜单ID
     *
     * @return 菜单 Id 集合
     */
    private Set<Long> listMenusForAdmin() {
        return menuServiceImpl.listMenusForAdmin();
    }

    /**
     * 查普通用户能看到的菜单ID
     *
     * @return 菜单 Id 集合
     */
    private Set<Long> listMenusForNormal() {
        return menuServiceImpl.listMenusForNormal();
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
            if ("H".equals(menu.getType())) {
                if (null != menu.getMenuUrl()) {
                    String menuUrl = mHirunHostPort + menu.getMenuUrl() + "?hirun-token=" + token;
                    menu.setMenuUrl(menuUrl);
                }
            }
        }

        return rtn;
    }

}
