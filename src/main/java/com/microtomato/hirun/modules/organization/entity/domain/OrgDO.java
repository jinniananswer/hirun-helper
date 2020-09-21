package com.microtomato.hirun.modules.organization.entity.domain;

import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SecurityUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import com.microtomato.hirun.modules.organization.service.impl.OrgServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 组织领域对象
 * @author: jinnian
 * @create: 2019-10-22 11:13
 **/
@Component
@Scope("prototype")
public class OrgDO {

    @Autowired
    private IOrgService orgService;

    private Org org;

    public OrgDO() {

    }

    /**
     * 构造函数
     *
     * @param orgId
     */
    public OrgDO(Long orgId) {
        this.setOrg(orgId);
    }

    /**
     * 设置部门数据
     *
     * @param org
     */
    public void setOrg(Org org) {
        this.org = org;
    }

    /**
     * 根据部门ID设置部门数据
     *
     * @param orgId
     */
    public void setOrg(Long orgId) {
        this.org = this.findSelf(orgId);
    }

    /**
     * 获取当前组织
     * @return
     */
    public Org getOrg() {
        return this.org;
    }

    /**
     * 根据当前组织机构ID获取归属的公司
     *
     * @return
     */
    public Org getBelongCompany() {
        List<Org> orgs = orgService.listAllOrgs();
        if (ArrayUtils.isEmpty(orgs)) {
            return null;
        }
        return this.findParent("2", orgs, this.org.getOrgId());
    }

    /**
     * 根据当前组织机构ID获取归属的门店
     *
     * @return
     */
    public Org getBelongShop() {
        List<Org> orgs = orgService.listAllOrgs();
        if (ArrayUtils.isEmpty(orgs)) {
            return null;
        }
        return this.findParent("4", orgs, this.org.getOrgId());
    }

    /**
     * 根据当前组织机构ID获取归属的事业部
     * @return
     */
    public Org getBelongBU() {
        List<Org> orgs = orgService.listAllOrgs();
        if (ArrayUtils.isEmpty(orgs)) {
            return null;
        }
        return this.findParent("1", orgs, this.org.getOrgId());
    }

    /**
     * 获取集团公司
     * @return
     */
    public Org getRoot() {
        List<Org> orgs = orgService.listAllOrgs();
        if (ArrayUtils.isEmpty(orgs)) {
            return null;
        }

        for (Org org : orgs) {
            if (org.getParentOrgId() == null) {
                return org;
            }
        }
        return null;
    }

    /**
     * 根据权限获取当前归属组织的顶层机构（店铺、分公司、事业部、或者集团公司)
     * @return
     */
    public Org getSelfTop() {
        boolean hasAllOrg = SecurityUtils.hasFuncId("ALL_ORG");
        if (hasAllOrg) {
            return this.getRoot();
        } else {
            Org topOrg = this.getBelongShop();
            if (topOrg.getParentOrgId() == null) {
                //走到根节点，表示没找到
                topOrg = this.getBelongCompany();
                if (topOrg.getParentOrgId() == null) {
                    topOrg = this.getBelongBU();
                }
            }

            return topOrg;
        }
    }

    /**
     * 获取所在部门位于公司的父子线上的所有组织
     *
     * @return
     */
    public List<Org> getCompanyLine() {
        List<Org> orgs = orgService.listAllOrgs();
        if (ArrayUtils.isEmpty(orgs)) {
            return null;
        }
        return this.findLine("2", orgs, this.org.getOrgId());
    }

    /**
     * 获取所在部门位于公司的父子线上的所有组织的ID
     * @return
     */
    public List<Long> getCompanyLineId() {
        List<Org> orgs = this.getCompanyLine();

        if (ArrayUtils.isNotEmpty(orgs)) {
            List<Long> ids = new ArrayList<>();
            orgs.forEach(org -> {
                ids.add(org.getOrgId());
            });
            return ids;
        }

        return null;
    }

    /**
     * 获取所在部门位于公司的父子线上的所有组织的路径
     *
     * @return
     */
    public String getCompanyLinePath() {
        List<Org> lineOrgs = this.getCompanyLine();
        if (ArrayUtils.isEmpty(lineOrgs)) {
            return null;
        }

        int size = lineOrgs.size();
        String path = "";
        for (int i = size - 1; i >= 0; i--) {
            path += lineOrgs.get(i).getName() + "-";
        }

        return path.substring(0, path.length() - 1);
    }

    /**
     * 获取所在部门直到集团的父子线上的所有组织
     *
     * @return
     */
    public List<Org> getFullLine() {
        List<Org> orgs = orgService.listAllOrgs();
        List<Org> lines = this.findLine("0", orgs, this.org.getOrgId());
        Collections.reverse(lines);
        return lines;
    }

