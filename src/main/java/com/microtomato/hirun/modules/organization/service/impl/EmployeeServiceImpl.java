package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.organization.entity.consts.EmployeeConst;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeExampleDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeInfoDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeQueryConditionDTO;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.mapper.EmployeeMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2019-09-24
 */
@Slf4j
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public Employee queryByIdentityNo(String identityNo) {
        List<Employee> employees = employeeMapper.selectList(new QueryWrapper<Employee>().lambda().eq(Employee::getIdentityNo, identityNo));
        if (ArrayUtils.isEmpty(employees)) {
            return null;
        }
        return employees.get(0);
    }

    @Override
    /**
     * 查询员工档案
     */
    public IPage<EmployeeInfoDTO> queryEmployeeList4Page(EmployeeQueryConditionDTO conditionDTO, Page<EmployeeQueryConditionDTO> employeePage) {
        QueryWrapper<EmployeeDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("c.org_id = b.org_id");
        queryWrapper.like(StringUtils.isNotEmpty(conditionDTO.getName()), "a.name", conditionDTO.getName());
        queryWrapper.eq(StringUtils.isNotEmpty(conditionDTO.getSex()), "a.sex", conditionDTO.getSex());
        queryWrapper.likeRight(StringUtils.isNotEmpty(conditionDTO.getMobileNo()), "a.mobile_no", conditionDTO.getMobileNo());
        queryWrapper.eq(StringUtils.isNotEmpty(conditionDTO.getEmployeeStatus()), "a.status", conditionDTO.getEmployeeStatus());
        queryWrapper.eq(StringUtils.isNotEmpty(conditionDTO.getType()), "a.type", conditionDTO.getType());
        if (!StringUtils.equals(conditionDTO.getOtherStatus(), EmployeeConst.EMPLOYEE_BORROW_STATUS) &&
                !StringUtils.equals(conditionDTO.getOtherStatus(), EmployeeConst.EMPLOYEE_TRANS_STATUS)) {
            queryWrapper.apply(StringUtils.isNotBlank(conditionDTO.getOrgSet()), "b.org_id in (" + conditionDTO.getOrgSet() + ")");
        }
        queryWrapper.orderByAsc("a.employee_id");
        //休假
        if (StringUtils.equals(conditionDTO.getOtherStatus(), EmployeeConst.EMPLOYEE_HOLIDAY_STATUS)) {
            queryWrapper.exists("select * from ins_employee_holiday ieh where a.employee_id=ieh.employee_id and (now() between ieh.start_time and ieh.end_time)");
        }
        //借调
        if (StringUtils.equals(conditionDTO.getOtherStatus(), EmployeeConst.EMPLOYEE_BORROW_STATUS)) {
            queryWrapper.exists("select * from ins_employee_trans_detail iep where a.employee_id=iep.employee_id  " +
                    "and iep.trans_type='1' and (now() between iep.start_time and iep.end_time)" +
                    "and (iep.source_org_id in(" + conditionDTO.getOrgSet() + ") or iep.org_id in (" + conditionDTO.getOrgSet() + "))");
        }
        //调出
        if (StringUtils.equals(conditionDTO.getOtherStatus(), EmployeeConst.EMPLOYEE_TRANS_STATUS)) {
            queryWrapper.exists("select * from ins_employee_trans_detail iep where a.employee_id=iep.employee_id  " +
                    "and iep.trans_type='2' and (now() between iep.start_time and iep.end_time)" +
                    "and (iep.source_org_id in(" + conditionDTO.getOrgSet() + ") or iep.org_id in (" + conditionDTO.getOrgSet() + "))");
        }
        //是黑名单
        if (StringUtils.equals(conditionDTO.getIsBlackList(), EmployeeConst.YES)) {
            queryWrapper.exists("select * from ins_employee_blacklist ieb where a.employee_id=ieb.employee_id  and (now() between ieb.start_time and ieb.end_time)");
        }
        //不是黑名单
        if (StringUtils.equals(conditionDTO.getIsBlackList(), EmployeeConst.NO)) {
            queryWrapper.notExists("select * from ins_employee_blacklist ieb where a.employee_id=ieb.employee_id  and (now() between ieb.start_time and ieb.end_time)");
        }
        IPage<EmployeeInfoDTO> iPage = employeeMapper.selectEmployeePage(employeePage, queryWrapper);

        return iPage;
    }

    /**
     * 测试（后期删除）
     */
    @Override
    public IPage<EmployeeExampleDTO> selectEmployeePageExample(String name, Long orgId, Long jobRole) {
        Page<EmployeeExampleDTO> page = new Page<>(1, 10);
        QueryWrapper<EmployeeExampleDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("b.employee_id = a.employee_id AND now() < b.end_date AND c.org_id = b.org_id");
        queryWrapper.like(StringUtils.isNotEmpty(name), "a.name", name);
        queryWrapper.eq(null != orgId, "b.org_id", orgId);
        queryWrapper.eq(null != jobRole, "b.job_role", jobRole);
        IPage<EmployeeExampleDTO> employeeExampleDTOIPage = employeeMapper.selectEmployeePageExample(page, queryWrapper);
        return employeeExampleDTOIPage;
    }

    @Override
    @Cacheable(value = "employee_name_cache")
    public String getEmployeeNameEmployeeId(Long employeeId) {
        Employee employee = this.employeeMapper.selectById(employeeId);
        if (employee == null) {
            return null;
        }
        return employee.getName();
    }

    @Override
    public Employee queryByUserId(Long userId) {
        Employee employee = this.getOne(new QueryWrapper<Employee>().lambda().eq(Employee::getUserId, userId));
        return employee;
    }

    @Override
    public List<Employee> findSubordinate(Long parentEmployeeId) {
        List<Employee> childEmployees = employeeMapper.queryEmployeeByParentEmployeeId(parentEmployeeId);
        return childEmployees;
    }

    @Override
    public List<EmployeeInfoDTO> queryEmployeeList(EmployeeQueryConditionDTO conditionDTO) {
        QueryWrapper<EmployeeQueryConditionDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("c.org_id = b.org_id");
        queryWrapper.like(StringUtils.isNotEmpty(conditionDTO.getName()), "a.name", conditionDTO.getName());
        queryWrapper.eq(StringUtils.isNotEmpty(conditionDTO.getSex()), "a.sex", conditionDTO.getSex());
        queryWrapper.likeRight(StringUtils.isNotEmpty(conditionDTO.getMobileNo()), "a.mobile_no", conditionDTO.getMobileNo());
        queryWrapper.eq(StringUtils.isNotEmpty(conditionDTO.getEmployeeStatus()), "a.status", conditionDTO.getEmployeeStatus());
        queryWrapper.eq(StringUtils.isNotEmpty(conditionDTO.getType()), "a.type", conditionDTO.getType());

        if (!StringUtils.equals(conditionDTO.getOtherStatus(), EmployeeConst.EMPLOYEE_BORROW_STATUS) &&
                !StringUtils.equals(conditionDTO.getOtherStatus(), EmployeeConst.EMPLOYEE_TRANS_STATUS)) {
            queryWrapper.apply(StringUtils.isNotBlank(conditionDTO.getOrgSet()), "b.org_id in (" + conditionDTO.getOrgSet() + ")");
        }        queryWrapper.orderByAsc("a.employee_id");

        //休假
        if (StringUtils.equals(conditionDTO.getOtherStatus(), EmployeeConst.EMPLOYEE_HOLIDAY_STATUS)) {
            queryWrapper.exists("select * from ins_employee_holiday ieh where a.employee_id=ieh.employee_id and (now() between ieh.start_time and ieh.end_time)");
        }
        //借调
        if (StringUtils.equals(conditionDTO.getOtherStatus(), EmployeeConst.EMPLOYEE_BORROW_STATUS)) {
            queryWrapper.exists("select * from ins_employee_trans_detail iep where a.employee_id=iep.employee_id  " +
                    "and iep.trans_type='1' and (now() between iep.start_time and iep.end_time)" +
                    "and (iep.source_org_id in(" + conditionDTO.getOrgSet() + ") or iep.org_id in (" + conditionDTO.getOrgSet() + "))");
        }
        //调出
        if (StringUtils.equals(conditionDTO.getOtherStatus(), EmployeeConst.EMPLOYEE_TRANS_STATUS)) {
            queryWrapper.exists("select * from ins_employee_trans_detail iep where a.employee_id=iep.employee_id  " +
                    "and iep.trans_type='2' and (now() between iep.start_time and iep.end_time)" +
                    "and (iep.source_org_id in(" + conditionDTO.getOrgSet() + ") or iep.org_id in (" + conditionDTO.getOrgSet() + "))");
        }
        //是黑名单
        if (StringUtils.equals(conditionDTO.getIsBlackList(), EmployeeConst.YES)) {
            queryWrapper.exists("select * from ins_employee_blacklist ieb where a.employee_id=ieb.employee_id  and (now() between ieb.start_time and ieb.end_time)");
        }
        //不是黑名单
        if (StringUtils.equals(conditionDTO.getIsBlackList(), EmployeeConst.NO)) {
            queryWrapper.notExists("select * from ins_employee_blacklist ieb where a.employee_id=ieb.employee_id  and (now() between ieb.start_time and ieb.end_time)");
        }
        List<EmployeeInfoDTO> list = employeeMapper.queryEmployeeList(queryWrapper);

        return list;
    }

}
