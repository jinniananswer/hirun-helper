package com.microtomato.hirun.modules.organization.controller;


import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeSalaryFixDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeSalaryFixQueryDTO;
import com.microtomato.hirun.modules.organization.service.IEmployeeSalaryFixService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 员工固定工资表(EmployeeSalaryFix)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-05 00:32:09
 */
@RestController
@Slf4j
@RequestMapping("/api/organization/employee-salary-fix")
public class EmployeeSalaryFixController {

    /**
     * 服务对象
     */
    @Autowired
    private IEmployeeSalaryFixService employeeSalaryFixService;

    /**
     * 界面查询员工固定工资数据，如果固定工资还未录入，也需要能查询出员工数据来，以供数据录入
     * @param param
     * @return
     */
    @GetMapping("/queryFixSalary")
    @RestResult
    public List<EmployeeSalaryFixDTO> queryFixSalary(EmployeeSalaryFixQueryDTO param) {
        if (StringUtils.isNotBlank(param.getOrgId())) {
            String[] orgIdArray = param.getOrgId().split(",");
            List<Long> orgIds = new ArrayList<>();
            for (String s : orgIdArray) {
                orgIds.add(Long.parseLong(s));
            }
            param.setOrgIds(orgIds);
        }
        return this.employeeSalaryFixService.queryEmployeeSalaries(param);
    }

    /**
     * 审核界面查询员工固定工资数据
     * @param param
     * @return
     */
    @GetMapping("/queryAuditFixSalary")
    @RestResult
    public List<EmployeeSalaryFixDTO> queryAuditFixSalary(EmployeeSalaryFixQueryDTO param) {
        if (StringUtils.isNotBlank(param.getOrgId())) {
            String[] orgIdArray = param.getOrgId().split(",");
            List<Long> orgIds = new ArrayList<>();
            for (String s : orgIdArray) {
                orgIds.add(Long.parseLong(s));
            }
            param.setOrgIds(orgIds);
        }
        return this.employeeSalaryFixService.queryAuditEmployeeSalaries(param);
    }

    @PostMapping("/submitFixSalaries")
    @RestResult
    public void submitFixSalaries(@RequestBody List<EmployeeSalaryFixDTO> employeeSalaries) {
        this.employeeSalaryFixService.saveSalaries(employeeSalaries, false);
    }

    @PostMapping("/auditFixSalaries")
    @RestResult
    public void auditFixSalaries(@RequestBody List<EmployeeSalaryFixDTO> employeeSalaries) {
        this.employeeSalaryFixService.saveSalaries(employeeSalaries, true);
    }

    @PostMapping("/auditPass")
    @RestResult
    public void auditPass(@RequestBody List<EmployeeSalaryFixDTO> employeeSalaries) {
        this.employeeSalaryFixService.audit(employeeSalaries, true);
    }

    @PostMapping("/auditNo")
    @RestResult
    public void auditNo(@RequestBody List<EmployeeSalaryFixDTO> employeeSalaries) {
        this.employeeSalaryFixService.audit(employeeSalaries, false);
    }


}