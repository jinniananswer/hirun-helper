package com.microtomato.hirun.modules.organization.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeePenaltyDTO;
import com.microtomato.hirun.modules.organization.entity.po.EmployeePenalty;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.organization.service.IEmployeePenaltyService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2019-11-14
 */
@RestController
@Slf4j
@RequestMapping("api/organization/employee-penalty")
public class EmployeePenaltyController {

    @Autowired
    private IEmployeePenaltyService employeePenaltyServiceImpl;

    @PostMapping("/addEmployeePenalty")
    @RestResult
    public void addEmployeePenalty(EmployeePenalty employeePenalty){
        employeePenaltyServiceImpl.addEmployeePenalty(employeePenalty);
    }

    @GetMapping("/queryPenaltyList")
    @RestResult
    public IPage<EmployeePenaltyDTO> queryPenaltyList(EmployeePenaltyDTO dto,Integer page,Integer limit){
        Page<EmployeePenaltyDTO> penaltyDTOPage = new Page<>(page, limit);
        return employeePenaltyServiceImpl.queryPenaltyList(dto,penaltyDTOPage);
    }

    @PostMapping("/deleteEmployeePenalty")
    @RestResult
    public void deleteEmployeePenalty(EmployeePenalty employeePenalty){
        employeePenaltyServiceImpl.deleteEmployeePenalty(employeePenalty);
    }

    @PostMapping("/updateEmployeePenalty")
    @RestResult
    public boolean updateEmployeePenalty(EmployeePenalty employeePenalty){
        return employeePenaltyServiceImpl.updateEmployeePenalty(employeePenalty);
    }
}
