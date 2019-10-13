package com.microtomato.hirun.modules.organization.service.impl;

import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeJobRoleDTO;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.service.IEmployeeDomainService;
import com.microtomato.hirun.modules.organization.service.IEmployeeJobRoleService;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 员工领域服务实现类
 * @author: jinnian
 * @create: 2019-10-08 14:54
 **/
@Slf4j
@Service
public class EmployeeDomainServiceImpl implements IEmployeeDomainService {

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IEmployeeJobRoleService employeeJobRoleService;

    @Autowired
    private IOrgService orgService;

    @Autowired
    private IStaticDataService staticDataService;

    @Override
    public List<EmployeeDTO> selectEmployee(String searchText) {
        List<Employee> employees = employeeService.searchByNameMobileNo(searchText);

        if (ArrayUtils.isEmpty(employees)) {
            return null;
        }

        List<EmployeeDTO> employeeDTOs = new ArrayList<EmployeeDTO>();
        for (Employee employee : employees) {
            EmployeeDTO employeeDTO = new EmployeeDTO();
            BeanUtils.copyProperties(employee, employeeDTO);

            EmployeeJobRole employeeJobRole = employeeJobRoleService.getValidJobRole(employee.getEmployeeId());
            if (employeeJobRole != null) {
                EmployeeJobRoleDTO jobRoleDTO = new EmployeeJobRoleDTO();
                BeanUtils.copyProperties(employeeJobRole, jobRoleDTO);
                Org org = orgService.getById(employeeJobRole.getOrgId());
                jobRoleDTO.setOrgName(org.getName());
                jobRoleDTO.setJobRoleName(staticDataService.getCodeName("JOB_ROLE", employeeJobRole.getJobRole()));
                employeeDTO.setEmployeeJobRole(jobRoleDTO);
            }

            employeeDTOs.add(employeeDTO);
        }
        return employeeDTOs;
    }
}
