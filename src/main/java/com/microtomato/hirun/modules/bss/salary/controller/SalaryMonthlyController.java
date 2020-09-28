package com.microtomato.hirun.modules.bss.salary.controller;


import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.salary.entity.dto.SalaryMonthlyDTO;
import com.microtomato.hirun.modules.bss.salary.entity.dto.SalaryMonthlyQueryDTO;
import com.microtomato.hirun.modules.bss.salary.service.ISalaryMonthlyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 员工月工资总表(SalaryMonthly)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-17 00:26:31
 */
@RestController
@RequestMapping("/api/bss.salary/salary-monthly")
public class SalaryMonthlyController {

    /**
     * 服务对象
     */
    @Autowired
    private ISalaryMonthlyService salaryMonthlyService;

    /**
     * 界面查询员工某月工资数据，如果该月还未录入，也需要能查询出员工数据来，以供数据录入
     * @param param
     * @return
     */
    @RequestMapping("/querySalary")
    @RestResult
    public List<SalaryMonthlyDTO> querySalary(@RequestBody SalaryMonthlyQueryDTO param) {
        return this.salaryMonthlyService.queryEmployeeSalaries(param);
    }

    /**
     * 审核界面查询员工某月工资数据
     * @param param
     * @return
     */
    @RequestMapping("/queryAuditSalary")
    @RestResult
    public List<SalaryMonthlyDTO> queryAuditFixSalary(@RequestBody SalaryMonthlyQueryDTO param) {
        return this.salaryMonthlyService.queryAuditEmployeeSalaries(param);
    }

    @PostMapping("/submitSalaries")
    @RestResult
    public void submitSalaries(@RequestBody List<SalaryMonthlyDTO> employeeSalaries) {
        this.salaryMonthlyService.saveSalaries(employeeSalaries, false);
    }

    @PostMapping("/auditSalaries")
    @RestResult
    public void auditSalaries(@RequestBody List<SalaryMonthlyDTO> employeeSalaries) {
        this.salaryMonthlyService.saveSalaries(employeeSalaries, true);
    }

    @PostMapping("/auditPass")
    @RestResult
    public void auditPass(@RequestBody List<SalaryMonthlyDTO> employeeSalaries) {
        this.salaryMonthlyService.audit(employeeSalaries, true);
    }

    @PostMapping("/auditNo")
    @RestResult
    public void auditNo(@RequestBody List<SalaryMonthlyDTO> employeeSalaries) {
        this.salaryMonthlyService.audit(employeeSalaries, false);
    }
}