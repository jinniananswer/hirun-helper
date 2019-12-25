package com.microtomato.hirun.modules.organization.entity.consts;

/**
 * @program: hirun-helper
 * @description: 部门常量数据类
 * @author: jinnian
 * @create: 2019-12-23 02:12
 **/
public class OrgConst {

    /**
     * 列出所有部门的权限
     */
    public static final String SECURITY_ALL_ORG = "ALL_ORG";

    /**
     * 列出所有事业部及其下部门的权限
     */
    public static final String SECURITY_ALL_BU = "ALL_BU";

    /**
     * 列出归属事业部及其下所有部门的权限
     */
    public static final String SECURITY_SELF_BU = "SELF_BU";

    /**
     * 列出所有分公司及其下部门的权限
     */
    public static final String SECURITY_ALL_SUB_COMPANY = "ALL_SUB_COMPANY";

    /**
     * 列出归属分公司及其下所有部门的权限
     */
    public static final String SECURITY_SELF_SUB_COMPANY = "SELF_SUB_COMPANY";

    /**
     * 列出所有店铺及其下部门的权限
     */
    public static final String SECURITY_ALL_COMANY_SHOP = "ALL_COMPANY_SHOP";

    /**
     * 列出归属公司下所有店铺及其下部门的权限
     */
    public static final String SECURITY_ALL_SHOP = "ALL_SHOP";

    /**
     * 列出归属店铺及其下部门的权限
     */
    public static final String SECURITY_SELF_SHOP = "SELF_SHOP";

    /**
     * 集团总公司
     */
    public static final String TYPE_CENTER_ENTERPRISE = "0";

    /**
     * 事业部
     */
    public static final String TYPE_BU = "1";

    /**
     * 分公司
     */
    public static final String TYPE_SUB_COMPANY = "2";

    /**
     * 部门
     */
    public static final String TYPE_DEPART = "3";

    /**
     * 门店
     */
    public static final String TYPE_SHOP = "4";

    /**
     * 小组
     */
    public static final String TYPE_GROUP = "5";

    /**
     * 员工与部门关系类型：关联关系
     */
    public static final String EMPLOYEE_REL_TYPE_RELEVANCE = "1";

}
