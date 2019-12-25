package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.data.TreeNode;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TreeUtils;
import com.microtomato.hirun.modules.organization.entity.dto.AreaOrgNumDTO;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.mapper.OrgMapper;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import com.microtomato.hirun.modules.system.entity.domain.AddressDO;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2019-09-16
 */
@Slf4j
@Service
public class OrgServiceImpl extends ServiceImpl<OrgMapper, Org> implements IOrgService {

    @Autowired
    private OrgMapper orgMapper;

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private AddressDO addressDO;

    @Override
    @Cacheable(value = "all-org")
    public List<Org> listAllOrgs() {
        List<Org> orgs = this.list(new QueryWrapper<Org>().lambda().eq(Org::getStatus, "0"));
        return orgs;
    }

    @Override
    @Cacheable(value = "listWithTree")
    public List<TreeNode> listWithTree() {
        List<Org> orgs = listAllOrgs();

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

    @Override
    public void buildMap(List<TreeNode> nodeList, Map<String, TreeNode> nodeMap) {
        for (TreeNode treeNode : nodeList) {
            String id = treeNode.getId();
            nodeMap.put(id, treeNode);
            if (null != treeNode.getChildren() && treeNode.getChildren().size() > 0) {
                List<TreeNode> childNodeList = treeNode.getChildren();
                buildMap(childNodeList, nodeMap);
            }
        }
    }

    @Override
    public List<AreaOrgNumDTO> countShopNum(String areaType) {
        List<AreaOrgNumDTO> orgNums = null;
        if (StringUtils.equals("1", areaType)) {
            orgNums = this.orgMapper.countShopNumByProvince();
        } else if (StringUtils.equals("2", areaType)) {
            orgNums = this.orgMapper.countShopNumByCity();
        }

        if (ArrayUtils.isNotEmpty(orgNums)) {
            for (AreaOrgNumDTO orgNum : orgNums) {
                if (StringUtils.equals("1", areaType)) {
                    String provinceName = this.addressDO.getProvinceName(Integer.parseInt(orgNum.getArea()));
                    if (StringUtils.isNotBlank(provinceName) && provinceName.length() > 2) {
                        provinceName = provinceName.substring(0, provinceName.length() - 1);
                    }
                    orgNum.setName(provinceName);
                } else if (StringUtils.equals("2", areaType)) {
                    orgNum.setName(this.staticDataService.getCodeName("BIZ_CITY", orgNum.getArea()));
                }
            }
        }
        return orgNums;
    }

}
