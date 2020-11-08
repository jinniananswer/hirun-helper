package com.microtomato.hirun.modules.organization.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.data.TreeNode;
import com.microtomato.hirun.framework.util.TreeUtils;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeOrgGroupByDTO;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.service.IEmployeeJobRoleService;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    private IEmployeeJobRoleService employeeJobRoleService;

    @GetMapping("/listWithTree")
    @RestResult
    public List<TreeNode> listWithTree() {
        return orgServiceImpl.listOrgTree();
    }

    @GetMapping("/listAssignType")
    @RestResult
    public List<TreeNode> listAssignType(String type) {
        return orgServiceImpl.listAssignType(type);
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
        orgServiceImpl.buildMap(nodeList, nodeMap);

        // 用来遍历的
        TreeSet<String> idSet1 = new TreeSet<>();
        // 用来判断的
        TreeSet<String> idSet2 = new TreeSet<>();
        idSet1.addAll(ids);
        idSet2.addAll(ids);

        while (idSet1.size() > 0) {
            String id = idSet1.first();
            TreeNode treeNode = nodeMap.get(id);
            if (isAllChildrenExist(treeNode, idSet2)) {
                // 所有子孙节点都在
                rtn.add(id);
                removeAllChildrenExist(treeNode.getChildren(), idSet1, rtn);
            }
            idSet1.remove(id);
        }

        return new ArrayList<>(rtn);
    }

    @GetMapping("/listWithBag")
    @RestResult
    public List<TreeNode> listWithBag() {
        List<TreeNode> nodeList = orgServiceImpl.listOrgTree();
        Map<String, TreeNode> nodeMap = new HashMap<>(512);
        orgServiceImpl.buildMap(nodeList, nodeMap);

        List<EmployeeOrgGroupByDTO> employeeOrgGroupByDTOS = employeeJobRoleService.countGroupByOrgId();
        for (EmployeeOrgGroupByDTO employeeOrgGroupByDTO : employeeOrgGroupByDTOS) {
            String orgId = employeeOrgGroupByDTO.getOrgId().toString();
            Long count = employeeOrgGroupByDTO.getCount();
            TreeUtils.addBag(orgId, count, nodeMap);
        }

        display(nodeList, 0);

        return nodeList;
    }

    private void display(List<TreeNode> nodeList, int level) {

        if (null == nodeList) {
            return;
        }

        String prefix1 = StringUtils.repeat("    ", level - 1);
        for (TreeNode treeNode : nodeList) {
            String prefix2 = "";
            if (0 != level) {
                prefix2 = prefix1 + "    ";
            } else {
                prefix2 = prefix1;
            }

            System.out.printf("%s %s:%s -> (%d)\n", prefix2, treeNode.getTitle(), treeNode.getId(), treeNode.getBag().get());
            display(treeNode.getChildren(), level + 1);
        }

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

    @GetMapping("/selectShop")
    @RestResult
    public List<Org> selectShop() {
        return orgServiceImpl.selectShop();
    }

}
