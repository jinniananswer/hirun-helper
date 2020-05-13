package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeSalaryFixDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeSalaryFixQueryDTO;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeSalaryFix;

import java.util.List;

/**
 * 员工固定工资表(EmployeeSalaryFix)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-04 23:52:25
 */
public interface IEmployeeSalaryFixService extends IService<EmployeeSalaryFix> {

    List<EmployeeSalaryFixDTO> queryEmployeeSalaries(EmployeeSalaryFixQueryDTO param);

    List<EmployeeSalaryFixDTO> queryAuditEmployeeSalaries(EmployeeSalaryFixQueryDTO param);

    List<EmployeeSalaryFix> queryAllValid();

    void saveSalaries(List<EmployeeSalaryFixDTO> salaries, boolean isAudit);

    void audit(List<EmployeeSalaryFixDTO> employeeSalaries, boolean isPass);
}