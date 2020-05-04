package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.data.TreeNode;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.*;
import com.microtomato.hirun.modules.organization.entity.consts.OrgConst;
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.dto.AreaOrgNumDTO;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeOrgRel;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.mapper.OrgMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeJobRoleService;
import com.microtomato.hirun.modules.organization.service.IEmployeeOrgRelService;
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
 * 服务实现类
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

    @Autowired
    private IEmployeeJobRoleService employeeJobRoleService;

    @Autowired
    private IEmployeeOrgRelService employeeOrgRelService;

    /**
     * 列出所有有效组织数据，有缓存
     *
     * @return
     */
    @Override
    @Cacheable(value = "all-org")
    public List<Org> listAllOrgs() {
        List<Org> orgs = this.list(
            Wrappers.<Org>lambdaQuery()
                .select(Org::getOrgId, Org::getParentOrgId, Org::getName, Org::getType, Org::getEnterpriseId, Org::getCity, Org::getStatus)
                .eq(Org::getStatus, "0")

        );
        return orgs;
    }

    /**
     * 根据员工权限列出能看到的部门
     *
     * @return
     */
    @Override
    public List<Org> listOrgsSecurity() {
        List<Org> allOrgs = this.listAllOrgs();

        if (SecurityUtils.hasFuncId(OrgConst.SECURITY_ALL_ORG)) {
            return allOrgs;
        } else if (SecurityUtils.hasFuncId(OrgConst.SECURITY_ALL_BU)) {
            List<Org> bu = this.listByType(OrgConst.TYPE_BU);
            if (ArrayUtils.isEmpty(bu)) {
                return null;
            }
            return this.findChildren(bu, allOrgs);
        } else if (SecurityUtils.hasFuncId(OrgConst.SECURITY_ALL_SUB_COMPANY)) {
            List<Org> companys = this.listByType(OrgConst.TYPE_SUB_COMPANY);
            if (ArrayUtils.isEmpty(companys)) {
                return null;
            }
            return this.findChildren(companys, allOrgs);
        } else if (SecurityUtils.hasFuncId(OrgConst.SECURITY_ALL_COMANY_SHOP)) {
            List<Org> shops = this.listByType(OrgConst.TYPE_SHOP);
            if (ArrayUtils.isEmpty(shops)) {
                return null;
            }
            return this.findChildren(shops, allOrgs);
        } else {
            UserContext userContext = WebContextUtils.getUserContext();
            Long employeeId = userContext.getEmployeeId();

            EmployeeJobRole mainJobRole = this.employeeJobRoleService.queryValidMain(employeeId);
            Long orgId = mainJobRole.getOrgId();

            OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, orgId);
            List<Org> temps = new ArrayList<>();

            Org belong = null;
            if (SecurityUtils.hasFuncId(OrgConst.SECURITY_SELF_BU)) {
                belong = orgDO.getBelongBU();
            } else if (SecurityUtils.hasFuncId(OrgConst.SECURITY_SELF_SUB_COMPANY) || SecurityUtils.hasFuncId(OrgConst.SECURITY_ALL_SHOP)) {
                belong = orgDO.getBelongCompany();
//
//                if (SecurityUtils.hasFuncId(OrgConst.SECURITY_ALL_SHOP) && belong != null) {
//                    //有查分公司下所有门店的权限
//                    List<Org> children = this.findChildren(belong, allOrgs);
//                    List<Org> shops = this.listByType(OrgConst.TYPE_SHOP, children);
//                    temps.addAll(shops);
//                    return this.findChildren(temps, allOrgs);
//                }

            } else if (SecurityUtils.hasFuncId(OrgConst.SECURITY_SELF_SHOP)) {
                belong = orgDO.getBelongShop();
            } else {
                temps.add(orgDO.getOrg());

                List<EmployeeOrgRel> orgRels = this.employeeOrgRelService.queryRelByEmployeeId(employeeId);
                if (ArrayUtils.isNotEmpty(orgRels)) {
                    for (EmployeeOrgRel orgRel : orgRels) {
                        Long relOrgId = orgRel.getOrgId();
                        Org relOrg = this.queryByOrgId(relOrgId);
                        temps.add(relOrg);
                    }
                }
                return temps;
            }

            if (belong == null) {
                //没有找到归属的指定类型的组织，则表示员工不属于该类型下的组织，则返回员工部门自身
                temps.add(orgDO.getOrg());
                return temps;
            } else {
                temps.add(belong);
                return this.findChildren(temps, allOrgs);
            }
        }
    }

    @Override
    @Cacheable("org-orgId")
    public Org queryByOrgId(Long orgId) {
        List<Org> orgs = this.listAllOrgs();
        if (ArrayUtils.isEmpty(orgs)) {
            return null;
        }

        for (Org org : orgs) {
            if (org.getOrgId().equals(orgId)) {
                return org;
            }
        }
        return null;
    }

    /**
     * 按组织类型列出所有符合条件的组织
     *
     * @param type 组织类型
     * @return
     */
    @Override
    public List<Org> listByType(String type) {
        List<Org> orgs = this.listAllOrgs();
        if (ArrayUtils.isEmpty(orgs)) {
            return null;
        }

        List<Org> result = new ArrayList<Org>();
        for (Org org : orgs) {
            if (StringUtils.equals(type, org.getType())) {
                result.add(org);
            }
        }
        return result;
    }

    /**
     * 按组织类型在指定的组织链上列出所有符合条件的组织
     *
     * @param type     组织类型
     * @param orgLines 指定的组织链
     * @return
     */
    public List<Org> listByType(String type, List<Org> orgLines) {
        if (ArrayUtils.isEmpty(orgLines)) {
            return null;
        }

        List<Org> result = new ArrayList<Org>();
        for (Org org : orgLines) {
            if (StringUtils.equals(type, org.getType())) {
                result.add(org);
            }
        }
        return result;
    }

    /**
     * 根据传入的列表集合找到所有其下的子组织机构(包含父组织机构本身)
     *
     * @param parents 父组织机构列表
     * @param orgs    源组织机构列表
     * @return
     */
    public List<Org> findChildren(List<Org> parents, List<Org> orgs) {
        if (ArrayUtils.isEmpty(parents)) {
            return null;
        }

        List<Org> children = new ArrayList<>();
        for (Org parent : parents) {
            children.add(parent);
            List<Org> temps = this.findChildren(parent, orgs);
            if (ArrayUtils.isNotEmpty(temps)) {
                children.addAll(temps);
            }
        }
        return children;
    }

    /**
     * 根据传入的列表集合找到所有其下的子组织机构（不包含父组织结构本身）
     *
     * @param parent 父组织机构
     * @param orgs   源组织机构列表
     * @return
     */
    public List<Org> findChildren(Org parent, List<Org> orgs) {
        List<Org> children = new ArrayList<>();
        for (Org org : orgs) {
            if (parent.getOrgId().equals(org.getParentOrgId())) {
                children.add(org);
                List<Org> temps = this.findChildren(org, orgs);
                if (ArrayUtils.isNotEmpty(temps)) {
                    children.addAll(temps);
                }
            }
        }
        return children;
    }

    /**
     * 查询某组织下的所有组织
     *
     * @param parent
     * @return
     */
    @Override
    public List<Org> findChildren(Org parent) {
        List<Org> allOrgs = this.listAllOrgs();
        if (ArrayUtils.isEmpty(allOrgs)) {
            return null;
        }
        return findChildren(parent, allOrgs);
    }

    @Override
    public List<TreeNode> listOrgTree() {
        List<Org> orgs = this.listOrgsSecurity();

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

    /**
     * 统计门店数量
     *
     * @param areaType
     * @return
     */
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

    @Override
    public String listOrgSecurityLine() {
        List<Org> orgs = this.listOrgsSecurity();
        if (ArrayUtils.isEmpty(orgs)) {
            return "";
        }
        String orgLine = "";

        for (Org org : orgs) {
            orgLine += org.getOrgId() + ",";
        }
        return orgLine.substring(0, orgLine.length() - 1);
    }
}
