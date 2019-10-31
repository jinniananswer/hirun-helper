package com.microtomato.hirun.modules.organization.entity.domain;

import com.alibaba.druid.util.StringUtils;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    /**
     * 设置部门数据
     * @param org
     */
    public void setOrg(Org org) {
        this.org = org;
    }

    /**
     * 根据部门ID设置部门数据
     * @param orgId
     */
    public void setOrg(Long orgId) {
        this.org = this.findSelf(orgId);
    }

    /**
     * 根据当前组织机构ID获取归属的公司
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
     * 获取所在部门位于公司的父子线上的所有组织
     * @return
     */
    public List<Org> getCompanyLine() {
        List<Org> orgs = orgService.listAllOrgs();
        if (ArrayUtils.isEmpty(orgs)) {
            return null;
        }
        return this.findCompanyLine(orgs, this.org.getOrgId());
    }

    /**
     * 获取所在部门位于公司的父子线上的所有组织的路径
     * @return
     */
    public String getCompanyLinePath() {
        List<Org> lineOrgs = this.getCompanyLine();
        if (ArrayUtils.isEmpty(lineOrgs)) {
            return null;
        }

        int size = lineOrgs.size();
        String path = "";
        for (int i=size-1; i >= 0; i--) {
            path += lineOrgs.get(i).getName()+"-";
        }

        return path.substring(0, path.length()-1);
    }

    /**
     * 递归找符合类型的组织机构
     * @param type: 1-事业部 2-公司（分公司或者集团公司） 3-部门 4-店铺 5-组
     * @param orgs
     * @param orgId
     * @return
     */
    private Org findParent(String type, List<Org> orgs, Long orgId) {
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
     * 递归查找所在部门位于公司的父子线上的所有组织
     * @param orgs
     * @param orgId
     * @return
     */
    private List<Org> findCompanyLine(List<Org> orgs, Long orgId) {
        List<Org> parents = new ArrayList<>();
        for (Org org : orgs) {
            if (orgId.equals(org.getOrgId())) {
                //先找到本身
                parents.add(org);
                if (StringUtils.equals("2", org.getType())) {
                    return parents;
                } else {
                    //找上级，看是否符合
                    Long parentOrgId = org.getParentOrgId();
                    if (parentOrgId == null) {
                        //已到根节点
                        return parents;
                    }
                    List<Org> temp = this.findCompanyLine(orgs, parentOrgId);
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
     * @param orgId
     * @return
     */
    private Org findSelf(Long orgId) {
        List<Org> orgs = this.orgService.listAllOrgs();
        if (ArrayUtils.isEmpty(orgs)) {
            return null;
        }

        for (Org org : orgs) {
            if (orgId.equals(org.getOrgId())) {
                return org;
            }
        }
        return null;
    }
}
