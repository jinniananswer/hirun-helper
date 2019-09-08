package com.microtomato.hirun.framework.utils;

import com.microtomato.hirun.framework.data.TreeNode;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 构造树的工具
 * @author: jinnian
 * @create: 2019-09-08 16:42
 **/
public class TreeUtil {

    /**
     * 构建通用树结构
     * @param nodes
     * @return
     */
    public static List<TreeNode> build(List<TreeNode> nodes) {
        List<TreeNode> roots = findRoot(nodes);

        if (ArrayUtil.isEmpty(roots)) {
            return null;
        }

        for (TreeNode root : roots) {
            buildChildren(root, nodes);
        }

        return roots;
    }

    /**
     * 构建树的子节点
     * @param node
     * @param nodes
     */
    private static void buildChildren(TreeNode node, List<TreeNode> nodes) {
        if (ArrayUtil.isEmpty(nodes)) {
            return;
        }

        List<TreeNode> children = new ArrayList<TreeNode>();
        for (TreeNode child : nodes) {
            if (StringUtils.equals(child.getParentId(), node.getId())) {
                children.add(child);
                buildChildren(child, nodes);
            }
        }

        if (ArrayUtil.isNotEmpty(children)) {
            node.setChildren(children);
        }
    }

    /**
     * 寻找根节点，找出那些上级节点为空的节点视为根节点
     * @param nodes
     * @return
     */
    public static List<TreeNode> findRoot(List<TreeNode> nodes) {
        if (ArrayUtil.isEmpty(nodes)) {
            return null;
        }

        List<TreeNode> result = new ArrayList<TreeNode>();
        for (TreeNode node : nodes) {
            String parentId = node.getParentId();
            if (StringUtils.isEmpty(parentId)) {
                //找不到上级节点，即默认视为根节点
                result.add(node);
            }
        }

        return result;
    }
}
