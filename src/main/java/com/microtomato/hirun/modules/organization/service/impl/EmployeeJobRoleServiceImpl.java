package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.organization.entity.consts.EmployeeConst;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import com.microtomato.hirun.modules.organization.mapper.EmployeeJobRoleMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeJobRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2019-10-09
 */
@Slf4j
@Service
public class EmployeeJobRoleServiceImpl extends ServiceImpl<EmployeeJobRoleMapper, EmployeeJobRole> implements IEmployeeJobRoleService {

    /**
     * 根据员工ID获取员工基本信息
     * @param employeeId
     * @return
     */
    @Override
    public List<EmployeeJobRole> queryValidMain(Long employeeId) {
        String now = TimeUtils.now();
        List<EmployeeJobRole> jobRoles = this.list(new QueryWrapper<EmployeeJobRole>().lambda().eq(EmployeeJobRole::getEmployeeId, employeeId).eq(EmployeeJobRole::getIsMain, EmployeeConst.JOB_ROLE_MAIN).le(EmployeeJobRole::getStartDate, now).ge(EmployeeJobRole::getEndDate, now));
        if (ArrayUtils.isEmpty(jobRoles)) {
            return null;
        }
        return jobRoles;
    }

    /**
     * 根据员工ID获取最近的一条岗位信息纪录
     * @param employeeId
     * @return
     */
    @Override
    public EmployeeJobRole queryLast(Long employeeId) {
        LambdaQueryWrapper<EmployeeJobRole> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(EmployeeJobRole::getEmployeeId, employeeId);
        wrapper.orderByDesc(EmployeeJobRole::getStartDate);

        List<EmployeeJobRole> jobRoles = this.list(wrapper);

        if (ArrayUtils.isEmpty(jobRoles)) {
            return null;
        }

        return jobRoles.get(0);
    }
}
