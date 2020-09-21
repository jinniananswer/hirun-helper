package com.microtomato.hirun.modules.bss.salary.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.salary.entity.dto.SalaryMonthlyDTO;
import com.microtomato.hirun.modules.bss.salary.entity.dto.SalaryMonthlyQueryDTO;
import com.microtomato.hirun.modules.bss.salary.entity.po.SalaryMonthly;

import java.util.List;

/**
 * 员工月工资总表(SalaryMonthly)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-17 00:26:31
 */
public interface ISalaryMonthlyService extends IService<SalaryMonthly> {

    List<SalaryMonthlyDTO> queryEmployeeSalaries(SalaryMonthlyQueryDTO param);

    List<SalaryMonthlyDTO> queryAuditEmployeeSalaries(SalaryMonthlyQueryDTO param);

    List<SalaryMonthly> queryByMonth(Integer salaryMonth);

    void saveSalaries(List<SalaryMonthlyDTO> salaries, boolean isAudit);

    void audit(List<SalaryMonthlyDTO> employeeSalaries, boolean isPass);

    SalaryMonthly getByEmployeeIdMonth(Long employeeId, Integer salaryMonth);

    void updateRoyalties(Long totalRoyalty, Integer salaryMonth, Long employeeId);

    void updateEmployeeMonthly(Long employeeId, String employeeName, String salaryItem, Long money, Integer salaryMonth);
}