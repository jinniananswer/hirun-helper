package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.organization.entity.consts.OrgConst;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeOrgRel;
import com.microtomato.hirun.modules.organization.mapper.EmployeeOrgRelMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeOrgRelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2019-12-23
 */
@Slf4j
@Service
@DataSource(DataSourceKey.INS)
public class EmployeeOrgRelServiceImpl extends ServiceImpl<EmployeeOrgRelMapper, EmployeeOrgRel> implements IEmployeeOrgRelService {

    @Autowired
    private EmployeeOrgRelMapper mapper;
    /**
     * 查询员工有关联关系的部门
     * @param employeeId
     * @return
     */
    @Override
    public List<EmployeeOrgRel> queryRelByEmployeeId(Long employeeId) {
        List<EmployeeOrgRel> orgRels = this.list(new QueryWrapper<EmployeeOrgRel>().lambda().eq(EmployeeOrgRel::getRelType, OrgConst.EMPLOYEE_REL_TYPE_RELEVANCE).eq(EmployeeOrgRel::getEmployeeId, employeeId));
        return orgRels;
    }

    @Override
    public void updateEmployeeOrgRel(String orgIds,String type) {
        this.mapper.delete(Wrappers.<EmployeeOrgRel>lambdaQuery().eq(EmployeeOrgRel::getRelType,type)
                .apply("org_id in ("+orgIds+")"));
    }

    /**
     * 查询财务人员负责的店面
     * @param employeeId
     * @return
     */
    @Override
    public List<EmployeeOrgRel> queryFinanceOrgRel(Long employeeId) {
        List<EmployeeOrgRel> orgRels = this.list(new QueryWrapper<EmployeeOrgRel>().lambda().eq(EmployeeOrgRel::getRelType, "2").eq(EmployeeOrgRel::getEmployeeId, employeeId));
        return orgRels;
    }
}
