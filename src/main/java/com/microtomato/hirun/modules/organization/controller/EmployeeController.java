package com.microtomato.hirun.modules.organization.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeDTO;
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
        log.debug("================name:"+employee.getName());
        log.debug("================natives:"+employee.getNatives());
        log.debug("================workExperiences:"+employee.getEmployeeWorkExperiences());
        log.debug("================workExperiences:"+employee.getEmployeeWorkExperiences().size());
        log.debug("================workExperiences:"+employee.getEmployeeWorkExperiences().get(0).getStartDate());
        log.debug("================workExperiences:"+employee.getEmployeeWorkExperiences().get(1));
        return new HashMap();
    }

    @GetMapping("/selectEmployee")
    @RestResult
    public List<EmployeeDTO> selectEmployee(String searchText) {
        return employeeDomainServiceImpl.selectEmployee(searchText);
    }
}
