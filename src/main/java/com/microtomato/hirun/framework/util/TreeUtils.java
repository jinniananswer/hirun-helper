package com.microtomato.hirun.framework.util;

import com.microtomato.hirun.framework.data.TreeNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 构造树的工具
 *
 * @author jinnian
 * @date 2019-09-08 16:42
 **/
@Slf4j
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

        // 排序
        Collections.sort(roots, Comparator.comparing(TreeNode::getPriority));

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

        // 排序
        Collections.sort(children, Comparator.comparing(TreeNode::getPriority));
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
            TreeNode root = findParent(node, nodes);
            if (root == null) {
                //没有找到，则加入根节点
                result.add(node);
            }
        }

        return result;
    }

    /**
     * 查找上级节点，
     *
     * @param leaf  叶子节点
     * @param nodes
     * @return
     */
    public static TreeNode findParent(TreeNode leaf, List<TreeNode> nodes) {
        if (StringUtils.isBlank(leaf.getParentId())) {
            return null;
        }
        for (TreeNode node : nodes) {
            if (StringUtils.equals(leaf.getParentId(), node.getId())) {
                return node;
            }
        }

        return null;
    }

    public static void addBag(String id, Long bag, Map<String, TreeNode> nodeMap) {
        if (null != id) {
            TreeNode treeNode = nodeMap.get(id);
            if (null == treeNode) {
                return;
            }
            //log.info("{} 当前员工数: {}, 添加员工数: {}", treeNode.getTitle(), treeNode.getBag().get(), bag);
            treeNode.getBag().addAndGet(bag);
            addBag(treeNode.getParentId(), bag, nodeMap);

        }
    }
}
