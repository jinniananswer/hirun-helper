package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.modules.organization.entity.consts.OrgConst;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeOrgRel;
import com.microtomato.hirun.modules.organization.mapper.EmployeeOrgRelMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeOrgRelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class EmployeeOrgRelServiceImpl extends ServiceImpl<EmployeeOrgRelMapper, EmployeeOrgRel> implements IEmployeeOrgRelService {

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
}
