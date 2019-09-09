package com.microtomato.hirun.modules.system.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.data.TreeNode;
import com.microtomato.hirun.framework.utils.ArrayUtil;
import com.microtomato.hirun.framework.utils.TreeUtil;
import com.microtomato.hirun.modules.system.entity.po.Menu;
import com.microtomato.hirun.modules.system.service.IMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jinnian
 * @since 2019-09-05
 */
@RestController
@Slf4j
@RequestMapping("/api/system/menu/")
public class MenuController {

    @Autowired
    private IMenuService menuServiceImpl;

    @GetMapping("/list")
    @RestResult
    public List<TreeNode> getMenuTree() {
        List<Menu> menus = menuServiceImpl.listAllMenus();

        if (ArrayUtil.isEmpty(menus)) {
            return null;
        }

        List<TreeNode> nodes = new ArrayList<TreeNode>();
        for (Menu menu : menus) {
            TreeNode node = new TreeNode();
            node.setId(menu.getMenuId() + "");
            if (menu.getParentMenuId() != null) {
                node.setParentId(menu.getParentMenuId() + "");
            }
            node.setNode(menu);
            nodes.add(node);
        }

        List<TreeNode> tree = TreeUtil.build(nodes);
        return tree;
    }
}
