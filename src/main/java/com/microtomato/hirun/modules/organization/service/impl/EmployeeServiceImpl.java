package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeExampleDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeInfoDTO;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.mapper.EmployeeMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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
    public IPage<EmployeeInfoDTO> queryEmployeeList4Page(EmployeeInfoDTO employeeInfoDTO, Page<EmployeeInfoDTO> employeePage) {
        QueryWrapper<EmployeeDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("b.employee_id=a.employee_id AND (now() between b.start_date and b.end_date) AND c.org_id = b.org_id");
        queryWrapper.like(StringUtils.isNotEmpty(employeeInfoDTO.getName()), "a.name", employeeInfoDTO.getName());
        queryWrapper.eq(StringUtils.isNotEmpty(employeeInfoDTO.getSex()), "a.sex", employeeInfoDTO.getSex());
        queryWrapper.likeRight(StringUtils.isNotEmpty(employeeInfoDTO.getMobileNo()), "a.mobile_no", employeeInfoDTO.getMobileNo());
        queryWrapper.eq(StringUtils.isNotEmpty(employeeInfoDTO.getEmployeeStatus()), "a.status", employeeInfoDTO.getEmployeeStatus());
        queryWrapper.eq(StringUtils.isNotEmpty(employeeInfoDTO.getType()), "a.type", employeeInfoDTO.getType());

/*        if (StringUtils.equals(employeeInfoDTO.getIsBlackList(), "on")) {
            queryWrapper.apply("EXISTS (select * FROM ins_employee_blacklist bb WHERE bb.employee_id = a.employee_id) ");
        }*/
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
        Employee employee=this.employeeMapper.selectById(employeeId);
        if(employee ==null){
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
    public List<EmployeeInfoDTO> queryEmployeeList(EmployeeInfoDTO employeeInfoDTO) {
        QueryWrapper<EmployeeDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("b.employee_id=a.employee_id AND (now() between b.start_date and b.end_date) AND c.org_id = b.org_id");
        queryWrapper.like(StringUtils.isNotEmpty(employeeInfoDTO.getName()), "a.name", employeeInfoDTO.getName());
        queryWrapper.eq(StringUtils.isNotEmpty(employeeInfoDTO.getSex()), "a.sex", employeeInfoDTO.getSex());
        queryWrapper.likeRight(StringUtils.isNotEmpty(employeeInfoDTO.getMobileNo()), "a.mobile_no", employeeInfoDTO.getMobileNo());
        queryWrapper.eq(StringUtils.isNotEmpty(employeeInfoDTO.getEmployeeStatus()), "a.status", employeeInfoDTO.getEmployeeStatus());
        List<EmployeeInfoDTO> list=employeeMapper.queryEmployeeList(queryWrapper);

        return list;
    }

}
