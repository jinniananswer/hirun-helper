package com.microtomato.hirun.modules.finance.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.security.Role;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.finance.entity.po.FinanceAcct;
import com.microtomato.hirun.modules.finance.mapper.FinanceAcctMapper;
import com.microtomato.hirun.modules.finance.service.IFinanceAcctService;
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * (FinanceAcct)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-10-13 16:51:29
 */
@Service
@Slf4j
public class FinanceAcctServiceImpl extends ServiceImpl<FinanceAcctMapper, FinanceAcct> implements IFinanceAcctService {

    @Autowired
    private FinanceAcctMapper financeAcctMapper;

    /**
     * 根据付款方式查询付款信息
     * @param paymentId
     * @return
     */
    @Override
    public FinanceAcct getByPaymentId(Long paymentId) {
        return this.getById(paymentId);
    }

    /**
     * 根据类型查询
     * @param type
     * @return
     */
    @Override
    public List<FinanceAcct> queryByType(String type) {
        return this.list(Wrappers.<FinanceAcct>lambdaQuery()
                .eq(FinanceAcct::getType, type)
                .eq(FinanceAcct::getStatus, "U"));
    }

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<FinanceAcct> queryAll() {
        return this.list(Wrappers.<FinanceAcct>lambdaQuery()
                .eq(FinanceAcct::getStatus, "U"));
    }

    /**
     * 获取登陆员工能够看到的付款方式
     * @return
     */
    @Override
    public List<FinanceAcct> queryByLoginEmployeeId() {
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        Long orgId = WebContextUtils.getUserContext().getOrgId();
        List<Role> roles = WebContextUtils.getUserContext().getRoles();

        List<FinanceAcct> result = new ArrayList<>();
        List<FinanceAcct> all = this.queryAll();

        OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, orgId);
        Org shop = orgDO.getBelongShop();
        Org company = orgDO.getBelongCompany();

        for (FinanceAcct acct : all) {
            String useShopId = acct.getUseShopId();
            String companyId = acct.getCompanyId();
            String useRoleId = acct.getUseRoleId();

            boolean isFixCachier = false;
            if (StringUtils.isNotBlank(companyId)) {
                //company主要针对位于公司下的出纳
                boolean isCashier = this.hasRole(roles, 60L);
                if (isCashier && companyId.equals(company.getOrgId())) {
                    isFixCachier = true;
                }
            }

            boolean isFixOther = false;
            if (StringUtils.isNotBlank(useShopId)) {
                if (StringUtils.contains(useShopId + ",", shop.getOrgId() + ",")) {
                    if (StringUtils.isNotBlank(useRoleId)) {
                        String[] useRoleIds = useRoleId.split(",");

                        for (String id : useRoleIds) {
                            if (StringUtils.equals(id, "60")) {
                                continue;
                            }

                            if (this.hasRole(roles, Long.parseLong(id))) {
                                isFixOther = true;
                                break;
                            }
                        }
                    }
                }
            }

            if (isFixCachier || isFixOther) {
                result.add(acct);
            }
        }

        return result;
    }

    /**
     * 登陆员工是否存在某角色
     * @param roles
     * @param roleId
     * @return
     */
    private boolean hasRole(List<Role> roles, Long roleId) {
        if (ArrayUtils.isEmpty(roles)) {
            return false;
        }

        for (Role role : roles) {
            if (role.getId().equals(roleId)) {
                return true;
            }
        }
        return false;
    }
}