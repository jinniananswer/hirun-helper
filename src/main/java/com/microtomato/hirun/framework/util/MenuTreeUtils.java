package com.microtomato.hirun.framework.util;

import com.microtomato.hirun.modules.system.entity.dto.MenuNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜單构造树的工具
 *
 * @author Steven
 * @date 2019-09-08 16:42
 **/
public class MenuTreeUtils {

    /**
     * 构建通用树结构
     *
     * @param nodes
     * @return
     */
    public static List<MenuNode> build(List<MenuNode> nodes) {
        List<MenuNode> roots = findRoot(nodes);

        if (ArrayUtils.isEmpty(roots)) {
            return null;
        }

        for (MenuNode root : roots) {
            root.setTitle(root.getTitle());
            buildChildren(root, nodes);
        }

        return roots;
    }

    /**
     * 构建树的子节点
     *
     * @param node
     * @param nodes
     */
    private static void buildChildren(MenuNode node, List<MenuNode> nodes) {
        if (ArrayUtils.isEmpty(nodes)) {
            return;
        }

        List<MenuNode> children = new ArrayList<>();
        for (MenuNode child : nodes) {
            Long pid = child.getPid();
            Long id = node.getId();

            if (null != pid && null != id) {
                if (pid.equals(id)) {
                    children.add(child);
                    child.setTitle(child.getTitle());
                    buildChildren(child, nodes);
                }
            }
        }

        if (ArrayUtils.isNotEmpty(children)) {
            node.setChildren(children);
        }
    }

    /**
     * 寻找根节点，找出那些上级节点为空的节点视为根节点
     *
     * @param nodes
     * @return
     */
    public static List<MenuNode> findRoot(List<MenuNode> nodes) {
        if (ArrayUtils.isEmpty(nodes)) {
            return null;
        }

        List<MenuNode> result = new ArrayList<>();
        for (MenuNode node : nodes) {
            Long parentId = node.getPid();
            if (null == parentId) {
                // 找不到上级节点，即默认视为根节点
                result.add(node);
            }
        }

        return result;
    }
}
