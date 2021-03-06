package com.microtomato.hirun.modules.bss.salary.controller;


import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.salary.entity.dto.SalaryFixDTO;
import com.microtomato.hirun.modules.bss.salary.entity.dto.SalaryFixQueryDTO;
import com.microtomato.hirun.modules.bss.salary.service.ISalaryFixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 员工固定工资表(SalaryFix)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-17 00:26:30
 */
@RestController
@RequestMapping("/api/bss.salary/salary-fix")
public class SalaryFixController {

    /**
     * 服务对象
     */
    @Autowired
    private ISalaryFixService salaryFixService;

    /**
     * 界面查询员工固定工资数据，如果固定工资还未录入，也需要能查询出员工数据来，以供数据录入
     * @param param
     * @return
     */
    @RequestMapping("/queryFixSalary")
    @RestResult
    public List<SalaryFixDTO> queryFixSalary(@RequestBody SalaryFixQueryDTO param) {
        return this.salaryFixService.queryEmployeeSalaries(param);
    }

    /**
     * 审核界面查询员工固定工资数据
     * @param param
     * @return
     */
    @RequestMapping("/queryAuditFixSalary")
    @RestResult
    public List<SalaryFixDTO> queryAuditFixSalary(@RequestBody SalaryFixQueryDTO param) {
        return this.salaryFixService.queryAuditEmployeeSalaries(param);
    }

    @PostMapping("/submitFixSalaries")
    @RestResult
    public void submitFixSalaries(@RequestBody List<SalaryFixDTO> employeeSalaries) {
        this.salaryFixService.saveSalaries(employeeSalaries, false);
    }

    @PostMapping("/auditFixSalaries")
    @RestResult
    public void auditFixSalaries(@RequestBody List<SalaryFixDTO> employeeSalaries) {
        this.salaryFixService.saveSalaries(employeeSalaries, true);
    }

    @PostMapping("/auditPass")
    @RestResult
    public void auditPass(@RequestBody List<SalaryFixDTO> employeeSalaries) {
        this.salaryFixService.audit(employeeSalaries, true);
    }

    @PostMapping("/auditNo")
    @RestResult
    public void auditNo(@RequestBody List<SalaryFixDTO> employeeSalaries) {
        this.salaryFixService.audit(employeeSalaries, false);
    }
}