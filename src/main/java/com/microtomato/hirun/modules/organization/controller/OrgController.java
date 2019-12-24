package com.microtomato.hirun.modules.organization.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.data.TreeNode;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TreeUtils;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jinnian
 * @since 2019-09-16
 */
@RestController
@Slf4j
@RequestMapping("api/organization/org")
public class OrgController {

    @Autowired
    private IOrgService orgServiceImpl;

    @GetMapping("/listWithTree")
    @RestResult
    public List<TreeNode> listWithTree() {
        List<Org> orgs = orgServiceImpl.listAllOrgs();

        if (ArrayUtils.isEmpty(orgs)) {
            return null;
        }

        List<TreeNode> nodes = new ArrayList<>();
        for (Org org : orgs) {
            TreeNode node = new TreeNode();
            node.setId(org.getOrgId() + "");
            node.setTitle(org.getName());

            if (org.getParentOrgId() != null) {
                node.setParentId(org.getParentOrgId() + "");
            } else {
                node.setSpread(true);
            }
            node.setNode(org);
            nodes.add(node);
        }
        List<TreeNode> tree = TreeUtils.build(nodes);
        return tree;
    }

    /**
     * 如果所有的子孙节点都在，并且是分支节点，那么仅保留该分支节点，同时剔除掉子孙节点，子孙节点不再纳入计算。
     *
     * @param ids
     * @return
     */
    @PostMapping("/filter")
    @RestResult
    public List<String> filter(@RequestBody List<String> ids) {

        Set<String> rtn = new HashSet<>();
        List<TreeNode> nodeList = listWithTree();
        Map<String, TreeNode> nodeMap = new HashMap<>(512);
        buildMap(nodeList, nodeMap);

        TreeSet<String> idSet = new TreeSet<>();
        TreeSet<String> idSet2 = new TreeSet<>();
        idSet.addAll(ids);
        idSet2.addAll(ids);

        while (idSet.size() > 0) {
            String id = idSet.first();
            TreeNode treeNode = nodeMap.get(id);
            if (isAllChildrenExist(treeNode, idSet2)) {
                // 所有子孙节点都在
                rtn.add(id);
                removeAllChildrenExist(treeNode.getChildren(), idSet, rtn);
            }
            idSet.remove(id);
        }

        return new ArrayList<>(rtn);
    }

    /**
     * 是否所有的子节点都在集合里
     *
     * @param idSet
     * @param treeNode
     * @return
     */
    private boolean isAllChildrenExist(TreeNode treeNode, Set<String> idSet) {
        List<TreeNode> children = treeNode.getChildren();
        if (null == children || 0 == children.size()) {
            // 当前是叶子节点，直接返回 true
            return true;
        }

        for (TreeNode node : children) {
            if (!idSet.contains(node.getId())) {
                // 发现有子节点不在集合内，那么不考虑该分支节点
                return false;
            }

            if (!isAllChildrenExist(node, idSet)) {
                // 发现孙...节点不在集合内，那么不考虑该分支节点
                return false;
            }
        }

        // 子节点都包含在内，那么只需提交该分支节点。
        return true;
    }

    private void removeAllChildrenExist(List<TreeNode> children, Set<String> idSet, Set<String> rtn) {
        if (null != children && children.size() > 0) {
            for (TreeNode node : children) {
                idSet.remove(node.getId());
                rtn.remove(node.getId());
                removeAllChildrenExist(node.getChildren(), idSet, rtn);
            }
        }
    }

    private void buildMap(List<TreeNode> nodeList, Map<String, TreeNode> nodeMap) {
        for (TreeNode treeNode : nodeList) {
            String id = treeNode.getId();
            nodeMap.put(id, treeNode);
            if (null != treeNode.getChildren() && treeNode.getChildren().size() > 0) {
                List<TreeNode> childNodeList = treeNode.getChildren();
                buildMap(childNodeList, nodeMap);
            }
        }
    }

}
