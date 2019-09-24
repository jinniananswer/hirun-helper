package com.microtomato.hirun.framework.util;

import com.microtomato.hirun.framework.data.TreeNode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 构造树的工具
 *
 * @author jinnian
 * @date 2019-09-08 16:42
 **/
public class TreeUtils {

    /**
     * 构建通用树结构
     *
     * @param nodes
     * @return
     */
    public static List<TreeNode> build(List<TreeNode> nodes) {
        List<TreeNode> roots = findRoot(nodes);

        if (ArrayUtils.isEmpty(roots)) {
            return null;
        }

        for (TreeNode root : roots) {
            root.setPath(root.getTitle());
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
    private static void buildChildren(TreeNode node, List<TreeNode> nodes) {
        if (ArrayUtils.isEmpty(nodes)) {
            return;
        }

        List<TreeNode> children = new ArrayList<>();
        for (TreeNode child : nodes) {
            if (StringUtils.equals(child.getParentId(), node.getId())) {
                children.add(child);
                String path = node.getPath() + "-" + child.getTitle();
                child.setPath(path);

                buildChildren(child, nodes);
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
    public static List<TreeNode> findRoot(List<TreeNode> nodes) {
        if (ArrayUtils.isEmpty(nodes)) {
            return null;
        }

        List<TreeNode> result = new ArrayList<>();
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
