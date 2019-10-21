package com.microtomato.hirun.modules.organization.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeDestroyInfoDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeExampleDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeQueryInfoDTO;
import com.microtomato.hirun.modules.organization.service.IEmployeeDomainService;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2019-09-24
 */
@RestController
@Slf4j
@RequestMapping("/api/organization/employee")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeServiceImpl;

    @Autowired
    private IEmployeeDomainService employeeDomainServiceImpl;

    @PostMapping("/create")
    @RestResult
    public Map create(EmployeeDTO employee) {
        employeeDomainServiceImpl.employeeEntry(employee);
        return new HashMap();
    }

    @GetMapping("/selectEmployee")
    @RestResult
    public List<EmployeeDTO> selectEmployee(String searchText) {
        return employeeDomainServiceImpl.selectEmployee(searchText);
    }

    @GetMapping("/selectEmployeeList")
    @RestResult
    public IPage<EmployeeQueryInfoDTO> employeeList(EmployeeQueryInfoDTO employeeQueryInfoDTO, Integer page, Integer limit) {
        Page<EmployeeQueryInfoDTO> employeeInfoDTOPage = new Page<>(page, limit);
        IPage<EmployeeQueryInfoDTO> employeeList = employeeDomainServiceImpl.queryEmployeeList(employeeQueryInfoDTO,employeeInfoDTOPage);
        return employeeList;
    }

    @PostMapping("/destroyEmployee")
    @RestResult
    public boolean destroyEmployee(EmployeeDestroyInfoDTO employeeDestroyInfoDTO) {
        boolean destroyRusult=employeeDomainServiceImpl.destroyEmployee(employeeDestroyInfoDTO);
        return destroyRusult;
    }

    /**
     * 测试后期删除
     */
    @GetMapping("/selectEmployeePageExample")
    @RestResult
    public IPage<EmployeeExampleDTO> selectEmployeePageExample(EmployeeExampleDTO stevenDTO) {
        return employeeServiceImpl.selectEmployeePageExample(stevenDTO.getName(), stevenDTO.getOrgId(), stevenDTO.getJobRole());
    }
}
