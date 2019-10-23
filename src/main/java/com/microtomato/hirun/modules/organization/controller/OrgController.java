package com.microtomato.hirun.modules.organization.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.data.TreeNode;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TreeUtils;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.service.IOrgService;
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

        List<TreeNode> nodes = new ArrayList<TreeNode>();
        for (Org org : orgs) {
            TreeNode node = new TreeNode();
            node.setId(org.getOrgId()+"");
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

}