    /**
     * 递归找符合类型的组织机构
     *
     * @param type: 0-集团公司 1-事业部 2-公司（分公司或者集团公司） 3-部门 4-店铺 5-组
     * @param orgs
     * @param orgId
     * @return
     */
    public Org findParent(String type, List<Org> orgs, Long orgId) {
        Org parent = null;
        for (Org org : orgs) {
            if (orgId.equals(org.getOrgId())) {
                //先找到本身
                if (StringUtils.equals(type, org.getType())) {
                    return org;
                } else {
                    //找上级，看是否符合
                    Long parentOrgId = org.getParentOrgId();
                    if (parentOrgId == null) {
                        //已到根节点
                        return org;
                    }
                    parent = this.findParent(type, orgs, parentOrgId);
                }
            }
        }
        return parent;
    }

    /**
     * 递归查找所在部门位于某类型的父子线上的所有组织
     *
     * @param type: 0-集团公司 1-事业部 2-公司（分公司或者集团公司） 3-部门 4-店铺 5-组
     * @param orgs
     * @param orgId
     * @return
     */
    private List<Org> findLine(String type, List<Org> orgs, Long orgId) {
        List<Org> parents = new ArrayList<>();
        for (Org org : orgs) {
            if (orgId.equals(org.getOrgId())) {
                //先找到本身
                parents.add(org);
                if (StringUtils.equals(type, org.getType())) {
                    return parents;
                } else {
                    //找上级，看是否符合
                    Long parentOrgId = org.getParentOrgId();
                    if (parentOrgId == null) {
                        //已到根节点
                        return parents;
                    }
                    List<Org> temp = this.findLine(type, orgs, parentOrgId);
                    if (ArrayUtils.isNotEmpty(temp)) {
                        parents.addAll(temp);
                    }
                }
            }
        }
        return parents;
    }

    /**
     * 找到部门自身的数据
     *
     * @param orgId
     * @return
     */
    private Org findSelf(Long orgId) {
        IOrgService service = SpringContextUtils.getBean(OrgServiceImpl.class);
        List<Org> orgs = service.listAllOrgs();
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
     * 是否是家装事业部下的组织
     *
     * @return
     */
    public boolean isHomeDecoration() {
        List<Org> orgs = this.getFullLine();
        if (ArrayUtils.isEmpty(orgs)) {
            return false;
        }

        for (Org org : orgs) {
            if (org.getOrgId() == 7) {
                return true;
            }
        }
        return false;
    }

    /**
     * 递归查找某部门下的所有部门集合,返回结果为orgId集合
     *
     * @return
     */
    public String getOrgLine(Long orgId) {
        List<Org> orgs = orgService.listAllOrgs();
        if (orgs.size() <= 0) {
            return null;
        }
        String orgLine = buildSubOrg(orgId, orgs, orgId + "");
        return orgLine;
    }

    /**
     * 获取其下所有的组织机构
     * @param hasSelf
     * @return
     */
    public List<Org> getChildren(boolean hasSelf) {
        List<Org> children = this.orgService.findChildren(this.org);

        if (children == null) {
            children = new ArrayList<>();
        }
        if (hasSelf) {
            children.add(this.org);
        }
        return children;
    }

    /**
     * 获取其下所有的组织机构的ID
     * @param hasSelf
     * @return
     */
    public List<Long> getChildrenIds(boolean hasSelf) {
        List<Org> children = this.getChildren(hasSelf);

        if (ArrayUtils.isNotEmpty(children)) {
            List<Long> ids = new ArrayList<>();
            children.forEach(child -> {
                ids.add(child.getOrgId());
            });

            return ids;
        }
        return null;
    }

    /**
     * 递归构造子部门的整条线的数据
     * @param rootOrgId
     * @param orgs
     * @param orgLine
     * @return
     */
    private String buildSubOrg(Long rootOrgId, List<Org> orgs, String orgLine) {
        if (ArrayUtils.isEmpty(orgs)) {
            return orgLine;
        }
        for (Org org : orgs) {
            if (rootOrgId.equals(org.getParentOrgId())) {
                String subOrgs = buildSubOrg(org.getOrgId(), orgs, org.getOrgId() + "");
                if (StringUtils.isNotBlank(subOrgs)) {
                    orgLine += "," + subOrgs;
                }
            }
        }
        return orgLine;
    }

    /**
     * 根据当前组织机构ID获取父级组织Id
     *
     * @return
     */
    public Long getParentOrgId() {

        return this.org.getParentOrgId();
    }
}