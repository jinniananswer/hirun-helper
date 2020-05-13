package com.microtomato.hirun.modules.organization.controller;


import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeSalaryDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeSalaryQueryDTO;
import com.microtomato.hirun.modules.organization.service.IEmployeeSalaryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 员工固定工资表(EmployeeSalary)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-02 00:25:10
 */
@RestController
@Slf4j
@RequestMapping("/api/organization/employee-salary")
public class EmployeeSalaryController {

    /**
     * 服务对象
     */
    @Autowired
    private IEmployeeSalaryService employeeSalaryService;

    /**
     * 界面查询员工某月工资数据，如果该月还未录入，也需要能查询出员工数据来，以供数据录入
     * @param param
     * @return
     */
    @GetMapping("/querySalary")
    @RestResult
    public List<EmployeeSalaryDTO> querySalary(EmployeeSalaryQueryDTO param) {
        if (StringUtils.isNotBlank(param.getOrgId())) {
            String[] orgIdArray = param.getOrgId().split(",");
            List<Long> orgIds = new ArrayList<>();
            for (String s : orgIdArray) {
                orgIds.add(Long.parseLong(s));
            }
            param.setOrgIds(orgIds);
        }
        return this.employeeSalaryService.queryEmployeeSalaries(param);
    }

    /**
     * 审核界面查询员工某月工资数据
     * @param param
     * @return
     */
    @GetMapping("/queryAuditSalary")
    @RestResult
    public List<EmployeeSalaryDTO> queryAuditFixSalary(EmployeeSalaryQueryDTO param) {
        if (StringUtils.isNotBlank(param.getOrgId())) {
            String[] orgIdArray = param.getOrgId().split(",");
            List<Long> orgIds = new ArrayList<>();
            for (String s : orgIdArray) {
                orgIds.add(Long.parseLong(s));
            }
            param.setOrgIds(orgIds);
        }
        return this.employeeSalaryService.queryAuditEmployeeSalaries(param);
    }

    @PostMapping("/submitSalaries")
    @RestResult
    public void submitSalaries(@RequestBody List<EmployeeSalaryDTO> employeeSalaries) {
        this.employeeSalaryService.saveSalaries(employeeSalaries, false);
    }

    @PostMapping("/auditSalaries")
    @RestResult
    public void auditSalaries(@RequestBody List<EmployeeSalaryDTO> employeeSalaries) {
        this.employeeSalaryService.saveSalaries(employeeSalaries, true);
    }

    @PostMapping("/auditPass")
    @RestResult
    public void auditPass(@RequestBody List<EmployeeSalaryDTO> employeeSalaries) {
        this.employeeSalaryService.audit(employeeSalaries, true);
    }

    @PostMapping("/auditNo")
    @RestResult
    public void auditNo(@RequestBody List<EmployeeSalaryDTO> employeeSalaries) {
        this.employeeSalaryService.audit(employeeSalaries, false);
    }
}