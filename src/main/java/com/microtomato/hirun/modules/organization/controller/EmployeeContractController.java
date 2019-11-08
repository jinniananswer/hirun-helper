package com.microtomato.hirun.modules.organization.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeContract;
import com.microtomato.hirun.modules.organization.service.IEmployeeContractDomainService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.organization.service.IEmployeeContractService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2019-11-05
 */
@RestController
@Slf4j
@RequestMapping("api/organization/employee-contract")
public class EmployeeContractController {


    @Autowired
    private IEmployeeContractDomainService domainService;

    @PostMapping("/createEmployeeContract")
    @RestResult
    public void createEmployeeContract(EmployeeContract employeeContract){
        domainService.createEmployeeContract(employeeContract);
    }

    @GetMapping("/queryEmployeeContracts")
    @RestResult
    public IPage<EmployeeContract> queryEmployeeContracts(Long employeeId, Integer page, Integer limit){
        Page<EmployeeContract> contractPage = new Page<>(page, limit);
        IPage<EmployeeContract> iPage=domainService.queryEmployeeContracts(employeeId,contractPage);
        return iPage;
    }

    @PostMapping("/deleteEmployeeContract")
    @RestResult
    public boolean deleteEmployeeContract(EmployeeContract employeeContract){
        return domainService.deleteEmployeeContract(employeeContract);
    }

    @PostMapping("/updateEmployeeContract")
    @RestResult
    public boolean updateEmployeeContract(EmployeeContract employeeContract){
        return domainService.updateEmployeeContract(employeeContract);
    }
}
